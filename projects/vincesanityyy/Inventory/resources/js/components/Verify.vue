<template>
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">Verify Your Account
                    <a href="#" class="float-right" @click="logout">Logout</a>
                </div>

                <div class="card-body">
                    <div class="form-group row">
                        <label for="code" class="col-md-4 col-form-label text-md-right">Enter Code</label>

                        <div class="row">
                            <div class="col-md-6">
                                <input v-model="code" type="text" class="form-control">
                            </div>
                            <div class="col-md-6">
                              <button @click="verifyCode" class="btn btn-primary">Submit Code</button>
                            </div>
                        </div>
                        
                    </div>

                    Before proceeding, please enter your verification code that was sent to your <strong>email</strong>.

                    If you did not receive the code,
		            <button @click="resend" type="submit" class="btn btn-link p-0 m-0 align-baseline">click here to request </button>  another code.
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                code:'',
                csrf: document.querySelector('meta[name="csrf-token"]').getAttribute('content'),
            }
        },
        methods:{
            resend(){
                console.log('Clicked')
                 let loader = this.$loading.show({
                            container: this.fullPage ? null : this.$refs.formContainer,
                            onCancel: this.onCancel,
                            color: '#c91010',
                            loader: 'bars',
                            width: 80,
                            height: 100,
                })
                axios.post('/resend')
                    .then((res)=>{
                        loader.hide();
                        toastr.success('Verification code sent!')
                    }).catch((err)=>{
                        loader.hide();
                        toastr.error(err)
                    })
            },
            verifyCode(){
                axios.post('/verifyUser',{
                    code:this.code
                }).then((res)=>{
                    // this.$router.push('/home')
                    window.location.href = '/home';
                    toastr.success('Account Verified!')
                }).catch((err)=>{
                    toastr.error('Invalid Code')
                })
            },
            logout(){
                let loader = this.$loading.show({
                            container: this.fullPage ? null : this.$refs.formContainer,
                            onCancel: this.onCancel,
                            color: '#c91010',
                            loader: 'bars',
                            width: 80,
                            height: 100,
                })
                axios.post('logout').then((res)=>{
                    toastr.success('Bye nigga')
                    loader.hide();
                    window.location.href = '/login'
                }).catch((err)=>{
                    loader.hide();
                    toastr.error(err)
                })
            }
        },
        mounted() {
            console.log('Component mounted.')
        }
    }
</script>
