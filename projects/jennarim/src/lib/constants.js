// Note: Width and height of canvas are declared outside 
// because the length of the wall is dependent on 
// the length of the canvas
const WIDTH = 600;
const HEIGHT = 600;

module.exports = Object.freeze({
    WIDTH: WIDTH,
    HEIGHT: HEIGHT,
    BORDER_LENGTH: 5,
    BG_COLOR: "darkgray",

    WALL_WIDTH: WIDTH/4,
    WALL_HEIGHT: HEIGHT/4,
    
    PADDLE_SHORT_LENGTH: 10,
    PADDLE_LONG_LENGTH: 100,

    GOAL_POST_LENGTH: 300,

    BALL_INITIAL_VX: 6,
    BALL_INITIAL_VY: 6,
    BALL_RADIUS: 6,

    START_TIME_IN_SEC: 20
});