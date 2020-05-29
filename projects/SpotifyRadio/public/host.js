
//js for a host to publish their spotify
const songInfo = document.querySelector("#song")
const streamButton = document.querySelector("button")
const token = document.cookie.split('; ').find(row => row.startsWith('token')).split('=')[1];
let streaming = false
let socket = null

//event for webSDK
window.onSpotifyWebPlaybackSDKReady = () => {
    //gets the current accounts data
    fetch("/account/data",{
        method: "POST",
    }).then((response)=>{
        if(response.status == 200){
            return response.json()
        }
    //uses the data to create a web player
    }).then((data) => {
    streamButton.onclick = () => toggleStream(data.username)
    const spotify_token = data.spotify_token;
    //contr
    const player = new Spotify.Player({
      name: 'Spotify Radio',
      getOAuthToken: cb => { cb(spotify_token); }
    });
    // Error handling
    player.addListener('initialization_error', ({ message }) => { console.error(message); });
    player.addListener('authentication_error', ({ message }) => { 
        //likely token is expired so it refreshes it
        console.error(message);
        if(message){
        fetch("/refresh",{
            method:"POST"
        })
        window.location.reload();
    }
     });
    player.addListener('account_error', ({ message }) => { console.error(message); });
    player.addListener('playback_error', ({ message }) => { console.error(message); });
  
    //listening for updates
    player.addListener('player_state_changed', state => { 
        if(state != null){
        const currentSong = state.track_window.current_track
        songInfo.innerHTML = `<h1>${currentSong.name}</h1><h2>${currentSong.artists[0].name}</h2><img src = ${currentSong.album.images[0].url}>`
        //pushes the updates to the server
        if(streaming){
        socket.emit('update', {
            token: token,
            data:{
                position: state.position,
                paused: state.paused,
                song: currentSong
            }
        });
    }
        }
        //is the person doesn't have the radio device set
        else{
            songInfo.innerHTML = "<code>Please select the device Spotify Radio on your Spotify App</code>"
        }
    });
    player.connect();
})
  };

//creates and destroys the websocket
const toggleStream = (username) =>{
      streaming = !streaming
      if(streaming){
        socket = io.connect(window.location.origin+"/"+username)
        fetch("/start-stream",{
            method: "POST"
        })
        streamButton.innerText = "Stop Radio"
      }
      else{
        socket.disconnect()
        fetch("/stop-stream",{
            method: "POST"
        })
        streamButton.innerText = "Start Radio"
      }
  }
  