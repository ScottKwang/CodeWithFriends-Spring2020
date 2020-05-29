class Wall {
    constructor(x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    render(ctx) {
        ctx.fillStyle = "#333";
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }
}

module.exports = Wall;