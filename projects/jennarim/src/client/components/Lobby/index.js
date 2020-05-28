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
            <div id="lobby">
                <form>
                    Type room id: <input type="text"></input>
                    <input type="submit"></input>
                </form>
            </div>
        );
    }
}

export default Lobby;