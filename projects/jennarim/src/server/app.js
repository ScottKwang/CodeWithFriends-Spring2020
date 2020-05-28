const express = require('express');
const path = require('path');
const app = express();
const Paddle = require('./../lib/Paddle.js');
const Ball = require('./../lib/Ball.js');
const Wall = require('./../lib/Wall.js');
const Room = require('./../lib/Room.js');
const Goal = require('./../lib/Goal.js');
const c = require('./../lib/constants.js');
const server = require('http').Server(app);
const io = require('socket.io')(server);

const rooms = [];

function getCurrentRoomOfSocket(socket) {
	const roomId = socket.rooms[Object.keys(socket.rooms)[1]]; // socket.room contains [<id of room 1>, <name of room 1>, ...]
	for (const room of rooms) {
		if (room.id === roomId) {
			return room;
		}
	}
	return null;
}

function removeRoom(roomId) {
	for (let i=0; i < rooms.length; i++) {
		if (rooms[i].id === roomId) {
			rooms.splice(i, 1);
		}
	}
}

io.on('connection', function(socket) {
	console.log(socket.id, "connected!");

	socket.on('join', function(roomName) {
		// Socket joins room
		socket.join(roomName);

		// Add a new room to the local list of Rooms if connected socket is the first player to join
		if (!getCurrentRoomOfSocket(socket)) {
			const newRoom = new Room(roomName);
			
			// Initialize walls
			const upperLeftWall  = new Wall(0, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
				  lowerLeftWall  = new Wall(0, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT),
				  upperRightWall = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
				  lowerRightWall = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT);
			const walls = [upperLeftWall, lowerLeftWall, upperRightWall, lowerRightWall];
			newRoom.setWalls(walls);
			
			// Add room to list of rooms
			rooms.push(newRoom);
		}
	});

	socket.on('new player', function() {
		const currentRoom = getCurrentRoomOfSocket(socket);
		if (!currentRoom) {
			console.log("error: cannot find currentroom");
		}

		const playerNo = Object.keys(currentRoom.playerPaddles).length + 1;
		let xPos, yPos;
		let width, height;
		switch (playerNo) {
			case 1: // left
				xPos = c.WALL_WIDTH - c.PADDLE_SHORT_LENGTH;
				yPos = c.WALL_HEIGHT;
				width = c.PADDLE_SHORT_LENGTH;
				height = c.PADDLE_LONG_LENGTH;
				break;
			case 2: // right
				xPos = c.WALL_WIDTH + c.GOAL_POST_LENGTH;
				yPos = c.WALL_HEIGHT;
				width = c.PADDLE_SHORT_LENGTH;
				height = c.PADDLE_LONG_LENGTH;
				break;
			case 3: // up
				xPos = c.WALL_WIDTH;
				yPos = c.WALL_HEIGHT - c.PADDLE_SHORT_LENGTH;
				width = c.PADDLE_LONG_LENGTH;
				height = c.PADDLE_SHORT_LENGTH;
				break;
			case 4: // down
				xPos = c.WALL_WIDTH;
				yPos = c.WALL_HEIGHT + c.GOAL_POST_LENGTH;
				width = c.PADDLE_LONG_LENGTH;
				height = c.PADDLE_SHORT_LENGTH;
				break;
		}
		currentRoom.playerPaddles[socket.id] = new Paddle(xPos, yPos, width, height, playerNo);
		currentRoom.playerGoals[socket.id] = new Goal(playerNo);
		currentRoom.playerScores[playerNo] = 0;

		if (Object.keys(currentRoom.playerPaddles).length === 4) {
			console.log("four players now. making ball.");
			currentRoom.ball = new Ball(c.WIDTH/2, c.HEIGHT/2, c.BALL_INITIAL_VX, c.BALL_INITIAL_VX, c.BALL_RADIUS);
			currentRoom.ball.alreadyPastGoal = false;
			socket.in(currentRoom.id).emit('game start');
		}
	});

	socket.on('disconnecting', function() {
		console.log(socket.id, "disconnecting!");
		const currentRoom = getCurrentRoomOfSocket(socket);
		if (currentRoom) {
			// Tell client to redisplay everyone's screen in this room
			socket.to(currentRoom.id).emit("restart");

			// Remove this room and force everyone to leave
			const socketsInRoom = io.sockets.adapter.rooms[currentRoom.id].sockets;
			let firstSocket;
			while (firstSocket = Object.keys(socketsInRoom)[0]) {
				console.log("client leaves room");
				io.sockets.connected[firstSocket].leave(currentRoom.id);
				removeRoom(currentRoom.id);
			}
		}
	});

	socket.on('player move', function(mousePos) {
		const currentRoom = getCurrentRoomOfSocket(socket);
		if (currentRoom) {
			const playerPaddle = currentRoom.playerPaddles[socket.id];
			if (playerPaddle) {
				let paddlePosX, paddlePosY;
				switch (playerPaddle.playerNo) {
					case 1: // vertical left
					case 2: // vertical right
						const minY = c.WALL_HEIGHT;
						const maxY = minY + c.GOAL_POST_LENGTH;
						if (mousePos.y < minY) {
							paddlePosY = minY;
						} else if ((mousePos.y + c.PADDLE_LONG_LENGTH) > maxY) {
							paddlePosY = maxY - c.PADDLE_LONG_LENGTH;
						} else {
							paddlePosY = mousePos.y;
						}
						playerPaddle.setY(paddlePosY);
						break;
					case 3: // horizontal up
					case 4: // horizontal down
						const minX = c.WALL_WIDTH;
						const maxX = minX + c.GOAL_POST_LENGTH;
						if (mousePos.x < minX) {
							paddlePosX = minX;
						} else if ((mousePos.x + c.PADDLE_LONG_LENGTH) > maxX) {
							paddlePosX = maxX - c.PADDLE_LONG_LENGTH;
						} else {
							paddlePosX = mousePos.x;
						}
						playerPaddle.setX(paddlePosX);
						break;
				}
			}
		}
	});

	socket.on('ball move', function(timeDifference) {
		const currentRoom = getCurrentRoomOfSocket(socket);

		if (currentRoom) {
			const ball = currentRoom.ball;
			if (ball) {
				const walls = currentRoom.walls;

				// Change ball direction if ball collides with wall
				for (const wall of walls) {
					const collides = ball.collidesWithWall(wall);
					if (collides) {
						console.log("ball collides with wall");
						if (ball.collidesAtCorner) {
							console.log("ball collides at corner");
							ball.vx *= -1;
							ball.vy *= -1;
						} else if (ball.isInBottomGoal() || ball.isInTopGoal()) {
							console.log("ball in bottom or top goal");
							ball.vx *= -1;
						} else if (ball.isInLeftGoal() || ball.isInRightGoal()) {
							console.log("ball in left or right goal");
							ball.vy *= -1;
						} else {
							console.log("ball collided but is not in any goal");
						}
						break;
					}
				}

				// Change ball direction if ball collides with paddle
				const playerPaddles = currentRoom.playerPaddles;
				for (const key in playerPaddles) {
					const paddle = playerPaddles[key];
					if (ball.collidesWithPaddle(paddle)) {
						if (!ball.currentlyCollidedWithPaddle) {
							console.log("ball collides with paddle for the first time", paddle.playerNo);
							switch (paddle.playerNo) {
								case 1:
								case 2:
									ball.vx *= -1;
									break;
								case 3:
								case 4:
									ball.vy *= -1;
							}
							ball.currentlyCollidedWithPaddle = true;
							ball.playerNoOfPaddleCollidedWith = paddle.playerNo;
						}
						break;
					} else {
						if (paddle.playerNo === ball.playerNoOfPaddleCollidedWith) {
							ball.currentlyCollidedWithPaddle = false;
						}
					}
				}

				// Increment score if ball passed goal
				const playerGoals = currentRoom.playerGoals;
				const playerScores = currentRoom.playerScores;
				for (const key in playerGoals) {
					const goal = playerGoals[key];
					if (ball.outOfBounds(goal)) {
						if (ball.alreadyPastGoal) {
							// Reset the ball
							ball.x = c.WIDTH/2;
							ball.y = c.HEIGHT/2;
							const angle = (360 * Math.random()) * (Math.PI/180);
							ball.vx = c.BALL_INITIAL_VX * Math.sin(angle);
							ball.vy = c.BALL_INITIAL_VY * Math.cos(angle);
							ball.alreadyPastGoal = false;
						} else {
							if (ball.playerNoOfPaddleCollidedWith) {
								playerScores[ball.playerNoOfPaddleCollidedWith]++;
								ball.playerNoOfPaddleCollidedWith = undefined;
							}
							ball.alreadyPastGoal = true;
						}
					}
				}

				ball.x += ball.vx;
				ball.y += ball.vy;
			}
		}
	/*
		if (ball) {
			let ballIsTouchingAWall = false;
			let wallBallIsTouching;
			let sideBallIsTouching;
			for (let i=0; i < walls.length; i++) {
				const wall = walls[i];
				const touchInfo = wall.touchedBy(ball);
				console.log(touchInfo);
				if (touchInfo.ballTouchesWall === true) {
					ballIsTouchingAWall = true;
					wallBallIsTouching = i;
					sideBallIsTouching = touchInfo.sideOfWallBallIsTouches;
					break;
				}
			}

			if (ballIsTouchingAWall) {
				console.log("ball is touching a wall", true);
				if (ball.touched === false) {
					console.log("first touch");
					// [upperLeftWall, lowerLeftWall, upperRightWall, lowerRightWall];
					if ((wallBallIsTouching === 1 && sideBallIsTouching === "right") ||
						(wallBallIsTouching === 3 && sideBallIsTouching === "left")) {
							console.log("within bottom goal");
							ball.vx *= -1;
					} else if ((wallBallIsTouching === 0 && sideBallIsTouching === "right") ||
							   (wallBallIsTouching === 2 && sideBallIsTouching === "left")) {
								console.log("within top goal");
							ball.vx *= -1;
					} else if ((wallBallIsTouching === 0 && sideBallIsTouching === "bottom") ||
							   (wallBallIsTouching === 1 && sideBallIsTouching === "top")) {
								console.log("within left goal");
							ball.vy *= -1;
					} else if ((wallBallIsTouching === 2 && sideBallIsTouching === "bottom") ||
							   (wallBallIsTouching === 3 && sideBallIsTouching === "top")) {
								console.log("within right goal");
							ball.vy *= -1;
					}
					// if (withinBottomGoal() || withinTopGoal()) {
					// 	console.log("ball is in top or bottom goal");
					// 	ball.vx *= -1;
					// } else if (withinLeftGoal() || withinRightGoal()) {
					// 	console.log("ball is in left or right goal");
					// 	ball.vy *= -1;
					// }

				}
				ball.touched = true;
			} else {
				console.log("ball is touching a wall", false);
				ball.touched = false;
			}
			// console.log("ball is touching wall?:", ballIsTouchingAWall);
			// touches sides of canvas
			// touches paddle
			ball.x += (ball.vx);
			ball.y += (ball.vy);
		}
	*/
	});

	setInterval(function() {
		const currentRoom = getCurrentRoomOfSocket(socket);
		if (currentRoom) {
			const playerPaddles = currentRoom.playerPaddles;
			const ball = currentRoom.ball;
			const walls = currentRoom.walls;
			const playerScores = currentRoom.playerScores;
			io.sockets.in(currentRoom.id).emit('state', {playerPaddles, ball, walls, playerScores});
		}
	}, 1000 / 60);
});

const port = process.env.PORT || 3000;
const DIST_DIR = path.join(__dirname, '../../dist');
const HTML_FILE = path.join(DIST_DIR, 'index.html');

app.use(express.static(DIST_DIR));

app.get('/', (req, res) => {
	res.sendFile(HTML_FILE);
});

server.listen(port, function() {
	console.log('App listening on port: ' + port);
});
