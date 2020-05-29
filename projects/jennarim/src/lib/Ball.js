const c = require('./constants.js');

const PADDING = 10; // used to to extend the hitbox of the goal

class Ball {
    constructor(x, y, vx, vy, radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.color = "white";
    }

    collidesWithWall(wall) {    
        const distX = Math.abs(this.x - (wall.x + wall.width/2));
        const distY = Math.abs(this.y - (wall.y + wall.height/2));
        if (distX > (wall.width/2 + this.radius)) {
            this.collidesAtCorner = false;
            return false;
        } else if (distY > (wall.height/2 + this.radius)) {
            this.collidesAtCorner = false;
            return false;
        } else if (distX <= (wall.width/2)) {
            this.collidesAtCorner = false;
            return true;
        } else if (distY <= (wall.height/2)) {
            this.collidesAtCorner = false;
            return true;
        }
        const dx = distX - wall.width/2;
        const dy = distY - wall.height/2;
        if (dx*dx+dy*dy<=(this.radius*this.radius)) {
            this.collidesAtCorner = true;
            return true;
        } else {
            this.collidesAtCorner = false;
            return false;
        }
    }

    outOfBounds(goal) {
        switch(goal.playerNo) {
            case 1:
                return this.x < 0;
            case 2:
                return this.x > c.WIDTH;
            case 3:
                return this.y < 0;
            case 4:
                return this.y > c.HEIGHT;
        }
        return false;
    }

    isInBottomGoal() {
        return (this.x > (c.WALL_WIDTH - PADDING) && this.x < (c.WIDTH-c.WALL_WIDTH+PADDING) && this.y >= (c.HEIGHT - c.WALL_HEIGHT) && this.y <= c.HEIGHT);
    }
    
    isInTopGoal() {
        return (this.x > (c.WALL_WIDTH - PADDING) && this.x < (c.WIDTH-c.WALL_WIDTH+PADDING) && this.y > 0 && this.y < c.WALL_WIDTH);
    }
    
    isInLeftGoal() {
        return (this.x > 0 && this.x <= c.WALL_WIDTH && this.y > (c.WALL_HEIGHT - PADDING) && this.y < (c.HEIGHT - c.WALL_HEIGHT + PADDING));
    }
    
    isInRightGoal() {
        return (this.x > (c.WIDTH - c.WALL_WIDTH) && this.x < c.WIDTH && this.y > (c.WALL_HEIGHT - PADDING) && this.y < (c.HEIGHT - c.WALL_HEIGHT + PADDING));
    }

    collidesWithPaddle(paddle) {
        const distX = Math.abs(this.x - (paddle.x + paddle.width/2));
        const distY = Math.abs(this.y - (paddle.y + paddle.height/2));
        if (distX > (paddle.width/2 + this.radius)) {
            return false;
        } else if (distY > (paddle.height/2 + this.radius)) {
            return false;
        } else if (distX <= (paddle.width/2)) {
            return true;
        } else if (distY <= (paddle.height/2)) {
            return true;
        }
        const dx = distX - paddle.width/2;
        const dy = distY - paddle.height/2;
        return (dx*dx+dy*dy<=(this.radius*this.radius));
    }

    reset() {
        this.x = c.WIDTH/2;
        this.y = c.HEIGHT/2;
        this.color = "white";
        const angle = (360 * Math.random()) * (Math.PI/180);
        this.vx = c.BALL_INITIAL_VX * Math.sin(angle);
        this.vy = c.BALL_INITIAL_VY * Math.cos(angle);
        this.alreadyPastGoal = false;
    }

    render(ctx) {
        ctx.fillStyle = this.color;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
        ctx.fill();
        ctx.closePath();
    }
}

module.exports = Ball;