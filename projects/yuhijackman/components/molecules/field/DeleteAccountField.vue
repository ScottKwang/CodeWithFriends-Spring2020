<template>
    <div>
        <h2 class="delete-field-title">Delete account</h2>
        <p>Careful! This will completely delete your account. You won’t be able to log in again after you do this.</p>
        <ValidationObserver ref="userObserver" v-slot="{ invalid }">
            <v-dialog v-model="dialog" persistent max-width="290">
                <template v-slot:activator="{ on }">
                    <v-btn
                        large
                        rounded
                        color="error"
                        class="button"
                        v-on="on"
                    >DELETE ACCOUNT</v-btn>
                </template>
                <v-card>
                    <v-card-title class="headline">Do you really want to delete account?</v-card-title>
                    <v-card-text> You won’t be able to restore your data.</v-card-text>
                    <v-card-text>
                        <ValidationProvider rules="required|min:6" v-slot="{ errors }">
                            <v-text-field
                                v-model="password"
                                :error-messages="errors"
                                label="Password"
                                type="password"
                                solo
                            ></v-text-field>
                        </ValidationProvider>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="green darken-1" text @click="dialog = false">Cancel</v-btn>
                        <v-btn color="green darken-1" text :disabled="invalid" @click="onSubmit">Submit</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </ValidationObserver>
    </div>
</template>

<script>
import { ValidationObserver, ValidationProvider } from 'vee-validate'
export default {
    name: 'DeleteAccountField',
    components: {
        ValidationObserver,
        ValidationProvider
    },
    props: {
        value: {
            type: String,
            default: ''
        }
    },
    data () {
        return {
            password: '',
            dialog: false
        }
    },
    watch: {
        value: {
            handler() {
                this.password = this.value
            },
            immediate: true
        },
        password() {
            if (this.value !== this.password) {
                this.$emit('input', this.password)
            }
        }
    },
    methods: {
        onSubmit() {
            this.dialog = false
            this.$emit('deleteAccount')
        }
    }
}
</script>

<style lang="scss" scoped>
.delete-field-title {
    font-size: 1.4rem;
}
</style>