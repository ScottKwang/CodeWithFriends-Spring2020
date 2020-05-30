<template>
    <v-card
        class="book-card"
    >
        <v-card-text class="book-card__item">
            <div class="book-card__item--thumbnail">
                <BookThumbnail
                    :thumbnailPath="book.image"
                    :size="'150px'"
                />
            </div>
            <div class="book-card__item--info">
                <div class="book-card__item--detail">
                    <p class="book-card__item--name">{{book.title}}</p>
                </div>
                <div class="book-card__item--detail book-card__item--detail--column">
                    <p class="book-card__item--title">BY:</p>
                    <p class="book-card__item--value">{{book.author}}</p>
                </div>
                <div class="book-card__item--detail book-card__item--detail--column">
                    <p class="book-card__item--title">PRICE:</p>
                    <p class="book-card__item--value">${{book.price}}</p>
                </div>
                <div class="book-card__item--detail book-card__item--detail--column">
                    <p class="book-card__item--title">PURCHASE DATE:</p>
                    <p class="book-card__item--value">{{book.purchase_date}}</p>
                </div>
                <div class="book-card__item--detail book-card__item--detail--bottom">
                    <v-switch
                        :id="book.id"
                        :input-value="switchValue"
                        @change="onChange"
                        :label="`${book.status}`"
                    ></v-switch>
                </div>
            </div>
        </v-card-text>
    </v-card>
</template>

<script>
import BookThumbnail from '~/components/molecules/thumbnail/BookThumbnail'

export default {
    name: 'BookCard',
    props: {
        book: {
            type: Object,
            default: {}
        }
    },
    components: {
        BookThumbnail
    },
    computed: {
        switchValue () {
            return this.book.status === 'read'
        },
    },
    methods: {
        onChange() {
            this.$emit('change', this.book)
        }
    }
}
</script>

<style lang="scss" scoped>
.book-card {
    flex: 1 0 330px;
    max-width: 330px;
    margin-left: 10px;
    margin-top: 10px;
    &__item {
        display: flex;
        font-size: .9rem;
        height: 100%; 
        p {
            margin-bottom: 0;
        }
        &--info {
            flex: 1 0 0;
            display: flex;
            flex-direction: column;
        }
        &--name {
            font-size: 1.2rem;
            font-weight: 800;
        }
        &--title {
            display: inline-block;
            margin-right: 10px;
        }
        &--value {
            flex: 1 0 0;
            font-weight: 500;
        }
        &--detail {
            display: flex;
            margin-bottom: 10px;
            &--column {
                flex-direction: column;
            }
            &--bottom {
                margin-top: auto;
            }
        }
        &--thumbnail {
            margin-right: 10px;
        }
    }
}
</style>