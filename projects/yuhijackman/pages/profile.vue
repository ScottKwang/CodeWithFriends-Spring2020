<template>
    <div class="profile__container">
        <div class="profile__contents">
            <h1 class="profile__contents--title">My Profile</h1>
            <div class="profile__info">
                <h2 class="form--title">Information</h2>
                <v-alert
                    type="error"
                    v-if="serverErrors.userInfo"
                    class="error-message"
                >
                    {{ serverErrors.userInfo }}
                </v-alert>
                <ValidationObserver ref="userObserver" v-slot="{ invalid }">
                    <form class="form">
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">User profile name</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider rules="required" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.name"
                                        :error-messages="errors"
                                        label="Your name"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">Email</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider rules="required|email" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.email"
                                        :error-messages="errors"
                                        label="Your email"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">Password</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider rules="required|min:6" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.password"
                                        :error-messages="errors"
                                        label="You need to enter password to update"
                                        type="password"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <DeleteAccountField
                            v-model="values.passwordForDelete"
                            @deleteAccount="deleteAccount"
                            class="profile__info--delete-field"
                        />
                        <v-alert
                            type="error"
                            v-if="serverErrors.deleteAccount"
                            class="error-message"
                        >
                            {{ serverErrors.deleteAccount }}
                        </v-alert>
                        <hr class="end-line">
                        <div class="form--button">
                            <v-btn
                                large
                                rounded
                                :disabled="invalid"
                                @click="updateUserInfo"
                                color="cyan darken-2"
                                class="white--text px-5 save-button"
                            >
                                SAVE CHANGES
                            </v-btn>
                        </div>
                    </form>
                </ValidationObserver>
            </div>
            <div class="update-password-field">
                <h2>Password</h2>
                <p>Change your password</p>
                <v-alert
                    type="error"
                    v-if="serverErrors.password"
                    class="error-message"
                >
                    {{ serverErrors.password }}
                </v-alert>
                <ValidationObserver ref="passwordObserver" v-slot="{ invalid }">
                    <form class="form">
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">Old password</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider rules="required|min:6" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.oldPassword"
                                        :error-messages="errors"
                                        label="Old password"
                                        type="password"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">New password</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider rules="required|min:6|password:@confirm" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.newPassword"
                                        :error-messages="errors"
                                        label="New password"
                                        type="password"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <div class="form--item">
                            <div class="form--left-item">
                                <v-subheader class="item-name">Confirm password</v-subheader>
                            </div>
                            <div class="form--right-item">
                                <ValidationProvider name="confirm" rules="required|min:6" v-slot="{ errors }">
                                    <v-text-field
                                        v-model="values.confirmPassword"
                                        :error-messages="errors"
                                        label="Confirm password"
                                        type="password"
                                        solo
                                    ></v-text-field>
                                </ValidationProvider>
                            </div>
                        </div>
                        <div class="form--button">
                            <v-btn
                                large
                                rounded
                                :disabled="invalid"
                                @click="updatePassword"
                                color="cyan darken-2"
                                class="white--text px-5 save-button"
                            >
                                UPDATE PASSWORD
                            </v-btn>
                        </div>
                    </form>
                </ValidationObserver>
            </div>
        </div>
    </div>
</template>

<script>
import DeleteAccountField from "~/components/molecules/field/DeleteAccountField"
import { ValidationObserver, ValidationProvider } from 'vee-validate'
import { mapActions, mapGetters } from 'vuex'

export default {
    middleware: 'authenticated',
    components: {
        DeleteAccountField,
        ValidationObserver,
        ValidationProvider
    },
    data() {
        return {
            values: {
                name: '',
                email: '',
                password: '',
                oldPassword: '',
                newPassword: '',
                confirmPassword: '',
                passwordForDelete: ''
            },
            serverErrors: {
                password: '',
                userInfo: '',
                deleteAccount: ''
            }
        }
    },
    created () {
        this.values = {
            ...this.values,
            name: this.authUser.name,
            email: this.authUser.email,
        }
    },
    computed: {
        ...mapGetters([
            'getUserInfo'
        ]),
        authUser() {
            return this.getUserInfo
        }
    },
    methods: {
        ...mapActions([
            'updateAuthUser',
            'updateFirebaseUserPassword',
            'updateFirebaseUserEmail',
            'deleteUser'
        ]),
        async updateUserInfo() {
            this.serverErrors.userInfo = ""
            const isValid = await this.$refs.userObserver.validate()
            if (isValid) {
                try {
                    const response = await this.updateFirebaseUserEmail({
                        newEmail:  this.values.email,
                        password: this.values.password
                    })
                    await this.updateAuthUser({
                        name: this.values.name
                    })
                } catch(error) {
                    if (error.code == "auth/wrong-password") {
                        this.serverErrors.userInfo = "The password you provided did not match your existing password."
                    } else if (error.code == "auth/email-already-in-use") {
                        this.serverErrors.userInfo = "The email is already used by another user."
                    } else if (error.code ==  "auth/too-many-requests") {
                        this.serverErrors.userInfo = "Too many unsuccessful attempts. Please try again later."
                    } else {
                        this.serverErrors.userInfo = "Unexpected error occurs."
                    }
                }
            }
        },
        async updatePassword() {
            this.serverErrors.password = ""
            const isValid = await this.$refs.passwordObserver.validate()
            if (isValid) {
                try {
                    const response = await this.updateFirebaseUserPassword({
                        oldPassword: this.values.oldPassword,
                        newPassword: this.values.newPassword
                    })
                } catch(error) {
                    this.serverErrors.password = "The old password you provided did not match your existing password."
                }
            }
        },
        async deleteAccount () {
            this.serverErrors.deleteAccount = ""
            try {
                const response = await this.deleteUser({
                    password: this.values.passwordForDelete
                })
                this.$router.push({ name: 'index' })
            } catch (error) {
                if (error.code == "auth/wrong-password") {
                    this.serverErrors.deleteAccount = "The password you provided did not match your existing password."
                } else if (error.code == "auth/email-already-in-use") {
                    this.serverErrors.deleteAccount = "The email is already used by another user."
                } else if (error.code ==  "auth/too-many-requests") {
                    this.serverErrors.deleteAccount = "Too many unsuccessful attempts. Please try again later."
                } else {
                    this.serverErrors.deleteAccount = "Unexpected error occurs."
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.profile {
    &__contents {
    }
    &__container {
        display: flex;
        justify-content: center;
    }
    &__info {
        display: flex;
        flex-direction: column;
        padding: 2rem 0;
        &--delete-field {
            padding-bottom: 2.1rem;
            padding-left: 12px;
        }
    }
}
.form {
    &--title {
        margin-bottom: 1rem;
    }
    &--item {
        display: flex;
        flex-wrap: wrap;
        flex: 1 1 auto;
        margin-right: -12px;
        margin-left: -12px;
    }
    &--right-item {
        flex: 0 0 66.6666666667%;
        max-width: 66.6666666667%;
        width: 100%;
        padding: 12px;
    }
    &--left-item {
        flex: 0 0 33.3333333333%;
        max-width: 33.3333333333%;
        width: 100%;
        padding: 12px;
    }
    &--button {
        padding-top: 2rem;
        display: flex;
        justify-content: flex-end;
    } 
}

.update-password-field {
    padding: 2rem 0;
}

.item-name {
    font-size: 1.1rem;
    color: #000;
    font-weight: 700 !important;
}

.end-line {
    border-color: rgb(229, 227, 221);
}

@media screen and (max-width: 600px) {
    .form {
        &--item {
            flex-direction: column;
        }
        &--right-item,
        &--left-item {
            max-width: 100%;
            padding: 0 12px;
        }
    }
    .profile__info--delete-field {
        padding-left: 0;
    }
    .item-name {
        padding: 0;
    }
}
</style>