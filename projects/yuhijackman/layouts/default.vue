<template>
    <v-app>
      <SpHeader
          class="d-sm-none"
      />
      <FixedHeader
          class="d-none d-sm-block"
      />
      <v-content>
          <div class="o-container">
              <nuxt />
          </div>
      </v-content>
      <v-footer
          app
      >
          <span>&copy; Created by Freddie The Ferret</span>
      </v-footer>
    </v-app>
</template>

<script>
import FixedHeader from '~/components/organisms/panel/FixedHeader'
import SpHeader from '~/components/organisms/panel/SpHeader'
export default {
  name: 'Default',
  components: {
      FixedHeader,
      SpHeader
  },
  data () {
      return {
           
      }
  }
}
</script>

<style lang="scss">
* :not(.v-icon) {
  font-family: "Helvetica Neue", "Yu Gothic", YuGothic, Verdana, Meiryo, "M+ 1p", sans-serif;
}
.o-container {
    width: 100%;
    padding: 20px 16px;
}
</style>