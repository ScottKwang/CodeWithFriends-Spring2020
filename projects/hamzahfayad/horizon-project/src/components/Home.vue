<template>
  <div id="home">
    <h1><a href="/">Horizon</a></h1>
    <footer>&copy; Hamzah - Code With Friends Spring 2020</footer>
  </div>
</template>

<script>

export default {
  name: 'Home',
  components: {
  }
}
</script>

<style>
#home {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
*{
  box-sizing: border-box;
  font-size: 16px;
}
body{
  background-color: #429fa9;
}
h1{
  color: #ffffff;
  font-size: 3rem;
  text-align: center;
  text-shadow: 1px 4px #265d63;
  border: white .5px solid;
  border-left: 1px;
  border-right: 1px;
}
footer{
    color: #161f29;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
}
</style>
