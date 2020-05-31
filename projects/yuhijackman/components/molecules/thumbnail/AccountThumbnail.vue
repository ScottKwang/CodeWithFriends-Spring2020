<template>
    <div>
        <img
            v-if="thumbnailPath"
            :src="url"
            :style="{ width: size, height: size }"
            class="account-thumbnail"
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
    name: 'AccountThumbnail',
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
        url() {
            return '/_nuxt/' + this.thumbnailPath;
        }
    }
}
</script>

<style lang="scss" scoped>
.account-thumbnail {
    border-radius: 50%;
    overflow: hidden;
}
</style>