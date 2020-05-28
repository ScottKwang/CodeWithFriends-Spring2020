const express = require('express');
const path = require('path');
const app = express();

const server = require('http').Server(app);
const io = require('socket.io')(server);

const Ball = require('./../lib/Ball.js');
const Wall = require('./../lib/Wall.js');
const Room = require('./../lib/Room.js');
const Player = require('./../lib/Player.js');
const c = require('./../lib/constants.js');

const rooms = [];

function getRoomWithId(roomId) {
	for (const room of rooms) {
		if (room.id === roomId) {
			return room;
		}
	}
	return null;
}

function getCurrentRoomOfSocket(socket) {
	const roomId = socket.rooms[Object.keys(socket.rooms)[1]]; // socket.room contains [<id of room 1>, <name of room 1>, ...]
	return getRoomWithId(roomId);
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

	socket.on('join pending', function(roomName) {
		const roomToJoin = getRoomWithId(roomName);
		
		if (roomToJoin && roomToJoin.isFull()) { // Room exists
			if (roomToJoin.isFull()) {
				console.log('someone tried to join full room');
				socket.emit('join failure');
			} else {
				socket.emit('join success');
				socket.join(roomName);
			}
		} else { // Room does not exist
			socket.emit('join success');
			socket.join(roomName);

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

	/*
		socket.on('join', function(roomName) {
		const roomToJoin = getRoomWithId(roomName);
		if (roomToJoin && roomToJoin.isFull()) {
			socket.emit('room is full');
			return;
		}

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
	*/
	socket.on('new player', function() {
		const currentRoom = getCurrentRoomOfSocket(socket);

		const playerNo = currentRoom.getNumberOfPlayers() + 1;
		const newPlayer = new Player(playerNo);

		currentRoom.players[socket.id] = newPlayer;

		if (currentRoom.getNumberOfPlayers() === 4) {
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
			const player = currentRoom.players[socket.id];
			const paddle = player.getPaddle();
			if (paddle) {
				let paddlePosX, paddlePosY;
				switch (paddle.playerNo) {
					case 1: // left
					case 2: // right
						const minY = c.WALL_HEIGHT;
						const maxY = minY + c.GOAL_POST_LENGTH;
						if (mousePos.y < minY) {
							paddlePosY = minY;
						} else if ((mousePos.y + c.PADDLE_LONG_LENGTH) > maxY) {
							paddlePosY = maxY - c.PADDLE_LONG_LENGTH;
						} else {
							paddlePosY = mousePos.y;
						}
						paddle.setY(paddlePosY);
						break;
					case 3: // up
					case 4: // down
						const minX = c.WALL_WIDTH;
						const maxX = minX + c.GOAL_POST_LENGTH;
						if (mousePos.x < minX) {
							paddlePosX = minX;
						} else if ((mousePos.x + c.PADDLE_LONG_LENGTH) > maxX) {
							paddlePosX = maxX - c.PADDLE_LONG_LENGTH;
						} else {
							paddlePosX = mousePos.x;
						}
						paddle.setX(paddlePosX);
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
				const players = currentRoom.players;
				for (const key in players) {
					if (players.hasOwnProperty(key)) {
						const paddle = players[key].getPaddle();
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
				}

				// Increment score if ball passed goal
				for (const key in players) {
					if (players.hasOwnProperty(key)) {
						const goal = players[key].getGoal();
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
									const playerToIncrScore = currentRoom.getPlayerWithPlayerNo(ball.playerNoOfPaddleCollidedWith);
									playerToIncrScore.incrScore();
									ball.playerNoOfPaddleCollidedWith = undefined;
								}
								ball.alreadyPastGoal = true;
							}
						}
					}
				}

				ball.x += ball.vx;
				ball.y += ball.vy;
			}
		}
	});

	setInterval(function() {
		const currentRoom = getCurrentRoomOfSocket(socket);
		if (currentRoom) {
			const ball = currentRoom.ball;
			const walls = currentRoom.walls;

			const players = currentRoom.players;
			io.sockets.in(currentRoom.id).emit('state', {ball, walls, players});
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
