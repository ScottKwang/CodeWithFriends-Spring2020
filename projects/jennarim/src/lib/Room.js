const c = require('./constants.js');
const Ball = require('./Ball.js');
const Wall = require('./Wall.js');

class Room {
    constructor(id) {
        this.id = id;
        this.players = {};
        this.gameActive = false;
        this.currentTime = c.START_TIME_IN_SEC;

        // Initialize ball
        this.ball = new Ball(c.WIDTH/2, c.HEIGHT/2, c.BALL_INITIAL_VX, c.BALL_INITIAL_VX, c.BALL_RADIUS);
        this.ball.alreadyPastGoal = false; // flag that indicates whether ball passed a goal
        const angle = (360 * Math.random()) * (Math.PI/180);
        this.ball.vx = c.BALL_INITIAL_VX * Math.sin(angle);
        this.ball.vy = c.BALL_INITIAL_VY * Math.cos(angle);

        // Initialize walls
        const upperLeftWall  = new Wall(0, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
              lowerLeftWall  = new Wall(0, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT),
              upperRightWall = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, 0, c.WALL_WIDTH, c.WALL_HEIGHT),
              lowerRightWall = new Wall(c.WALL_WIDTH + c.GOAL_POST_LENGTH, c.WALL_HEIGHT + c.GOAL_POST_LENGTH, c.WALL_WIDTH, c.WALL_HEIGHT);
		this.walls = [upperLeftWall, lowerLeftWall, upperRightWall, lowerRightWall];
    }

    getNumberOfPlayers() {
        return Object.keys(this.players).length;
    }

    getPaddleOfPlayer(socketID) {
        for (const key in this.players) {
            if (this.players.hasOwnProperty(key)) {
                if (key === socketID) {
                    const player = this.players[key];
                    return player.getPaddle();
                }
            }
        }
        return null;
    }

    getPlayerWithPlayerNo(playerNo) {
        for (const key in this.players) {
            if (this.players.hasOwnProperty(key)) {
                const player = this.players[key];
                if (playerNo === player.playerNo) {
                    return player;
                }
            }
        }
        return null;
    }

    isFull() {
        return this.getNumberOfPlayers() === 4;
    }

    startGame() {
        this.gameActive = true;
        this.startTime = new Date();
    }

    stopGame() {
        this.gameActive = false;
    }

    updateTime() {
        this.currentTime = parseInt(c.START_TIME_IN_SEC - ((new Date() - this.startTime)/1000));
    }

    getWinners() {
        let maxScore = 0;
        let listofWinners = []; // objects with playerNo : playerColor
        for (const key in this.players) {
            if (this.players.hasOwnProperty(key)) {
                const player = this.players[key];
                const playerScore = player.getScoreValue();
                if (playerScore > maxScore) {
                    maxScore = playerScore;
                    listofWinners = [];
                    listofWinners.push({playerNo: player.playerNo, color: player.paddle.color});
                } else if (playerScore === maxScore) {
                    listofWinners.push({playerNo: player.playerNo, color: player.paddle.color});
                }
            }
        }
        return listofWinners;
    }
}

module.exports = Room;