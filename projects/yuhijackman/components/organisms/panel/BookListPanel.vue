<template>
    <article>
        <div>
            <div class="text-center justify-center py-6">
                <h1 class="panel-title">Book List</h1>
            </div>
            <v-tabs
                v-model="tab"
                background-color="transparent"
                color="cyan darken-2"
                slider-color="cyan darken-2"
                grow
            >
                <v-tab
                    v-for="item in items"
                    :key="item"
                >
                    {{ item }}
                </v-tab>
            </v-tabs>

            <v-tabs-items v-model="tab">
                <v-tab-item
                    v-for="item in items"
                    :key="item"
                >
                    <div
                        v-if="item === 'unread'"
                        class="card-container"
                    >
                        <div
                            v-if="hasUnreadBooks"
                            class="card-container__books"
                        >
                            <BookCard
                                v-for="book in unreadBooks"
                                :key="book.id"
                                :book="book"
                                @change="changeStatus"
                                class="mr-2"
                            />
                        </div>
                        <div
                            v-else
                            class="card-container__books-not-found">
                            <div class="card-container__inside-wrap">
                                <p>You have no books on the list.</p>
                                <BookSearchModalPanel />
                            </div>
                        </div>
                    </div>

                    <div
                        v-else-if="item === 'read'"
                        class="card-container"
                    >
                        <div
                            v-if="hasReadBooks"
                            class="card-container__books"
                        >
                            <BookCard
                                v-for="book in readBooks"
                                :key="book.id"
                                :book="book"
                                @change="changeStatus"
                                class="mr-2"
                            />
                        </div>
                        <div
                            v-else
                            class="card-container__books-not-found">
                            <div class="card-container__inside-wrap">
                                <p>You have no books on the list.</p>
                                <BookSearchModalPanel />
                            </div>
                        </div>
                    </div>
                </v-tab-item>
            </v-tabs-items>
        </div>
    </article>
</template>

<script>
import BookCard from '~/components/molecules/card/BookCard'
import BookSearchModalPanel from '~/components/organisms/panel/BookSearchModalPanel'
import { mapActions, mapGetters } from 'vuex'
import { auth, db } from '~/plugins/firebase'
export default {
    name: 'BookListPanel',
    components: {
        BookCard,
        BookSearchModalPanel
    },
    data () {
      return {
        tab: null,
        items: [
          'unread', 'read'
        ]
      }
    },
    created () {
        this.fetchMyBooks()
    },
    computed: {
        ...mapGetters([
            'getMyBooksByStatus'
        ]),
        unreadBooks () {
            return this.getMyBooksByStatus('unread')
        },
        readBooks () {
            return this.getMyBooksByStatus('read')
        },
        hasUnreadBooks () {
            return Object.keys(this.unreadBooks).length
        },
        hasReadBooks () {
            return Object.keys(this.readBooks).length
        }
    },
    methods: {
        ...mapActions([
            'fetchMyBooks',
            'changeBookStatus'            
        ]),
        changeStatus(book) {
            this.changeBookStatus(book)
        }
    }
}
</script>

<style lang="scss" scoped>
.panel-title {
    font-size: 2rem;
}
.card-container {
    &__books {
        display: flex;
        flex-wrap: wrap;
        margin-left: -10px;
    }
    &__books-not-found {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 100px 0;
    }
    &__inside-wrap {
        text-align: center;
        font-size: 1.2rem;
        font-weight: 700;
    }
}
</style>