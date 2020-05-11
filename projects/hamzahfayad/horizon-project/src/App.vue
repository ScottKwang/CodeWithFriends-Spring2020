<template>
  <div id="app">
    <Home />
    <Nav />
  </div>
</template>

<script>
import Home from "./components/Home";
import Nav from "./components/Nav";
export default {
  name: "App",
  components: {
    Home,
    Nav
  }
};
</script>

<style scoped>
img{
  display: block;
  margin: auto;
  animation-name: cloud;
  animation-duration: 2s;
  animation-iteration-count: infinite;
    animation-direction: alternate;

}
@keyframes cloud {
  0% {margin-left: 40%;}
  100% {margin-left: 45%;}
}
</style>
