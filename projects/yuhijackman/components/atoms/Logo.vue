<template>
    <nuxt-link to='/'>
        <img :class="'logo-image--' + size" src="~/assets/images/logo.png" alt="Logo image">
    </nuxt-link>
</template>

<script>
export default {
    props: {
        size: {
            type: String,
            default: 's'
        },
    },
}
</script>
<style lang="scss" scoped>
.logo-image {
    &--s {
        height: 80px;
    }
    &--m {
        height: 110px;
    }
    &--l {
        height: 180px;
    }
}
</style>