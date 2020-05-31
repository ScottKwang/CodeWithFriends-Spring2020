<template>
<div>
    <button @click="add()">Delete</button>
</div>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            list: {},
        }
    },
    components: {

    },
    methods: {
        add(id) {
            axios.post("http:localhost:3000/add")
            .then(function(res) {
                this.$router.push({name: 'list'})
                console.log(res)
            })
            .catch(err => {
                console.log(err)
            })
        }
    }
}
</script> 