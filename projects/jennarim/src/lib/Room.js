class Room {
    constructor(id) {
        this.id = id;
        this.walls = [];
        this.ball = {};
        this.players = {};
    }

    setWalls(walls) {
        this.walls = walls;
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
}

module.exports = Room;