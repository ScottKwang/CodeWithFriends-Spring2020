<template>
    <article class="top-panel__field">
        <img class="image" src="~/assets/images/hero-image.png" alt="">
        <div class="mask"></div>
        <div
            v-if="isAuthenticated"
            class="top-panel__content"
        >
            <CurrentStatusPanel
                :totalPrice="totalPriceOfUnreadBooks"
                :amountOfBooks="amountOfUnreadBooks"
            />
            <BookSearchModalPanel />
        </div>
        <div 
            v-else
            class="top-panel__content"
        >
            <ServiceDescriptionPanel />
        </div>
    </article>
</template>

<script>
import ServiceDescriptionPanel from '~/components/molecules/field/ServiceDescriptionPanel'
import CurrentStatusPanel from '~/components/molecules/field/CurrentStatusPanel'
import BookSearchModalPanel from '~/components/organisms/panel/BookSearchModalPanel'
import { mapGetters, mapActions } from 'vuex'

export default {
    name: 'TopPanel',
    components: {
        ServiceDescriptionPanel,
        CurrentStatusPanel,
        BookSearchModalPanel,
    },
    created () {
        this.fetchMyBooks()
    },
    computed: {
        ...mapGetters([
            "isAuthenticated",
            "calculateTotalPriceByStatus",
            "calculateAmountOfBooksByStatus"
        ]),
        totalPriceOfUnreadBooks () {
            return this.calculateTotalPriceByStatus("unread")
        },
        amountOfUnreadBooks () {
            return this.calculateAmountOfBooksByStatus("unread")
        }
    },
    methods: {
        ...mapActions([
            'fetchMyBooks'          
        ]),
    }
}
</script>

<style lang="scss" scoped>
.top-panel {
    &__field {
        position: relative;
        // margin-bottom: 32px;
    }
    &__content {
        position: absolute;
        top: 0;
        width: 100%;
        z-index: 1;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        text-align: center;
    }
}

.image {
    width: 100%;
    height: 350px;
}

.mask {
    position: absolute;
    top: 0;
    width: 100%;
    height: 350px;
    z-index: 1;
    background-color: rgba(0,0,0,.4);
}

</style>