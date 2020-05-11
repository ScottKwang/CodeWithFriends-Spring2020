<template>
  <div id="canvas">
    <button id="button-save">Save</button>
    <button id="button-save">Rectangle</button>
    <canvas id="c" @mousedown="rec"></canvas>
    <br />
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  mounted() {
    var c = document.getElementById("c");
    var context = c.getContext("2d");
    this.vueCanvas = context;
  },
  methods: {
    rec() {
      var c = document.getElementById("c");
      var ctx = c.getContext("2d");
      ctx.beginPath();
      ctx.rect(50,50, 20, 20);
      ctx.stroke();
    },
    showCoordsX() {
      var x = window.event.clientX; 
      console.log("x:" + x);
      return x;
    },
    showCoordsY() {
      var y = window.event.clientY;
      console.log("y:" + y);
      return y;
    },
    getCursorPositionX(canvas) {
      const rect = canvas.getBoundingClientRect();
      const x = window.event.clientX - rect.left;
      console.log("x: " + x);
      return x
    },
    getCursorPositionY(canvas) {
    const rect = canvas.getBoundingClientRect()
    const y = window.event.clientY - rect.top
    console.log(" y: " + y)
    return y
},
   getRandomInt(min, max) {
    //min = Math.ceil(min);
    //max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
  }
};
</script>

<style>
#c {
  height: 600px;
  width: 1400px;
  background-color: #fafafa;
}
#button-save {
  float: left;
  margin: 5px;
}
</style>