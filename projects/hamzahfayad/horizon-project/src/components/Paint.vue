<template>
  <div id="canvas">
    <button id="button-save">Save</button>
    <button id="button-clear" @click="clear">clear</button>

    <button id="rec" @click="drawRect">Rectangle</button>
    <button @click="addWidthRect">Add width</button>
    <button @click="subWidthRect">Sub Width</button>
    <button @click="addHeightRect">Add Height</button>
    <button @click="subHeightRect">Sub Height</button>
    <br />
    <div class="circles">
      <button id="circle" @click="drawCircle">Circle</button>
      <button @click="addWidthCircle">Bigger Circle</button>
      <button @click="subWidthCircle">Smaller Circle</button>
    </div>
    <br />
    <canvas id="c"></canvas>
    <br />
  </div>
</template>

<script>
/*var c = document.getElementById("c");
var ctx = c.getContext("2d");
ctx.beginPath();
ctx.rect(20, 20, 150, 100);
ctx.stroke();*/
export default {
  data() {
    return {
      vueCanvas: null,
      rectWidth: 20,
      rectHeight: 20,
      circleRadius: 20
    };
  },
  mounted() {
    var c = document.getElementById("c");
    var ctx = c.getContext("2d");
    this.vueCanvas = ctx;
  },
  methods: {
    drawRect() {
      this.vueCanvas.clearRect(0, 0, this.rectWidth, this.rectHeight);
      this.vueCanvas.beginPath();
      this.vueCanvas.strokeStyle = "red";
      this.vueCanvas.rect(20, 20, this.rectWidth, this.rectHeight);
      this.vueCanvas.stroke();
    },
    addWidthRect() {
      this.rectWidth += 10;
      this.drawRect();
    },
    subWidthRect() {
      this.rectWidth -= 10;
      this.drawRect();
    },
    addHeightRect() {
      this.rectHeight += 10;
      this.drawRect();
    },
    subHeightRect() {
      this.rectHeight -= 10;
      this.drawRect();
    },
    drawCircle() {
      this.vueCanvas.beginPath();
      this.vueCanvas.fillStyle = "green";
      this.vueCanvas.fill();
      this.vueCanvas.arc(100, 105, this.circleRadius, 0, 2 * Math.PI);
      this.vueCanvas.stroke();
    },
    addWidthCircle() {
      this.circleRadius += 10;
      this.drawCircle();
    },
    subWidthCircle() {
      this.circleRadius -= 10;
      this.drawCircle();
    },
    clear() {
      this.vueCanvas.clearRect(0, 0, 1400, 600);
    },
    /*addImage(e) {
      let input = document.getElementById("input");
      let myImg = new Image;
      myImg.src = URL.createObjectURL(e.target.files[0]);
      myImg.onload = function() {
        this.vueCanvas.drawImage(myImg, 20, 20);
        alert("Image is drawn")
      }
    }*/
  }

  /*data() {
    return {
      canvas: null,
      draw: false,
      context: null
    };
  },
  mounted() {
    var c = document.getElementById("c")
    this.canvas = document.getElementById("c");
    this.context = c.getContext("2d");
    this.canvas.height = window.innerHeight;
    this.canvas.width = window.innerWidth;
    //this.vueCanvas = context;
    // c.height = window.innerHeight;
  },
  methods: {
    drawBegin(e) {
      this.draw = true;
      console.log(this.draw);
      this.paint(e);
    },
    drawEnd() {
      this.draw = false;
      console.log(this.draw);
      this.context.beginPath();
    },
    paint(e) {
      if (!this.draw) return;
      this.context.lineWidth = 10;
      this.context.lineCap = "round";

      this.context.lineTo(e.clientX, e.clientY);
      this.context.stroke();

      this.context.beginPath();
      this.context.moveTo(e.clientX, e.clientY);
    }
  }*/
};
</script>

<style>
#c {
  height: 600px;
  width: 1400px;
  background-color: #fafafa;
}
#button-save,
#button-clear {
  float: left;
  margin: 5px;
}
button {
  padding: 5px;
}
.circles {
  margin-right: 13%;
}
</style>