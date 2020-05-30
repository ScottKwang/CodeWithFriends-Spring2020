<template>
    <div id="nav" style="text-align: center;">
        <nav>
            <router-link tag="li" :to="{name: 'paint'}">Paint</router-link>
            <router-link tag="li" :to="{name: 'list'}">List</router-link>
            <router-link tag="li" to="/about">About</router-link>
        </nav>
        <router-view></router-view>
    </div>
</template>

<script> 
export default {
    
}
</script>
 
<style>
li, a{
    color: #ffffff;
    font-size: 1.5em;
    text-decoration: none;
    display: inline-block;
    text-align: center;
    margin: 0 20px;
    cursor: pointer;
    text-shadow: 1px 2px #173455;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
</style>