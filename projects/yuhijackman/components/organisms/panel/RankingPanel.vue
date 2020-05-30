<template>
    <article>
        <h1>Ranking name(Props)</h1>
        <div class="ranking__content">
            <AccountCard
                v-for="account in accounts"
                :key="account.name"
                :account="account"
                class="mr-2"
            />
        </div>
    </article>
</template>

<script>
import AccountCard from '~/components/molecules/card/AccountCard'
export default {
    components: {
        AccountCard
    },
    data() {
        return {
            accounts: [
                {
                    name: 'Andrew',
                    thumbnailPath: 'assets/images/doughboys.png',
                    amountOfBooks: 4000,
                    amountOfMoney: 450000,
                },
                {
                    name: 'Hugh',
                    thumbnailPath: 'assets/images/doughboys.png',
                    amountOfBooks: 5000,
                    amountOfMoney: 550000,
                },
                {
                    name: 'Will',
                    thumbnailPath: 'assets/images/doughboys.png',
                    amountOfBooks: 6000,
                    amountOfMoney: 650000,
                },
                {
                    name: 'Dane',
                    thumbnailPath: 'assets/images/doughboys.png',
                    amountOfBooks: 7000,
                    amountOfMoney: 750000,
                },
                {
                    name: 'Harry',
                    thumbnailPath: 'assets/images/doughboys.png',
                    amountOfBooks: 2,
                    amountOfMoney: 2000,
                },
            ]
        }
    }
}
</script>

<style lang="scss" scoped>
.ranking {
    &__content {
        display: flex;
        padding: 10px 0;
    }
}
</style>