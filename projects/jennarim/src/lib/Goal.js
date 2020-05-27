const c = require('./constants.js');

class Goal {
    constructor(playerNo) {
        this.playerNo = playerNo;
    }

    ballScoresAtGoal(ball) {
        switch (this.playerNo) {
            case 1:
                return ball.x < 0;
            case 2:
                return ball.x > c.WIDTH;
            case 3:
                return ball.y < 0;
            case 4:
                return ball.y > c.HEIGHT;
        }
        return false;
    }
}

module.exports = Goal;