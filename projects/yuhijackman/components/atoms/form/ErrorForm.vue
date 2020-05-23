<template>
  <div class="error-form">
    <slot
      :error="!!errors.length"
      name="input"
    />
    <p
      v-if="errors.length"
      class="message"
      v-html="showingOnlyFirst ? message : messages"
    />
  </div>
</template>

<script>
export default {
  props: {
    errors: {
      type: Array,
      default() {
        return []
      }
    },

    showingOnlyFirst: {
      type: Boolean,
      default: false
    }
  },

  computed: {
    message() {
      return this.errors[0]
    },

    messages() {
      return _.union(this.errors).join('<br>')
    }
  }
}
</script>

<style lang="scss" scoped>
.error-form {
  width: 100%;

  &:not(:first-child) {
    margin-top: 10px;
  }
}

.message {
  color: $color-red;
  margin-top: 0.5em;
  font-size: 1.2rem;
}
</style>
