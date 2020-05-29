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

let intervals = [];

class Canvas extends React.Component {
    componentDidMount() {
        const socket = this.props.socket;
        const canvas = this.refs.canvas;
        const ctx = canvas.getContext('2d');

        socket.on('game start', function() {
            let lastUpdateTime = (new Date()).getTime();
            let interval = setInterval(function() {
                const currentTime = (new Date()).getTime();
                const timeDifference = currentTime - lastUpdateTime;
                socket.emit('ball move', {timeDifference});
                lastUpdateTime = currentTime;
            }, 1000 / 60);
            intervals.push(interval);
        });

        socket.on('restart', function() {
            for (const interval of intervals) {
                clearInterval(interval);
            }

            // Black background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = c.BG_COLOR;
            ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

            // Show someone disconnected
            ctx.font = '25px serif';
            ctx.fillStyle = 'white';
            ctx.textAlign = "center";
            ctx.fillText("Someone disconnected... Please leave the room!", c.WIDTH/2 - 10, c.HEIGHT/2 - 20);
        });

        socket.on('game over', function(winnerList) {
            for (const interval of intervals) {
                clearInterval(interval);
            }

            // Show someone disconnected
            ctx.font = '25px serif';
            ctx.textAlign = 'center';

            ctx.fillStyle = 'white';
            let msg = "WINNER";
            ctx.fillText(msg, c.WIDTH/2 - 10, c.HEIGHT/2 - 75);

            let offset = 50;
            for (const winner of winnerList) {
                const playerNo = winner.playerNo;
                const color = winner.color;
                ctx.fillStyle = color;
                msg = playerNo + " ";
                ctx.fillText(msg, c.WIDTH/2 - 10, c.HEIGHT/2 - offset);
                offset -= 25;
            }
           
            ctx.fillStyle = 'white';
            msg = "Thanks for playing!";
            ctx.fillText(msg, c.WIDTH/2 - 10, c.HEIGHT/2 + 50);
        });

        socket.on('state', function(data) {
            // Draw background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = c.BG_COLOR;
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
            <canvas ref="canvas" width={c.WIDTH} height={c.HEIGHT} className="hide">
                Sorry, your browser is not supported.
            </canvas>
        );
    }
}

export default Canvas;