<template>
    <v-app>
        <v-content class="pt-9">
            <div class="wrapper">
                <Logo 
                    :size = "'m'"
                />
                <div class="o-container">
                    <nuxt />
                </div>
            </div>
        </v-content>
        <v-footer
            app
        >
            <span>&copy; {{ new Date().getFullYear() }}</span>
        </v-footer>
    </v-app>
</template>

<script>
import Logo from '~/components/atoms/Logo'
export default {
    components: {
        Logo
    },
    data () {
        return {
        }
    }
}
</script>

<style lang="scss" scoped>
.o-container {
    max-width: 580px;
    width: 100%;
    padding: 12px;
    display: flex;
    justify-content: center;
}
.wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>