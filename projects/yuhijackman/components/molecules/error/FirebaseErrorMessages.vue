<template>
    <div>
        <p
            v-if="errorMessages"
        >
            {{errorMessages}}
        </p>
    </div>
</template>

<script>
export default {
    props: {
        error: {
            type: Object
        }
    },
    computed: {
        errorMessages () {
            let code = this.error.code
            let message = this.error.message
            if (this.error.code == "auth/user-not-found") {
                return "There is no user record corresponding to this email address.";
            } else if (this.error.code == "auth/wrong-password") {
                return "The password is invalid.";
            }
            this.serverErrors = e.message
        }
    }
}
</script>