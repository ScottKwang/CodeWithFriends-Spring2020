<template>
  <div class="index">
    <TopPanel
        class="mb-8"
    />
    <BookListPanel
        class="mb-8"
    />
  </div>
</template>

<script>
import RankingPanel from '~/components/organisms/panel/RankingPanel'
import TopPanel from '~/components/organisms/panel/TopPanel'
import BookListPanel from '~/components/organisms/panel/BookListPanel'

export default {
  middleware: 'authenticated',
  components: {
      TopPanel,
      BookListPanel
  },
  data () {
    return {
     
    }
  }
}
</script>


<style lang="scss" scoped>
.index {
    overflow: hidden;
}
</style>