import React from 'react';

import c from './../../../lib/constants.js';
import Paddle from './../../../lib/Paddle.js';
import Ball from './../../../lib/Ball.js';
import Wall from '../../../lib/Wall.js';

function rotateBy(ctx, x, y, width, height, degrees) {
    const translateXBy = x + (width / 2);
    const translateYBy = y + (height / 2);
    ctx.translate(translateXBy, translateYBy);
    ctx.rotate(degrees * (Math.PI / 180));
    ctx.translate(-1 * translateXBy, -1 * translateYBy);
}

function handleMouseMove(event, socket, ctx) {
    const bounds = document.querySelector('canvas').getBoundingClientRect();

    // Convert mouse coordinates to be relative to the canvas, not the entire window
    let mouseX = (event.clientX - bounds.left - c.BORDER_LENGTH) - (c.PADDLE_SHORT_LENGTH/2);
    let mouseY = (event.clientY - bounds.top - c.BORDER_LENGTH) - (c.PADDLE_LONG_LENGTH/2);

    socket.emit('player move', {x: mouseX, y: mouseY});
}

function handleRestart(event) {
    if (event.keyCode === 82) {
        socket.emit('restart');
    }
}

let gameActive = true;

class Canvas extends React.Component {
    componentDidMount() {
        const socket = this.props.socket;
        const canvas = this.refs.canvas;
        const ctx = canvas.getContext('2d');
        ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

        socket.on('game start', function() {
            // move ball
            let lastUpdateTime = (new Date()).getTime();
            setInterval(function() {
                const currentTime = (new Date()).getTime();
                const timeDifference = currentTime - lastUpdateTime;
                socket.emit('ball move', {timeDifference});
                lastUpdateTime = currentTime;
            }, 1000 / 60);
            document.addEventListener("keydown", handleRestart);
        });

        socket.on('restart', function() {
            console.log("restarting...");
            gameActive = false;

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

        socket.on('state', function(data) {
            if (!gameActive) return;
            // Draw background
            ctx.clearRect(0, 0, c.WIDTH, c.HEIGHT);
            ctx.fillStyle = "black";
            ctx.fillRect(0, 0, c.WIDTH, c.HEIGHT);

            // Draw walls
            data.walls.forEach(wall => { 
                Object.setPrototypeOf(wall, Wall.prototype);
                wall.render(ctx);
            });

            // Draw each paddle
            const playerPaddles = data.playerPaddles;
            for (const socketID in playerPaddles) {
                const playerPaddle = playerPaddles[socketID];
                Object.setPrototypeOf(playerPaddle, Paddle.prototype);
                playerPaddle.render(ctx);
            }

            // Draw ball
            const ball = data.ball;
            if (ball) {
                Object.setPrototypeOf(ball, Ball.prototype);
                ball.render(ctx);
            }

            // Draw each score
            const playerScores = data.playerScores;
            for (const playerNo in playerScores) {
                const score = playerScores[playerNo];
                ctx.font = '30px serif';
                ctx.fillStyle = 'white';
                switch (playerNo) {
                    case '1':
                        ctx.fillText(score, 20, c.HEIGHT/2);
                        break;
                    case '2':
                        ctx.fillText(score, c.WIDTH - 40, c.HEIGHT/2);
                        break;
                    case '3':
                        ctx.fillText(score, c.WIDTH/2, 40);
                        break;
                    case '4':
                        ctx.fillText(score, c.WIDTH/2, c.HEIGHT - 40);
                        break;
                }
            }
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