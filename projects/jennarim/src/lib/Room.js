class Room {
    constructor(id) {
        this.id = id;
        this.playerPaddles = {};
        this.walls = [];
        this.ball = {};
        this.playerGoals = {};
        this.playerScores = {};
    }

    setWalls(walls) {
        this.walls = walls;
    }
}

module.exports = Room;