<template>
    <v-card
        min-width="250"
    >
        <v-card-text class="account-card__item">
            <div class="account-card__item--detail">
                <AccountThumbnail
                    :thumbnailPath="account.thumbnailPath"
                />
            </div>
            <div class="account-card__item--detail">
                <p class="account-card__item--name">{{account.name}}</p>
            </div>
            <div class="account-card__item--detail">
                <p class="account-card__item--title">AMOUNT:</p>
                <p class="account-card__item--value">${{account.amountOfMoney}}</p>
            </div>
            <div class="account-card__item--detail">
                <p class="account-card__item--title">AMOUNT:</p>
                <p class="account-card__item--value">{{account.amountOfBooks}} books</p>
            </div>
        </v-card-text>
    </v-card>
</template>

<script>
import AccountThumbnail from '~/components/molecules/thumbnail/AccountThumbnail'

export default {
    name: 'AccountCard',
    components: {
        AccountThumbnail
    },
    props: {
        account: {
            type: Object,
            default: {}
        }
    }
}
</script>

<style lang="scss" scoped>
.account-card {
    &__item {
        display: flex;
        flex-direction: column;
        font-size: 1rem;
        p {
            margin-bottom: 0;
        }
        &--name {
            font-weight: 800;
        }
        &--title {
            margin-right: 10px;
        }
        &--value {
            flex: 1 0 0;
        }
        &--detail {
            display: flex;
            justify-content: center;
            margin-bottom: 10px;
        }
    }
}
</style>