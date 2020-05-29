import React from 'react';

function handleRoomWithSocket(lobby, socket) {
    return function(event) {
        event.preventDefault();
        const roomName = lobby.querySelector("input").value;
        socket.emit('join pending', roomName);
    };
}

class Lobby extends React.Component {
    componentDidMount() {
        const socket = this.props.socket;
        const lobby = document.getElementById("lobby");
        const form = lobby.querySelector("form");
        const roomCodeInput = document.getElementById("roomcode");
        const playBtn = document.getElementById("play-btn");

        roomCodeInput.addEventListener("input", function(event) {
            if (roomCodeInput.value.length === 4) {
                playBtn.disabled = false;
            } else {
                playBtn.disabled = true;
            }
        });
        form.addEventListener("submit", handleRoomWithSocket(lobby, socket));

        // Room is full
        socket.on('join failure', function() {
            // Display room is full message
            lobby.appendChild(document.createTextNode("This room is full. Enter another name."));
        });

        // Player able to join room
        socket.on('join success', function() {
            // Socket becomes a new player
            socket.emit('new player');

            // Remove lobby interface
            lobby.classList.add('hide');

            // Show canvas interface
            const canvas = document.querySelector("canvas");
            canvas.classList.remove('hide');
        });
    }

    render() {
        return (
            <div id="lobby" className="max-w-md max-h-md m-auto">
                <form>
                    <fieldset>
                        <div className="form-group">
                            <label htmlFor="roomcode" className="text-lobby"> ROOM CODE </label>
                            <input name="roomcode" id="roomcode" className="input-lobby" type="text" maxLength="4" placeholder="Enter 4-letter room code to create or join"></input>
                            <div className="px-4">
                                <button type="submit" id="play-btn" className="my-3" disabled>PLAY</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        );
    }
}

export default Lobby;