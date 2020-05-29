<template>
    <section class="content">
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 class="m-0 text-dark">Suppliers</h1>
                    </div>
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Suppliers</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card card-primary card-outline">
                    <div class="card-header">
                        <h3 class="card-title">Suppliers</h3>
                          <button @click="addModal" class="btn btn-primary" style="float:right;">Add Supplier</button>
                    </div>
                    <div class="card-body table-responsive">
                        <table id="myTable" class="table table-bordered table-striped ">
                            <thead>
                                <tr>
                                    <th>Supplier ID</th>
                                    <th>Supplier Name</th>
                                    <th>Supplier Address</th>
                                    <th>Supplier Email</th>
                                    <th>Supplier Phone</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="supplier in suppliers" :key="supplier.supplier_id">
                                    <td>{{supplier.supplier_id}}</td>
                                    <td>{{supplier.supplier_name}}</td>
                                    <td>{{supplier.supplier_address}}</td>
                                    <td>{{supplier.supplier_email}}</td>
                                    <td>{{supplier.supplier_phone}}</td>
                                    <td>
                                        &emsp;
                                        <a href="#" >
                                        <i class="fa fa-edit" @click="editModal(supplier)"></i>
                                        </a>
                                        &emsp;
                                         <a href="#">
                                        <i class="fa fa-trash" @click="deleteSupplier(supplier.supplier_id)"></i>
                                        </a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
          <!-- Modal-->
         <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add Supplier</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form @submit.prevent="editMode ? updateSupplier() : addSupplier()">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Supplier Name</label>
                        <input v-model="supplier_name" required type="text" class="form-control" id="product" placeholder="Supplier Name">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">Address</label>
                        <input v-model="supplier_address" type="text" required class="form-control" id="barcode" placeholder="Address">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">Email</label>
                        <input v-model="supplier_email" type="email" required class="form-control" id="price" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">Phone</label>
                        <input v-model="supplier_phone" type="text" required class="form-control" id="price" placeholder="">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
                </form>
                </div>
            </div>
            </div>
         <!-- Modal -->
    </section>
</template>

<script>
    export default {
        data(){
            return{
                suppliers:[],
                supplier_name:'',
                supplier_address:'',
                supplier_email:'',
                supplier_phone:'',
                supplier_id:'',
                editMode:false,
            }
        },
        methods:{
           async getSuppliers(){
               await axios.get('/getSuppliers')
                    .then((res)=>{
                        this.suppliers = res.data
                        this.myTable()
                    })
            },
            myTable(){
               $(document).ready( function () {
                     $('#myTable').DataTable();
                  });
            },
            clearValues(){
                this.supplier_name=''
                this.supplier_address=''
                this.supplier_email=''
                this.supplier_phone=''
                this.supplier_Id=''
            },
            addModal(){
                this.editMode = false
                this.clearValues()
                $('#exampleModal').modal('show')
            },
            addSupplier(){
                axios.post('/addSupplier',{
                    supplier_name: this.supplier_name,
                    supplier_address: this.supplier_address,
                    supplier_phone: this.supplier_phone,
                    supplier_email: this.supplier_email
                })
                    .then((res)=>{
                        this.clearValues()
                        $('#exampleModal').modal('hide')
                        toastr.success('Supplier Added!')
                        this.getSuppliers()
                    }).catch((res)=>{
                        toastr.error(res.message+ ' Check your inputs')
                    })
            },
            editModal(supplier){
                this.editMode = true
                this.supplier_name = supplier.supplier_name
                this.supplier_address = supplier.supplier_address
                this.supplier_phone = supplier.supplier_phone
                this.supplier_email = supplier.supplier_email
                this.supplier_id = supplier.supplier_id
                $('#exampleModal').modal('show')
            },
            async updateSupplier(){
                await axios.put('/updateSupplier/?supplier_id='+this.supplier_id,{
                    supplier_name: this.supplier_name,
                    supplier_address: this.supplier_address,
                    supplier_phone: this.supplier_phone,
                    supplier_email: this.supplier_email
                })
                    .then((res)=>{
                        this.clearValues()
                        $('#exampleModal').modal('hide')
                        toastr.success('Updated!')
                        this.editMode = false
                        this.getSuppliers()
                    }).catch((err)=>{
                         toastr.error(res.message+ ' Check your inputs')
                    })
            },
            deleteSupplier(supplier_id){
                swal.fire({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                        }).then((result) => {
                            if (result.value) {
                                axios.delete('/deleteSupplier/?supplier_id='+supplier_id)
                                swal.fire(
                                'Deleted!',
                                'Supplier Deleted',
                                'success'
                            )
                        this.getSuppliers()
                    }
                })
            }

        },
        created(){
            this.getSuppliers()
             window.Echo.channel('Suppliers').listen('SupplierEvent',(e)=>{
                // console.log(e)
                // this.suppliers.push(e.supplier)
                this.getSuppliers() 
            })
        },
        mounted() {
            console.log('Component mounted.')
        }
    }
</script>
