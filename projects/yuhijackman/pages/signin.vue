<template>
    <div
        class="form-body"
    >
        <h1>Sign in</h1>
        <p
            v-if="serverError"
            class="error-message"
        >
            {{ serverError }}   
        </p>
        <ValidationObserver ref="observer" v-slot="{ invalid }">
            <v-form
                ref="form"
                lazy-validation
            >
            <ValidationProvider rules="required|email" v-slot="{ errors }">
                <v-text-field
                    v-model="email" 
                    :error-messages="errors"
                    label="Email"
                    required
                ></v-text-field>
            </ValidationProvider>
            <ValidationProvider rules="required|min:6" v-slot="{ errors }">
                <v-text-field
                    v-model="password"
                    :error-messages="errors"
                    label="Password"
                    type="password"
                    required
                ></v-text-field>
            </ValidationProvider>
            <v-btn
                @click="signIn"
                color="success"
                class="mr-4"
            >
            Sign in
            </v-btn>
            </v-form>
        </ValidationObserver>
    </div>
</template>

<script>
import { auth } from '~/plugins/firebase';
import { mapActions } from  'vuex';
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default {
    layout: 'unauthenticated',
    components: {
        ValidationObserver,
        ValidationProvider
    },
    data() {
        return {
            email: '',
            password: '',
            serverError: ''
        }
    },
    computed: {
    
    },
    methods: {
        ...mapActions([
            'doSignIn'
        ]),
        async signIn() {
            this.serverError = ''
            const isValid = await this.$refs.observer.validate()
            if (isValid) {
                try {
                    await this.doSignIn({
                        email: this.email,
                        password: this.password
                    }) 
                    this.$router.push({ name: 'index' })
                } catch (error) {
                    if (error.code === "auth/wrong-password") {
                        this.serverError = "Incorrect email or password."
                    } else if (error.code ===  "auth/too-many-requests") {
                        this.serverError = "Too many unsuccessful attempts. Please try again later."
                    } else {
                        this.serverError = "Unexpected error occurs."
                    }
                }
            }
        }
    },
}
</script>

<style lang="scss" scoped>
.form-body {
    width: 100%;
    text-align: center;
}
.error-message {
    color: #ff5252;
}
</style>