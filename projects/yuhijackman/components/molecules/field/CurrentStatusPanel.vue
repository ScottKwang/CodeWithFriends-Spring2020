<template>
    <div class="content">
        <div class="content__message">
            <h1 class="catchphrase">You let {{ amountOfBooks }} books pile up unread</h1>
            <p class="content__message--status">They are worth</p>
            <p class="content__message--value">${{ totalPrice }}</p>
        </div>
    </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
export default {
    name: "CurrentStatusPanel",
    props: {
        totalPrice: {
            type: Number,
            default: 0
        },
        amountOfBooks: {
            type: Number,
            default: 0
        }
    },
    computed: {
    },
}
</script>

<style lang="scss" scoped>
.content {
    margin-bottom: 10px;
    &__message {
        color: #fff;
        &--status {
            font-size: 2.2rem;
            margin-bottom: 0;
        }
        &--value {
            font-size: 2.8rem;
            margin-bottom: 0;
        }
    }
}

.catchphrase {
    font-size: 2.2rem;
    font-weight: 700;
}

.sub-message {
    font-size: 1.2rem;
}

@media screen and (max-width: 600px) {
    .content {
        &__message {
            &--status {
                font-size: 1.5rem;
            }
            &--value {
                font-size: 2rem;
            }
        }
    }
    .catchphrase {
        font-size: 1.5rem;
    }
    .sub-message {
        font-size: 1rem;
    }
}

</style>