<template>
    <div>
        <img
            v-if="thumbnailPath"
            :src="thumbnailPath"
            :style="{ width: size, height: 'auto' }"
            class="book-thumbnail"
        >
        <div
            v-else
        >
            No thumbnail
        </div>
    </div>
</template>

<script>
export default {
    name: 'BookThumbnail',
    props: {
        thumbnailPath: {
            type: String,
            default: null
        },
        size: {
            type: String,
            default: "5rem"
        }
    },
    computed: {
        
    }
}
</script>

<style lang="scss" scoped>
.book-thumbnail {
    overflow: hidden;
}
</style>