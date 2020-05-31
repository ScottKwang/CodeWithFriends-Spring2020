<template>
    <v-card>
        <v-card-title class="headline">
            <slot name="title">Search for books</slot>
        </v-card-title>
        <v-card-text>
            <v-autocomplete
                v-model="selectedObj"
                :items="entries"
                :loading="isLoading"
                :search-input.sync="searchKeyword"
                color="cyan darken-2"
                item-color="cyan darken-2"
                hide-no-data
                item-text="title"
                item-value="id"
                label="Books"
                placeholder="Start typing to Search"
                prepend-icon="mdi-database-search"
                return-object
            ></v-autocomplete>
        </v-card-text>
        <v-divider></v-divider>
        <v-expand-transition>
            <v-list v-if="'title' in selectedBook">
                <v-list-item>
                    <slot name="result"></slot>
                </v-list-item>
            </v-list>
        </v-expand-transition>
        <v-card-actions>
            <slot name="actions" />
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: "BookSearchCard",
    props: {
        value: {
            type: String
        },
        entries: {
            type: Array
        },
        isLoading: {
            type: Boolean
        },
        selectedBook: {
            type: Object
        }
    },
    computed: {
        selectedObj: {
            get() {
                return this.selectedBook
            },
            set(newObj) {
                if (typeof newObj.title != 'undefined') {
                    this.$emit('selectBook', newObj)
                }
            }
        },
        searchKeyword: {
            get() {
                return this.value
            },
            set(searchKeyword) {
                this.$emit('input', searchKeyword)
            }
        }
    },
    watch: {
        searchKeyword (searchKeyword) {
            this.$emit('search', searchKeyword)
        }
    },
    methods: {

    }
}
</script>