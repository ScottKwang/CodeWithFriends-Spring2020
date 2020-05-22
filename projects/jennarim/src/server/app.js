const express = require('express');
const path = require('path');
const app = express();
const Paddle = require('./../lib/Paddle.js');
const Ball = require('./../lib/Ball.js');
const Wall = require('./../lib/Wall.js')
const c = require('./../lib/constants.js');
const server = require('http').Server(app);
const io = require('socket.io')(server);

const MAX_NUM_OF_PLAYERS = 4;

const playerPaddles = {};
let walls;
let ball;

const PADDING = 10;

function withinBottomGoal() {
	// const [lowerLeftWall, lowerRightWall] = [walls[1], walls[3]];
	// const b = (!lowerLeftWall.ballIsAbove(ball) && lowerLeftWall.ballIsRight() && 
	// 		!lowerRightWall.ballIsAbove(ball) && lowerRightWall.ballIsLeft());
	// console.log("within bottom", b);
	// const b = lowerLeftWall.touchedBy(ball) || 
	// return b;
	return (ball.x > (c.WALL_WIDTH - PADDING) && ball.x < (c.WIDTH-c.WALL_WIDTH+PADDING) && ball.y >= (c.HEIGHT - c.WALL_HEIGHT) && ball.y <= c.HEIGHT);
	// 0 < x < width of canvas
	// height of canvas - height of wall < y < height of canvas
}

function withinTopGoal() {
	// const [upperLeftWall, upperRightWall] = [walls[0], walls[2]];
	// const b = (!upperLeftWall.ballIsBelow(ball) && upperLeftWall.ballIsRight() &&
	// 		!upperRightWall.ballIsBelow(ball) && upperRightWall.ballIsLeft());
	// console.log("within top", b);
	// return b;
	// 0 < x < width of canvas
	// 0 < y < width of wall
	return (ball.x > (c.WALL_WIDTH - PADDING) && ball.x < (c.WIDTH-c.WALL_WIDTH+PADDING) && ball.y > 0 && ball.y < c.WALL_WIDTH);
}

function withinLeftGoal() {
	// const [upperLeftWall, lowerLeftWall] = [walls[0], walls[1]];
	// return (!upperLeftWall.ballIsRight(ball) && upperLeftWall.ballIsBelow() && 
	// !lowerLeftWall.ballIsRight(ball));
	// const upperLeftWall = walls[0];
	// return upperLeftWall.ballIsRight();
	return (ball.x > 0 && ball.x <= c.WALL_WIDTH && ball.y > (c.WALL_HEIGHT - PADDING) && ball.y < (c.HEIGHT - c.WALL_HEIGHT + PADDING));
}

function withinRightGoal() {
	// const [upperRightWall, lowerRightWall] = [walls[2], walls[3]];
	// return (!upperRightWall.ballIsLeft(ball) && !lowerRightWall.ballIsLeft(ball));
	return (ball.x > (c.WIDTH - c.WALL_WIDTH) && ball.x < c.WIDTH && ball.y > (c.WALL_HEIGHT - PADDING) && ball.y < (c.HEIGHT - c.WALL_HEIGHT + PADDING));
}

function getDistance(x1, x2, y1, y2) {
	const xDistance = x1-x2;
	const yDistance = y1-y2;
	return Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
}

function ballCollidesWithWall(wall) {
	const res = {collides: false, atCorner: false};

	const distX = Math.abs(ball.x - (wall.x + wall.width/2));
	const distY = Math.abs(ball.y - (wall.y + wall.height/2));
	if (distX > (wall.width/2 + ball.radius)) {
		res.collides = false;
	} else if (distY > (wall.height/2 + ball.radius)) {
		res.collides = false;
	} else if (distX <= (wall.width/2)) {
		res.collides = true;
	} else if (distY <= (wall.height/2)) {
		res.collides = true;
	}
	const dx = distX - wall.width/2;
	const dy = distY - wall.height/2;
	if (dx*dx+dy*dy<=(ball.radius*ball.radius)) {
		res.collides = true;
		res.atCorner = true;
	}
	return res;
}

function ballCollidesWithPaddle(paddle) {
	const distX = Math.abs(ball.x - (paddle.x + paddle.width/2));
	const distY = Math.abs(ball.y - (paddle.y + paddle.height/2));
	if (distX > (paddle.width/2 + ball.radius)) {
		return false;
	} else if (distY > (paddle.height/2 + ball.radius)) {
		return false;
	} else if (distX <= (paddle.width/2)) {
		return true;
	} else if (distY <= (paddle.height/2)) {
		return true;
	}
	const dx = distX - paddle.width/2;
	const dy = distY - paddle.height/2;
	return (dx*dx+dy*dy<=(ball.radius*ball.radius));
}

io.on('connection', function(socket) {
	console.log(socket.id, " connected!");

	const upperLeftWall   = new Wall(0, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
		  lowerLeftWall  = new Wall(0, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT),
		  upperRightWall  = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
		  lowerRightWall = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT);
	walls = [upperLeftWall, lowerLeftWall, upperRightWall, lowerRightWall];

	socket.on('new player', function() {
		const playerNo = Object.keys(playerPaddles).length + 1;
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
		playerPaddles[socket.id] = new Paddle(xPos, yPos, width, height, playerNo);

		if (Object.keys(playerPaddles).length === MAX_NUM_OF_PLAYERS) {
			console.log("four players now. making ball.", ball);
			ball = new Ball(c.WIDTH/2, c.HEIGHT/2, 4, 5, 6);
			// ball.alreadyTouched = false;
			ball.touchedPaddle = false;
			socket.emit('game start');
			socket.on('restart', function() {
				socket.emit('game start');
			});
		}
	});

	socket.on('disconnect', function() {
		console.log(socket.id, "disconnected!");

		delete playerPaddles[socket.id];
		let index = 1;
		// Update playerNo
		for (const playerPaddle in playerPaddles) {
			playerPaddle.playerNo = index++;
		}
	});

	socket.on('player move', function(mousePos) {
		// console.log(socket.id, "moved!");
		// console.log(mousePos);
		const playerPaddle = playerPaddles[socket.id];
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
	});

	socket.on('ball move', function(timeDifference) {
		// ball = new Ball(data.mouseX, data.mouseY, 0, 0, 6);
		if (ball) {
			for (const wall of walls) {
				const res = ballCollidesWithWall(wall);
				const collides = res.collides;
				if (collides) {
					console.log("ball collides with wall");
					const atCorner = res.atCorner;
					if (atCorner) {
						console.log("ball collides at corner");
						ball.vx *= -1;
						ball.vy *= -1;
					} else if (withinBottomGoal() || withinTopGoal()) {
						console.log("ball in bottom or top goal");
						ball.vx *= -1;
					} else if (withinLeftGoal() || withinRightGoal()) {
						console.log("ball in left or right goal");
						ball.vy *= -1;
					} else {
						console.log("ball collided but is not in any goal");
					}
					break;
				}
			}
			for (const key in playerPaddles) {
				const paddle = playerPaddles[key];
				if (ballCollidesWithPaddle(paddle)) {
					if (ball.touchedPaddle === false) {
						console.log("ball collides with paddle", paddle.playerNo);
						// const signOfOriginalVx = (ball.vx > 0? 1:-1);
						// const signOfOriginalVy = (ball.vy > 0? 1:-1);
						// ball.vx = (Math.floor(Math.random() * 9) + 1) * signOfOriginalVx;
						// ball.vy = (Math.floor(Math.random() * 9) + 1) * signOfOriginalVy;
						switch (paddle.playerNo) {
							case 1:
							case 2:
								ball.vx *= -1;
								break;
							case 3:
							case 4:
								ball.vy *= -1;
						}
						ball.touchedPaddle = true;
						ball.paddleTouched = paddle.playerNo;
					} else {
						console.log("already touched");
					}
					break;
				} else {
					if (paddle.playerNo === ball.paddleTouched) {
						ball.touchedPaddle = false;
					}
				}
				// if ball collides with paddle
					// vx = 1 - 9
					// vy = 1 - 9
					// if top bot, vy *= -1
					// if left right, vx *= -1
			}
			/*
				if (ballTouchingWall) {
					console.log("ball collides with wall");
					if (ball.alreadyTouched === false) {
						if (withinBottomGoal() || withinTopGoal()) {
							console.log("ball in bottom or top goal");
							ball.vx *= -1;
						} else if (withinLeftGoal() || withinRightGoal()) {
							console.log("ball in left or right goal");
							ball.vy *= -1;
						}
						ball.alreadyTouched = true;
					}
				} else {
					ball.alreadyTouched = false;
				}
			*/
			ball.x += ball.vx;
			ball.y += ball.vy;
			// if ball collides with wall 
				// if it's the first time
					// in bottom or top goals
						// ball.vx *= -1;
					// in left or right goals
						// ball.vy *= -1;
					// collision with wall = true
			// else
				// collision with wall = false
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
});

setInterval(function() {
	io.sockets.emit('state', {playerPaddles, ball, walls});
}, 1000 / 60);

const port = process.env.PORT || 3000;
const DIST_DIR = path.join(__dirname, '../../dist');
// const DIST_PUBLIC_DIR = path.join(__dirname, DIST_DIR, 'src/client/public');
const HTML_FILE = path.join(DIST_DIR, 'index.html');

app.use(express.static(DIST_DIR));

app.get('/', (req, res) => {
	res.sendFile(HTML_FILE);
});

server.listen(3000, function() {
	console.log('App listening on port: ' + port);
});

// app.listen(port, function () {
//  console.log('App listening on port: ' + port);
// });

// var createError = require('http-errors');
// var express = require('express');
// var path = require('path');
// var cookieParser = require('cookie-parser');
// var logger = require('morgan');

// var indexRouter = require('./routes/index');
// var usersRouter = require('./routes/users');

// var app = express();

// // view engine setup
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'jade');

// app.use(logger('dev'));
// app.use(express.json());
// app.use(express.urlencoded({ extended: false }));
// app.use(cookieParser());
// app.use(express.static(path.join(__dirname, 'public')));

// app.use('/', indexRouter);
// app.use('/users', usersRouter);

// // catch 404 and forward to error handler
// app.use(function(req, res, next) {
//   next(createError(404));
// });

// // error handler
// app.use(function(err, req, res, next) {
//   // set locals, only providing error in development
//   res.locals.message = err.message;
//   res.locals.error = req.app.get('env') === 'development' ? err : {};

//   // render the error page
//   res.status(err.status || 500);
//   res.render('error');
// });

// module.exports = app;
