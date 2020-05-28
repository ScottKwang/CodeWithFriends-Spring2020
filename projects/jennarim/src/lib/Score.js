const c = require('./../lib/constants.js');

class Score {
    constructor(value, playerNo) {
        this.playerNo = playerNo;
        this.value = value;
    }

    getValue() {
        return this.value;
    }

    incr() {
        this.value++;
    }

    render(ctx) {
        ctx.font = '30px serif';
        ctx.fillStyle = 'white';
        
        switch (this.playerNo) {
            case 1:
                ctx.fillText(this.value, 20, c.HEIGHT/2);
                break;
            case 2:
                ctx.fillText(this.value, c.WIDTH - 40, c.HEIGHT/2);
                break;
            case 3:
                ctx.fillText(this.value, c.WIDTH/2, 40);
                break;
            case 4:
                ctx.fillText(this.value, c.WIDTH/2, c.HEIGHT - 40);
                break;
        }
    }
}

module.exports = Score;