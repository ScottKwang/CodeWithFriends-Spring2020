const Paddle = require('./../lib/Paddle.js');
const Goal = require('./../lib/Goal.js');
const Score = require('./../lib/Score.js');
const c = require('./../lib/constants.js');

function paddleArgsBasedOn(playerNo) {
    let xPos, yPos;
    let width, height;
    let color;
    switch (playerNo) {
        case 1: // left
            xPos = c.WALL_WIDTH - c.PADDLE_SHORT_LENGTH;
            yPos = c.WALL_HEIGHT;
            width = c.PADDLE_SHORT_LENGTH;
            height = c.PADDLE_LONG_LENGTH;
            color = "crimson";
            break;
        case 2: // right
            xPos = c.WALL_WIDTH + c.GOAL_POST_LENGTH;
            yPos = c.WALL_HEIGHT;
            width = c.PADDLE_SHORT_LENGTH;
            height = c.PADDLE_LONG_LENGTH;
            color = "moccasin";
            break;
        case 3: // up
            xPos = c.WALL_WIDTH;
            yPos = c.WALL_HEIGHT - c.PADDLE_SHORT_LENGTH;
            width = c.PADDLE_LONG_LENGTH;
            height = c.PADDLE_SHORT_LENGTH;
            color = "lightgreen";
            break;
        case 4: // down
            xPos = c.WALL_WIDTH;
            yPos = c.WALL_HEIGHT + c.GOAL_POST_LENGTH;
            width = c.PADDLE_LONG_LENGTH;
            height = c.PADDLE_SHORT_LENGTH;
            color = "cornflowerblue";
            break;
    }
    return [xPos, yPos, width, height, playerNo, color];
}

class Player {
    constructor(playerNo) {
        this.playerNo = playerNo;
        this.paddle = new Paddle(...paddleArgsBasedOn(playerNo));
        this.goal = new Goal(playerNo);
        this.score = new Score(0, playerNo);
    }

    getPlayerNo() {
        return this.playerNo;
    }

    getPaddle() {
        return this.paddle;
    }

    getGoal() {
        return this.goal;
    }

    getScore() {
        return this.score;
    }

    incrScore() {
        this.score.incr();
    }
}

module.exports = Player;