class Paddle {
    constructor(x, y, width, height, playerNo) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerNo = playerNo;
    }

    render(ctx) {
        ctx.fillStyle = 'white';
        ctx.fillRect(this.x, this.y, this.width, this.height);
        /*
                    ctx.save();
                    const playerPaddle = playerPaddles[id];
                    console.log(playerPaddle);

                    let angle;
                    switch (playerPaddle.playerNo) {
                        case 1:
                        case 2:
                            // angle = 45;
                            break;
                        case 3:
                        case 4:
                            angle = -90;
                            break;
                    }
                    rotateBy(ctx, playerPaddle.x, playerPaddle.y, playerPaddle.width, playerPaddle.height, angle);
                    render.call(playerPaddle, ctx);
                    ctx.restore();
                */
    }

    setX(x) {
        this.x = x;
    }

    setY(y) {
        this.y = y;
    }

    getX(x) {
        return this.x;
    }

    getY(y) {
        return this.y;
    }

    getWidth() {
        return this.width;
    }

    getHeight() {
        return this.height;
    }
}

module.exports = Paddle;