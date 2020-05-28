class Paddle {
    constructor(x, y, width, height, playerNo) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerNo = playerNo;
    }

    setX(x) {
        this.x = x;
    }

    setY(y) {
        this.y = y;
    }

    getX() {
        return this.x;
    }

    getY() {
        return this.y;
    }

    getWidth() {
        return this.width;
    }

    getHeight() {
        return this.height;
    }

    render(ctx) {
        ctx.fillStyle = 'white';
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }
}

module.exports = Paddle;