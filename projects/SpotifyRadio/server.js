//import required libraries
const express = require("express");
const app = express();
const http = require("http").createServer(app);
const io = require("socket.io")(http);
const fs = require("fs");
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser')
const bcrypt = require("bcrypt");
const uuidToken = require('uuid-token-generator');
const https = require("https");
const querystring = require("querystring");

//Auth Constants
const SALT_ROUNDS = 10;
const tokenGenerator = new uuidToken();

//spotify API constants
const CLIENT_ID = "94353135a0fe4bf1ad43d5b5aaffdc63"
const CLIENT_SECRET = "4af7105cd3c84ef199975db9711ce033"
const redirect_uri = "http://localhost:3000/spotify/callback"

//pushes public folder
app.use(express.static(__dirname + "/public"));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cookieParser())

//importing database
const stations = JSON.parse(fs.readFileSync(__dirname + "/database/stations.json"));
const accounts = JSON.parse(fs.readFileSync(__dirname + "/database/accounts.json"));
const tokens = JSON.parse(fs.readFileSync(__dirname + "/database/tokens.json"));

//starts the stream on the hosts socket
app.post("/start-stream",(req,res)=>{
  const hostToken = req.cookies.token
  let current_song = {}
  const currentStation = io.of('/'+tokens[hostToken].username)
    .on('connection',(socket)=>{
      socket.emit('command',current_song)
      socket.on('update',(command)=>{
        if(command.token == hostToken){
          current_song = command.data
          currentStation.emit("command",current_song)
      }
      })
    })
  res.sendStatus(200)
})

//stops the stream on the hosts socket
app.post("/stop-stream",(req,res)=>{
  const hostToken = req.cookies.token
  let current_song = {}
  const currentStation = io.of('/'+tokens[hostToken].username)
  currentStation.removeAllListeners()
  res.sendStatus(200)
})

//getting data about the account logged in
app.post("/account/data",(req,res)=>{
  const stationData = stations[tokens[req.cookies.token].username]
  if(stationData){
    const accountData = {
      username: tokens[req.cookies.token].username,
      spotify_token: stationData.spotify_token,
      refresh_token: stationData.refresh_token
    }
    res.status(200).send(accountData);
  }
  else{
    res.sendStatus(409);
  }
})



//connecting spotify
app.get("/spotify/connect",(req,res)=>{
  const scopes =
  "user-read-currently-playing user-read-playback-position user-read-playback-state user-modify-playback-state streaming user-read-email user-read-private";
  res.redirect(
  "https://accounts.spotify.com/authorize" +
    "?response_type=code" +
    "&client_id=" +
    CLIENT_ID +
    "&scope=" +
    encodeURIComponent(scopes) +
    "&redirect_uri=" +
    redirect_uri
  +'&show_dialog=true'
);
})

//callback for Oauth2 Authentication Handshake
app.get("/spotify/callback",(req,res)=>{
  if (req.query.error) {
    res.send("you didn't approve it :(");
  } else {
    const code = req.query.code;
    const body = querystring.stringify({
      grant_type: "authorization_code",
      code: code,
      redirect_uri: redirect_uri
    });
    const opt = {
      hostname: "accounts.spotify.com",
      path: "/api/token",
      port: 443,
      method: "POST",
      headers: {
        Authorization:
          "Basic " +
          Buffer.from(CLIENT_ID + ":" + CLIENT_SECRET).toString("base64"),
        "Content-Type": "application/x-www-form-urlencoded",
        "Content-Length": body.length
      }
    };
    const getToken = https.request(opt, response => {
      response.on("data", d => {
        let data = JSON.parse(d);
        let user = {}
        user.spotify_token = data.access_token
        user.refresh_token = data.refresh_token
        stations[tokens[req.cookies.token].username]=user
        fs.writeFileSync("./database/stations.json",JSON.stringify(stations));
      });
      response.on("error", e => {
        console.log(e);
      });
    });
    getToken.write(body);
    getToken.end();
    res.redirect("/account");
  }
});

//refreshing the access token
app.post("/refresh",(req,res)=>{
  if(req.cookies.token && stations[tokens[req.cookies.token].username]){
  const station = stations[tokens[req.cookies.token].username]
  const body = querystring.stringify({
    grant_type: "refresh_token",
    refresh_token: station.refresh_token
  });
  const opt = {
    hostname: "accounts.spotify.com",
    path: "/api/token",
    port: 443,
    method: "POST",
    headers: {
      Authorization:
        "Basic " +
        Buffer.from(CLIENT_ID + ":" + CLIENT_SECRET).toString("base64"),
      "Content-Type": "application/x-www-form-urlencoded",
      "Content-Length": body.length
    }
  };
  const getToken = https.request(opt, response => {
    response.on("data", d => {
      let data = JSON.parse(d);
      if(data.refresh_token){station.refresh_token = data.refresh_token;}
      station.spotify_token = data.access_token;
    });
    response.on("error", e => {
      console.log(e);
    });
  });
  getToken.write(body);
  getToken.end();
  res.sendStatus(200);
}else{res.sendStatus(409)}
})

//post request to create a new account
app.post("/create-account", (req,res)=>{
  if(stations[(req.body.username).toLowerCase()]){
    res.sendStatus(403);
  }
  else if(accounts[(req.body.email).toLowerCase()]){
    res.sendStatus(409)
  }
  else{
    const password = bcrypt.hashSync(req.body.password,SALT_ROUNDS)
    const token = tokenGenerator.generate()
    accounts[(req.body.email).toLowerCase()] = {
      "username": req.body.username,
      "password": password,
      "token": token
    }
    tokens[token] = {
      "username":req.body.username
    }
    fs.writeFileSync("./database/accounts.json",JSON.stringify(accounts));
    fs.writeFileSync("./database/tokens.json",JSON.stringify(tokens));
    res.cookie("token",token).sendStatus(201);
  }
});

//login request
app.post("/login",(req,res)=>{
  if(accounts[req.body.email]&&bcrypt.compareSync(req.body.password,accounts[req.body.email].password)){
    res.cookie("token",accounts[req.body.email].token).sendStatus(200)
  }
  else{
    res.sendStatus(401)
  }
});


//redirects to the account page
app.get("/account", (req,res)=>{
  res.sendFile(__dirname + "/views/account.html")
});

//redirects to the host page
app.get("/host",(req,res)=>{
  res.sendFile(__dirname + "/views/host.html");
})


//login page
app.get("/login",(req,res)=>{
  res.sendFile(__dirname + "/views/login.html");
});

//create account page
app.get("/create-account",(req,res)=>{
  res.sendFile(__dirname + "/views/create-account.html");
});

//router for channels. routes everthing to the watch page but lets the url specify channel
app.get("/:room", (req, res) => {
  if (!stations[req.params.room]){
    res.send(req.params.room+" is not a broadcaster");
  }
  else{
  res.sendFile(__dirname + "/views/listen.html");
    }
});

//default homepage. will have a browse and list of stations
app.get("/", (req, res) => {
  res.sendFile(__dirname + "/views/index.html");
});

//tells the server to listen on a port
http.listen(3000, () => {
  console.log("listening on *: 3000" );
});
