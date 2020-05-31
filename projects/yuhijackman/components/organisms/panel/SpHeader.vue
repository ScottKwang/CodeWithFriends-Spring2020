<template>
    <div>
        <v-navigation-drawer
            v-model="drawer"
            right
            temporary
            fixed
            app
        >
            <v-list
                v-if="isAuthenticated"
            >
                <v-list-item
                    router
                    exact
                >
                    <v-list-item-content>
                        <v-list-item-title>
                            <nuxt-link
                                to="/mypage"
                                class="navigation__link"
                            >
                                <span>MY BOOK LIST</span>
                            </nuxt-link>
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item
                    router
                    exact
                >
                    <v-list-item-content>
                        <v-list-item-title>
                            <nuxt-link
                                to="/profile"
                                class="navigation__link"
                            >
                                <span>MY PROFILE</span>
                            </nuxt-link>
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item
                    router
                    exact
                >
                    <v-list-item-content>
                        <v-list-item-title
                            v-text="'LOG OUT'"
                            @click="logOut"
                        />
                    </v-list-item-content>
                </v-list-item>
            </v-list>
            <v-list
                v-else
            >
                <v-list-item
                    router
                    exact
                >
                    <v-list-item-content>
                        <v-list-item-title>
                            <nuxt-link
                                to="/signin"
                                class="navigation__link"
                            >
                                <span>SIGN IN</span>
                            </nuxt-link>
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item
                    router
                    exact
                >
                    <v-list-item-content>
                        <v-list-item-title>
                            <nuxt-link
                                to="/signup"
                                class="navigation__link"
                            >
                                <span>SIGN UP</span>
                            </nuxt-link>
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <v-app-bar
            fixed
            app
        >
            <Logo />
            <v-spacer />
            <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
        </v-app-bar>
    </div>
</template>

<script>
import Logo from '~/components/atoms/Logo'
import { mapGetters, mapActions } from 'vuex'

export default {
    components: {
        Logo
    },
    props: {

    },
    data () {
        return {
            drawer: false,
        }
    },
    computed: {
        ...mapGetters([
            'isAuthenticated'
        ]),
    },
    methods: {
        ...mapActions([
            'doLogOut'
        ]),
        logOut() {
            this.doLogOut()
            this.$router.push({ name: 'signin' })
        }
    }
}
</script>

<style lang="scss" scoped>
.navigation__link {
    text-decoration: none;
    color: #000;
}
</style>