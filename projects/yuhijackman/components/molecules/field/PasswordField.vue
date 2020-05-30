<template>
    <div>
        <slot name="title"></slot>
        <slot name="desc"></slot>
        <form class="form">
            <div class="form--left-item">
                <v-subheader class="item-name">Old password</v-subheader>
            </div>
            <div class="form--right-item">
                <v-text-field
                    v-model="oldPassword"
                    label="Old password"
                    solo
                ></v-text-field>
            </div>
            <div class="form--left-item">
                <v-subheader class="item-name">New password</v-subheader>
            </div>
            <div class="form--right-item">
                <v-text-field
                    v-model="newPassword"
                    label="New password"
                    solo
                ></v-text-field>
            </div>   
             <div class="form--left-item">
                <v-subheader class="item-name">Confirm password</v-subheader>
            </div>
            <div class="form--right-item">
                <v-text-field
                    v-model="confirmPassword"
                    label="Confirm password"
                    solo
                ></v-text-field>
            </div>      
        </form>
        <slot name="button"></slot>
    </div>
</template>

<script>
export default {
    name: 'PasswordField',
}
</script>

<style lang="scss" scoped>
.form {
    display: flex;
    flex-wrap: wrap;
    flex: 1 1 auto;
    margin-right: -12px;
    margin-left: -12px;
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
}
</style>