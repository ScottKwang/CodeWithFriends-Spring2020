<template>
    <div class="content">
        <div class="content__message">
            <h1 class="catchphrase">Stop letting books pile up unread</h1>
            <p class="sub-message">Keep track of how much unread books on your shelves are worth.</p>
        </div>
        <div class="button-wrapper">
            <div class="mx-2">
                <v-btn
                large
                rounded
                color="blue lighten-1"
                class="button"
                :to="'signup'"
                >Sign up</v-btn>
            </div>
            <div class="mx-2">
                <v-btn
                large
                rounded
                color="cyan darken-2"
                class="button"
                :to="'signin'"
                >Sign in</v-btn>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'ServiceDescriptionPanel',
}
</script>

<style lang="scss" scoped>
.content {
    &__message {
        color: #fff;
    }
}
.button-wrapper {
    display: flex;
    justify-content: center;
}
.catchphrase {
    font-size: 2.2rem;
    font-weight: 700;
}

.sub-message {
    font-size: 1.2rem;
}

.button {
    color: #fff;
    width: 150px;
}
</style>