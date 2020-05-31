<template>
    <div
        class="form-body"
    >
        <h1>Sign up</h1>
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
                <ValidationProvider rules="required" v-slot="{ errors }">
                    <v-text-field
                        v-model="name"
                        :error-messages="errors"
                        label="Name"
                        required
                    ></v-text-field>
                </ValidationProvider>
                <ValidationProvider rules="required|email" v-slot="{ errors }">
                    <v-text-field
                        v-model="email"
                        :error-messages="errors"
                        label="Email"
                        required
                    ></v-text-field>
                </ValidationProvider>
                <ValidationProvider rules="required|min:6|password:@confirm" v-slot="{ errors }">
                    <v-text-field
                        v-model="password"
                        :error-messages="errors"
                        label="Password"
                        type="password"
                        required
                    ></v-text-field>
                </ValidationProvider>
                <ValidationProvider name="confirm" rules="required|min:6" v-slot="{ errors }">
                    <v-text-field
                        v-model="confirmPassword"
                        :error-messages="errors"
                        label="Confirm password"
                        type="password"
                        required
                    ></v-text-field>
                </ValidationProvider>
                <v-btn
                    :disabled="invalid"
                    @click="signUp"
                    color="success"
                    class="mr-4"
                >
                Sign up
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
            name: '',
            email: '',
            password: '',
            confirmPassword: '',
            serverError: ''
        }
    },
    methods: {
        ...mapActions([
            'doSignUp'
        ]),
        async signUp() {
            this.serverError = ''
            const isValid = await this.$refs.observer.validate();
            if (isValid) {
                try {
                    await this.doSignUp({
                        email: this.email,
                        password: this.password,
                        name: this.name
                    }) 
                    this.$router.push({ name: 'index' })
                } catch (error) {
                    console.log(error)
                    this.serverError = error.message
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.form-body {
    width: 100%;
    text-align: center;
}
</style>