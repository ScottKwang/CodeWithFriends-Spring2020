class Room {
    constructor(id) {
        this.id = id;
        this.playerPaddles = {};
        this.walls = [];
        this.ball = {};
    }

    setWalls(walls) {
        this.walls = walls;
    }
}

module.exports = Room;