const spotifyButton = document.querySelector("#connect-spotify")

//redirects through the spotify auth system
spotifyButton.onclick = () =>{
    window.location = "/spotify/connect"
}

//gets the account data 
fetch("/account/data",{
    method: "POST",
}).then((response)=>{
    if(response.status == 200){
        return response.json()
    }
}).then((data) => {
    document.querySelector("#name").innerText = data.username
    if(data.spotify_token){
        //gets rid of the connect button
        document.querySelector("div").innerHTML = ""
}
});


