<template>
    <div class="wrapper">
        <div class="message-wrapper">
            <slot name="message"></slot>
        </div>
        <div class="image-wrapper" :class="'position--' + position">
            <slot name="image"></slot>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        position: {
            type: String,
            default: "right"
        }
    }
}
</script>

<style lang="scss" scoped>
.wrapper {
    display: flex;
}

.position {
    &--first {
        order: -1;
    }
}

.message-wrapper {
    flex: 1 0 60%;
    padding: 0 60px;
}
.image-wrapper {
    position: relative;
}

@media screen and (max-width: 800px) {
    .image-wrapper {
        display: none;
    }
    .message-wrapper {
        padding: 20px;
    }
}
</style>