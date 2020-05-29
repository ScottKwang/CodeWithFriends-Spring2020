//js code for the listening side
const radioName = window.location.pathname.split('/')[1] + "'s Radio"

//updating page display
window.onload = () =>{
  document.querySelector("title").innerText = radioName;
  document.querySelector("h1").innerText= radioName;
  document.querySelector("#songInfo").innerHTML = `<code>Switch to the device called ${radioName} on your Spotify application`
}

//joins the socket for the page
const socket = io.connect(window.location.href)

//webSDK event
window.onSpotifyWebPlaybackSDKReady = () => {
  let device = null
  let currentSong = {}
  //getting data
  fetch("/account/data",{
    method: "POST",
}).then((response)=>{
    if(response.status == 200){
        return response.json()
    }
}).then((data) => {
      //creating web player
      const spotify_token = data.spotify_token;
      const player = new Spotify.Player({
        name: radioName,
        getOAuthToken: cb => { cb(spotify_token); }
      });
      
      //Error handling
      player.addListener('initialization_error', ({ message }) => { console.error(message); });
      player.addListener('authentication_error', ({ message }) => { 
        //refreshing token
        console.error(message);
        if(message){
        fetch("/refresh",{
            method:"POST"
        })
        window.location.reload();
    }});
      player.addListener('account_error', ({ message }) => { console.error(message); });
      player.addListener('playback_error', ({ message }) => { console.error(message); });
      //joining the radio by switching devices
      player.addListener('ready', ({ device_id }) => {
        device = device_id
        if(currentSong.paused){
          updateSong(currentSong)
        }
      });

      //leaving radio 
      player.addListener('not_ready', ({ device_id }) => {
        device = null
      });


      // Connect to the player!
      player.connect();

      //updating the current song
      const updateSong = (song) =>{
        //if song is different
        if(device != null && (!currentSong.song || currentSong.song.uri!=song.song.uri)){
          currentSong.song = song.song
          console.log(song.song.uri)
          fetch(`https://api.spotify.com/v1/me/player/play?device_id=${device}`,{
            method: "PUT",
            body: JSON.stringify({
              uris: [song.song.uri],
              position_ms: song.position
            }),
            headers:{
              'Content-Type': 'application/json',
              'Authorization': "Bearer "+spotify_token
            }
          })
        }
        //if just position is different
        else if(currentSong.position!=song.position){
          currentSong.position = song.position
          player.seek(song.position)
        }
        //if song is paused
        if(currentSong.paused!=song.paused){
          currentSong.paused = song.paused
          player.togglePlay()
        }
        //display
        document.querySelector("#songInfo").innerHTML = `<h1>${currentSong.song.name}</h1><h2>${currentSong.song.artists[0].name}</h2><img src = ${currentSong.song.album.images[0].url}>`
      }

      //listens for the socket
      socket.on('command', (msg)=>{
        updateSong(msg)
      });
    })
    };

    