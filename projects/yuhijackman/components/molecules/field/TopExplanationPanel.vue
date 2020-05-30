<template>
    <div>
        <h1 class="article-title" :class="'text-color--' + colorName">
           <slot name="title"></slot>
        </h1>
        <div class="article-contents">
            <slot name="contents"></slot>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        colorName: {
            type: String,
            default: "primary"
        }
    }
}
</script>

<style lang="scss" scoped>
.article-title {
    text-align: center;
    margin-bottom: 1rem;
    font-size: 2rem;
    font-weight: 700;
    font-family: aktiv-grotesk, sans-serif;
}
.article-contents {
  display: flex;
}
.text-color {
    &--primary {
        color:#0097a7;
    }
    &--red {
        color:#FF5252;
    }
}
</style>