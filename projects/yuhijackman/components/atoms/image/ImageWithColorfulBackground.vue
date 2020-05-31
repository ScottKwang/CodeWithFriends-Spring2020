<template>
    <div class="image" :class="'position--' + imagePosition">
        <img
            v-if="imageType === 'pile'"
            src="@/assets/images/pile.png"
        >
        <img
            v-else-if="imageType === 'howtouse'"
            src="@/assets/images/howtouse.png"
        >
        <div class="background" :class="'background--' + colorName"></div>
    </div>
</template>

<script>
export default {
    props: {
        colorName: {
            type: String,
            default: "colorName"
        },
        imagePosition: {
            type: String,
            default: "right"
        },
        imageType: {
            type: String
        }
    }
}
</script>

<style lang="scss" scoped>
.image {
    position: relative;
    img {
        position: relative;
        z-index: 1;
        width: calc(100% - 20px);
        box-shadow: 10px 20px 10px rgba(0,0,0,0.2);
    }
}
.position {
    &--left {
        img {
            transform: translateX(20px);
        }
        div {
            right: 20px;
        }
    }
    &--right {
        div {
            left: 20px;
        }
    }
}
.background {
    position: absolute;
    width: calc(100% - 20px);
    height: 100%;
    top: 20px;
    &--primary {
        background-color: #0097a7;
    }
    &--red {
        background-color: #FF5252;
    }
}
</style>