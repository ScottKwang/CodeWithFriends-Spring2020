<template>
  <div class="register-box">
  <div class="register-logo">
    <a href="/"><b>Hello</b>Nigga</a>
  </div>

  <div class="card">
    <div class="card-body register-card-body">
      <p class="login-box-msg">Register a new membership</p>

      <form @submit.prevent="register" method="post">
        <div class="input-group mb-3">
          <input v-model="name" type="text" class="form-control" placeholder="Full name">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-user"></span>
            </div>
          </div>
        </div>
        <div class="input-group mb-3">
          <input v-model="email" type="email" class="form-control" placeholder="Email">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-envelope"></span>
            </div>
          </div>
        </div>
        <div class="input-group mb-3">
          <input v-model="phone" type="text" class="form-control" placeholder="Phone">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-phone"></span>
            </div>
          </div>
        </div>
        <div class="input-group mb-3">
          <input v-model="password" type="password" class="form-control" placeholder="Password">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-lock"></span>
            </div>
          </div>
        </div>
        <div class="input-group mb-3">
          <input v-model="confirmPass" type="password" class="form-control" placeholder="Retype password">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-lock"></span>
            </div>
          </div>
        </div>
        <div class="row">
          <!-- /.col -->
          <div class="col-4">
            <button type="submit" class="btn btn-primary btn-block">Register</button>
          </div>
          <!-- /.col -->
        </div>
      </form>

      <a href="/login" class="text-center">Aleady a member? Login here</a>
    </div>
    <!-- /.form-box -->
  </div><!-- /.card -->
</div>
</template>

<script>

export default {
data(){
    return{
        email:'',
        password:'',
        confirmPass:'',
        name:'',
        phone:''
        }
    },
    methods:{
        register(){
            if(this.password != this.confirmPass){
                toastr.error('Passwords diidnt match')
            }
            else{
                if(/^(09|\+639)\d{9}$/.test(this.phone)){
                    let loader = this.$loading.show({
                        container: this.fullPage ? null : this.$refs.formContainer,
                        onCancel: this.onCancel,
                        color: '#c91010',
                        loader: 'bars',
                        width: 80,
                        height: 100,
                    })
                    axios.post('register',{
                        email: this.email,
                        password: this.password,
                        phone: this.phone,
                        name:this.name
                    }).then((res)=>{
                        loader.hide();
                        window.location.href = '/home'
                    }).catch((err)=>{
                        loader.hide();
                        toastr.error('Records not found')
                    })
                }else{
                    toastr.error('Not valid PH phone number')
                }
            }
        }
    },
    created(){
        console.log('compoent created')
    }
}

</script>

