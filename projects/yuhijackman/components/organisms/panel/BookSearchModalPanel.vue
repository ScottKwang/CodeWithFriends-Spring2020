<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on }">
            <v-btn color="cyan darken-2" dark v-on="on">Add a book to your list</v-btn>
        </template>
        <BookSearchCard
            v-model="searchKeyword"
            :entries="suggestions"
            :is-loading="isLoading"
            :selected-book="selectedBookData"
            @selectBook="(selectedBook) => selectBook(selectedBook)"
            @search="(keyword) => searchBooks(keyword)"
        >
            <template #title>
                Search for books by title
            </template>
            <template #result>
                <v-list-item-avatar
                    height="100%"
                    width="80px"
                >
                    <v-img
                        :src="selectedBookData.image"
                        aspect-ratio="1"
                    >
                    </v-img>
                </v-list-item-avatar>
                <ValidationObserver ref="observer" v-slot="{ invalid }">
                    <v-list-item-content>
                        <v-list-item-title v-text="selectedBookData.title"></v-list-item-title>
                        <v-list-item-subtitle v-text="selectedBookData.author"></v-list-item-subtitle>
                        <v-list-item
                            class="px-0"
                        >
                            <ValidationProvider rules="required" v-slot="{ errors }">
                                <v-text-field
                                    v-model.number="price"
                                    label="Price"
                                    value=""
                                    prefix="$"
                                    type="number"
                                ></v-text-field>
                                <span id="error">{{ errors[0] }}</span>
                            </ValidationProvider>
                        </v-list-item>
                        <v-list-item
                            class="px-0"
                        >
                            <ValidationProvider rules="required|date_format" v-slot="{ errors }">
                                <v-text-field
                                    v-model="purchaseDate"
                                    label="Purchase Date"
                                    value=""
                                    type="date"
                                ></v-text-field>
                                <span id="error">{{ errors[0] }}</span>
                            </ValidationProvider>
                        </v-list-item>
                    </v-list-item-content>
                </ValidationObserver>
            </template>
            <template #actions>
                <v-btn
                    color="grey darken-1"
                    @click="closeModal"
                    class="white--text px-1"
                >
                    CLOSE
                    <v-icon right>mdi-close-circle</v-icon>
                </v-btn>
                <v-btn
                    :disabled="!('title' in selectedBookData)"
                    color="grey darken-1"
                    @click="reset"
                    class="white--text px-1"
                >
                    CLEAR
                    <v-icon right>mdi-close-circle</v-icon>
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                    :disabled="!('title' in selectedBookData)"
                    color="cyan darken-2"
                    @click="saveBook"
                    class="white--text px-5"
                >
                    SAVE
                </v-btn>
            </template>
        </BookSearchCard>
    </v-dialog>
</template>

<script>
import BookSearchCard from '~/components/molecules/card/BookSearchCard'
import { ValidationObserver, ValidationProvider } from 'vee-validate'
import { mapActions, mapGetters } from  'vuex'
import moment from 'moment'

export default {
    name: "BookSearchModalPanel",

    components: {
        BookSearchCard,
        ValidationObserver,
        ValidationProvider
    },

    data() {
        return {
            dialog: false,
            searchKeyword: '',
            entries: [],
            isLoading: false,
            descriptionLimit: 60,
            selectedBookData: {},
            price: null,
            purchaseDate: ''
        }
    },

    computed: {
        suggestions() {
            return this.entries.map(entry => {
                const title = entry.title.length > this.descriptionLimit
                    ? entry.title.slice(0, this.descriptionLimit) + '...'
                    : entry.title
                return Object.assign({}, entry, { title })
            })
        },
        addingData() {
            return  {
                ...this.selectedBookData,
                price: this.price,
                status: 'unread',
                purchase_date: this.purchaseDate
            }
        }
    },

    methods: {
        ...mapActions([
            "addUnreadBook"
        ]),
        ...mapGetters([
            "findUnreadBookById"
        ]),
        selectBook(selectedBook) {
            this.selectedBookData = selectedBook
        },

        closeModal() {
            this.reset()
            this.dialog = false
        },

        async saveBook() {     
            const isValid = await this.$refs.observer.validate();
            if (isValid) {
                await this.addUnreadBook(this.addingData)
                this.closeModal()
            }
        },

        reset() {
            this.selectedBookData = {}
            this.entries = []
            this.searchKeyword = ''
            this.price = null
            this.purchaseDate = ''
        },

        async searchBooks (keyword) {
            if (this.isLoading) return
            this.isLoading = true
            this.$axios.$get('./.netlify/functions/book', {
                params: { 
                    q: keyword,
                    key: process.env.GOODREADS_API_KEY
                }
            }).then((response) => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(response, "text/xml");
                const works = doc.getElementsByTagName('work');
                const bookData = [...works].reduce((result, work) => {
                    let tmp = {
                        id: work.getElementsByTagName('id')[0].childNodes[0].nodeValue,
                        title: work.getElementsByTagName('title')[0].childNodes[0].nodeValue,
                        author: work.getElementsByTagName('author')[0].getElementsByTagName('name')[0].childNodes[0].nodeValue,
                        image: work.getElementsByTagName('image_url')[0].childNodes[0].nodeValue
                    }
                    result.push(tmp)
                    return result;
                }, []);
                this.entries = bookData;
            }).catch((error) => {
                console.log(error);
                return Promise.reject(error)
            }) .finally(() => (
                this.isLoading = false
            ))
        }
    }
}
</script>