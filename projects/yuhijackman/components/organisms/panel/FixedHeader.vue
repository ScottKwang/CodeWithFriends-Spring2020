<template>
    <v-app-bar
        fixed
        app
    >
        <Logo />
        <v-spacer />
        <ul
            v-if="isAuthenticated"
            class="navigation navigation__list">
            <li
                class="navigation__item"
            >
                <nuxt-link
                    to="/mypage"
                    class="navigation__link"
                >
                    <span>MY BOOK LIST</span>
                </nuxt-link>
            </li>
            <li
                class="navigation__item"
            >
                <nuxt-link
                    to="/profile"
                    class="navigation__link"
                >
                    <!-- <AccountIcon/> -->
                    <span>MY PROFILE</span>
                </nuxt-link>
            </li>
            <li
                class="navigation__item"
            >
                <button
                    @click="logOut"
                    class="navigation__link"
                >
                    <span>LOG OUT</span>
                </button>
            </li>
        </ul>
        <ul
            v-else
            class="navigation navigation__list">
            <li
                class="navigation__item"
            >
                <nuxt-link
                    to="/signin"
                    class="navigation__link"
                >
                    <span>SIGN IN</span>
                </nuxt-link>
            </li>
            <li
                class="navigation__item"
            >
                <nuxt-link
                    to="/signup"
                    class="navigation__link"
                >
                    <span>SIGN UP</span>
                </nuxt-link>
            </li>
        </ul>
    </v-app-bar>
</template>

<script>
import Logo from '~/components/atoms/Logo'
import AccountIcon from '~/components/atoms/icon/AccountIcon'
import { mapGetters, mapActions } from 'vuex'
export default {
    name: 'FixedHeader',
    components: {
        Logo,
        AccountIcon
    },
    props: {

    },
    data () {
        return {

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
.navigation {
    &__list {
        align-items: center;
        display: flex;
        list-style: none;
    }
    &__item {
        align-items: center;
        display: flex;
        list-style: none;
        margin-right: 30px;
        padding: 0px;
        &:last-child {
            margin-right: 0;
        }
    }
    &__link {
        position: relative;
        text-decoration: none;
        color: #000;
        padding: 10px 2px;
        font-weight: 700;
        &:before {
            content: "";
            position: absolute;
            width: 100%;
            height: 2px;
            bottom: 0;
            left: 0;
            background-color: #0097a7;
            visibility: hidden;
            transform: scaleX(0);
            transition: all 0.3s ease-in-out 0s;
        }
        &:hover:before {
            visibility: visible;
            transform: scaleX(1);
        }
    }
}
</style>