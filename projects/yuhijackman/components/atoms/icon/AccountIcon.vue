<template>
  <svg
    :width="size"
    :height="size"
    class="cls-3"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    viewBox="0 0 26.9 26.9"
  >
    <path d="M24.1,21.5c-0.6-1.2-1.6-1.7-3.8-2.5s-4.1-1.3-4.1-1.3l-0.1-2c0,0,1.3-0.5,1.4-2.7c0.5,0.1,1-0.8,1.1-1.2c0.2-0.8,0.1-1.8-0.6-1.6C18,9.3,18,8.5,18,7.7c-0.2-2.1-1.8-3.4-4.4-3.4S9.4,5.6,9.2,7.7c-0.1,0.8-0.1,1.6,0,2.4c-0.6-0.1-0.8,0.8-0.6,1.6c0.1,0.5,0.6,1.4,1.1,1.2c0,2.2,1.4,2.7,1.4,2.7l-0.1,2c0,0-2,0.5-4.1,1.3S3.5,20.3,3,21.5c-0.5,1-0.4,5.4-0.4,5.4h22C24.6,26.9,24.6,22.5,24.1,21.5z" />
  </svg>
</template>

<script>
export default {
  props: {
    size: {
      type: String,
      default: '20px'
    }
  }
}
</script>

<style lang="scss" scoped>
.cls-1{fill:none;}
.cls-2{fill:#d6e0e9;}
.cls-3{clip-path:url(#clip-path);}
.cls-4{fill:#a1afbc;}
</style>
