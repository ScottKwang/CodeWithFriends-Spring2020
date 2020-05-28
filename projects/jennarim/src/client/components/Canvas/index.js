import React from 'react';
import c from './../../../lib/constants.js';
import Paddle from './../../../lib/Paddle.js';
import Player from './../../../lib/Player.js';
import Ball from './../../../lib/Ball.js';
import Wall from './../../../lib/Wall.js';
import Score from './../../../lib/Score.js';

function handleMouseMove(event, socket, ctx) {
    const bounds = document.querySelector('canvas').getBoundingClientRect();

    // Convert mouse coordinates to be relative to the canvas, not the entire window
    let mouseX = (event.clientX - bounds.left - c.BORDER_LENGTH) - (c.PADDLE_SHORT_LENGTH/2);
    let mouseY = (event.clientY - bounds.top - c.BORDER_LENGTH) - (c.PADDLE_LONG_LENGTH/2);

    socket.emit('player move', {x: mouseX, y: mouseY});
}

class Canvas extends React.Component {
    componentDidMount() {
        const socket = this.props.socket;
        const canvas = this.refs.canvas;
        const ctx = canvas.getContext('2d');

        socket.on('game start', function() {
            let lastUpdateTime = (new Date()).getTime();
            setInterval(function() {
                const currentTime = (new Date()).getTime();
                const timeDifference = currentTime - lastUpdateTime;
                socket.emit('ball move', {timeDifference});
                lastUpdateTime = currentTime;
            }, 1000 / 60);
        });

        socket.on('restart', function() {
            // Black background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = "black";
            ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

            // Show someone disconnected
            ctx.font = '25px serif';
            ctx.fillStyle = 'white';
            ctx.textAlign = "center";
            ctx.fillText("Someone disconnected... Please leave the room!", c.WIDTH/2 - 10, c.HEIGHT/2 - 20);
        });

        socket.on('game over', function(winner) {
            // Black background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = "black";
            ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

            // Show someone disconnected
            ctx.font = '25px serif';
            ctx.fillStyle = 'white';
            ctx.textAlign = 'center';
            let msg = "Game over. ";
            msg += (winner === "tie" ? "All players tied. " : "Player " + winner + " won. ");
            msg += "Thanks for playing!";
            ctx.fillText(msg, c.WIDTH/2 - 10, c.HEIGHT/2 - 20);
        });

        socket.on('state', function(data) {
            console.log("state received!");
            // Draw background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = "black";
            ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

            // Draw walls
            data.walls.forEach(wall => { 
                Object.setPrototypeOf(wall, Wall.prototype);
                wall.render(ctx);
            });

            // Draw ball
            const ball = data.ball;
            if (ball) {
                Object.setPrototypeOf(ball, Ball.prototype);
                ball.render(ctx);
            }

            // Display paddle and score
            const players = data.players;
            for (const socketID in players) {
                if (players.hasOwnProperty(socketID)) {
                    const player = players[socketID];
                    Object.setPrototypeOf(player, Player.prototype);

                    // Draw each paddle
                    const paddle = player.getPaddle();
                    Object.setPrototypeOf(paddle, Paddle.prototype);
                    paddle.render(ctx);

                    // Draw each score
                    const score = player.getScore();
                    Object.setPrototypeOf(score, Score.prototype);
                    score.render(ctx);
                }
            }

            // Display remaining time
            const time = data.time;
            ctx.font = '20px serif';
            ctx.fillStyle = 'white';
            ctx.fillText('Time: ' + time, 20, 40);
        });

        document.addEventListener("mousemove", function(event) {
            handleMouseMove(event, socket, ctx);
        });
    }

    render() {
        return (
            <canvas ref="canvas" width={c.WIDTH} height={c.HEIGHT} className="hide" />
        );
    }
}

export default Canvas;