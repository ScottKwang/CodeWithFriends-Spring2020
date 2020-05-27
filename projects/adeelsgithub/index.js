const canvas = document.querySelector('#myCanvas');
const clearButton = document.querySelector(".clearBtn");
const ctx = canvas.getContext('2d');
let moveNumber = 10;
let width = canvas.width;
let height = canvas.height;
let hsl = 0;
// random position for dot to appear
let x = Math.floor(Math.random() * width);
let y = Math.floor(Math.random() * height);

ctx.lineJoin = "round";
ctx.lineCap = "round";
ctx.lineWidth = moveNumber;

ctx.beginPath();
ctx.moveTo(x,y);
ctx.lineTo(x,y);
ctx.stroke();
ctx.strokeStyle = `hsl(${hsl},100%,50%)`;

function draw(key){
    // changing stroke color
    hsl+=5;
    ctx.strokeStyle = `hsl(${hsl},100%,50%)`;

    ctx.beginPath();
    ctx.moveTo(x,y);
    switch(key){
        case "ArrowUp":
            y -= moveNumber;
            ctx.lineTo(x,y);
            break;
        case "ArrowDown":
            y += moveNumber;
            ctx.lineTo(x,y);
            break; 
        case "ArrowRight":
            x += moveNumber;
            ctx.lineTo(x,y);
            break;            
        case "ArrowLeft":
            x -= moveNumber;
            ctx.lineTo(x,y);
            break;
    }
    ctx.lineTo(x,y);

    ctx.stroke();
}

function handleKey(e){
    if(e.key.includes("Arrow")){
        e.preventDefault();
        draw(e.key);
        console.log("handled");



    }
}

function clearCanvas(){
    ctx.clearRect(0,0,width,height);
}

window.addEventListener("keydown",handleKey);
clearButton.addEventListener("click",clearCanvas)