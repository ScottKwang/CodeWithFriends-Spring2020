class Wall {
    constructor(x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    render(ctx) {
        ctx.fillStyle = "blue";
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

    ballIsBelow(ball) {
        return (this.y + this.height) < (ball.y - ball.radius);
    }

    ballIsAbove(ball) {
        return this.y > (ball.y + ball.radius);
    }

    ballIsRight(ball) {
        return (this.x + this.width) < (ball.x - ball.radius);
    }
    
    ballIsLeft(ball) {
        return this.x > (ball.x + ball.radius);
    }

    touchedBy(ball) {
        // const ballIsOutsideOfWall = this.ballIsLeft(ball) || this.ballIsRight(ball) || this.ballIsAbove(ball) || this.ballIsBelow(ball);
        // return !ballIsOutsideOfWall;

        const ballisTouchingLeftSide = !this.ballIsLeft(ball),
            ballIsTouchingTopSide = !this.ballIsAbove(ball),
            ballIsTouchingRightSide = !this.ballIsRight(ball),
            ballIsTouchingBotSide = !this.ballIsBelow(ball);
    
        let ballTouchesWall = false;
        let sideOfWallBallTouches = "";
        if (ballisTouchingLeftSide) {
            ballTouchesWall = true;
            sideOfWallBallTouches = "left";
        } else if (ballIsTouchingRightSide) {
            ballTouchesWall = true;
            sideOfWallBallTouches = "right";
        } else if (ballIsTouchingTopSide) {
            ballTouchesWall = true;
            sideOfWallBallTouches = "top";
        } else if (ballIsTouchingBotSide) {
            ballTouchesWall = true;
            sideOfWallBallTouches = "bottom";
        } else {
            ballTouchesWall = false;
        }

        return {ballTouchesWall, sideOfWallBallTouches};
    }
}

module.exports = Wall;