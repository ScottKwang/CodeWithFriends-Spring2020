<template>
    <section class="content">
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 class="m-0 text-dark">Product List</h1>
                    </div>
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Products</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card card-primary card-outline">
                    <div class="card-header">
                        <h3 class="card-title">Products</h3>
                        <button @click="addModal" class="btn btn-primary" style="float:right;">Add Product</button>
                    </div>
                    <div class="card-body table-responsive">
                        <table id="myTable" class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Preview</th>
                                    <th>Barcode</th>
                                    <th>Product Name</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Supplier</th>
                                    <th>Category</th>
                                    <th>Tags</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="product in products" :key="product.product_id">
                                    <td>
                                        <img :src="'img/'+ product.image" 
                                        class="img-responsive" 
                                        style="display:block; height: auto;"
                                        alt="Preview">
                                    </td>
                                    <td>{{product.barcode}}</td>
                                    <td>{{product.product_name}}</td>
                                    <td>{{product.price}}</td>
                                    <td>{{product.quantity}}</td>
                                    <td>{{product.supplier.supplier_name}}</td>
                                    <td>{{product.category}}</td>
                                    <td>
                                        <span v-if="product.quantity <= 10" class="badge bg-danger">Need to Restock</span>
                                        <span v-else class="badge bg-success">In Stock</span>
                                    </td>
                                    <td>
                                        <a href="#"  @click="stockInModal(product)">
                                        <i class="fa fa-plus"></i>
                                        </a>
                                        &emsp;
                                        <a href="#" @click="editModal(product)">
                                        <i class="fa fa-edit"></i>
                                        </a>
                                        &emsp;
                                         <a href="#" @click="deleteProduct(product.product_id)">
                                        <i class="fa fa-trash"></i>
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
                    <h5 class="modal-title" id="exampleModalLabel">Add new Product</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form @submit.prevent ="editMode ? updateProduct() : addProduct()">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Product Name</label>
                        <input v-model="product_name" required type="text" class="form-control" id="product" placeholder="Product Name">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">Barcode</label>
                        <input v-model="barcode" type="text" required class="form-control" id="barcode" placeholder="Barcode">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">Price</label>
                        <input v-model="price" type="text" required class="form-control" id="price" placeholder="In Php">
                    </div>
                      <div class="form-group">
                        <label for="exampleInputEmail1">Supplier</label>
                        <v-select :options="suppliers"  :required="!supplier" v-model="supplier" name="supplier"></v-select>
                    </div>
                     <div class="form-group">
                        <label for="exampleInputEmail1">Category</label>
                        <v-select :options="categories"  :required="!category" v-model="category"></v-select>
                    </div>
                     <div class="form-group">
                        <label for="">Picture</label>
                        <input @change="onFileChange" type="file" id="image" name="image"  class="form-control">
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
         <!-- Stock In -->
         <div class="modal fade" id="exampleModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Stocks In</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form @submit.prevent="stocksIn">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Input Quantity</label>
                        <input v-model="stocks" required type="number" class="form-control" placeholder="Quantity">
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
         <!-- Stock in -->
    </section>
</template>

<script>
import vSelect from 'vue-select'
Vue.component('v-select', vSelect)
import 'vue-select/dist/vue-select.css'
    export default {
        data(){
            return{
                editMode: false,
                suppliers:[],
                products:[],
                product_name:'',
                supplier:'',
                category:'',
                barcode:'',
                categories:['Phone','Laptop','Computers','Appliances'],
                category:'',
                price:'',
                errorInputs:[],
                stocks:'',
                product_id:'',
                image:'',

            }
        },
        methods:{
            onFileChange(e) {
                let file = e.target.files[0];
                console.log(file);
                var reader = new FileReader();
                reader.onloadend = (file)=>{
                    this.image = reader.result
                }
                reader.readAsDataURL(file);
            },
            getSuppliers(){
                axios.get('/getSuppliersCombo')
                    .then((res)=>{
                        this.suppliers = res.data

                    }).catch((err)=>{
                        console.log(err)
                    })
            },
            addProduct(){
                axios.post('/addProduct',{
                    product_name: this.product_name,
                    quantity: 0,
                    supplier: this.supplier.id,
                    category: this.category,
                    barcode: this.barcode,
                    price: this.price,
                    image: this.image,
                }).then((res)=>{
                    this.clearValues()
                    $('#exampleModal').modal('hide')
                    toastr.success('Product Added!')
                    this.getProducts() 
                }).catch((res)=>{
                    toastr.error(res.message+' Check your Inputs')
                })
            },
            getProducts(){
                axios.get('/getProducts')
                    .then((res)=>{
                        this.products = res.data
                        this.myTable()
                    }).catch((err)=>{
                        console.log(err)
                    })
            },
            myTable(){
               $(document).ready( function () {
                     $('#myTable').DataTable();
                  });
            },
            clearValues(){
                this.product_name=''
                this.supplier=''
                this.category= ''
                this.barcode=''
                this.quantity=''
                this.price=''
            },
            stockInModal(product){
                console.log(product.product_id)
                this.product_id = product.product_id
                $('#exampleModal1').modal('show')
            },
            stocksIn(){
                axios.put('/stockIn/?product_id='+this.product_id,{
                    stocks: this.stocks
                })
                    .then((res)=>{
                        toastr.success('Stocks Added!')
                        this.product_id=''
                        $('#exampleModal1').modal('hide')
                        this.getProducts()
                    }).catch((err)=>{
                        console.log(err)
                        toastr.error('Something went wrong')
                })
            },
            deleteProduct(product_id){
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
                        axios.delete('/deleteProduct/?product_id='+product_id)
                        swal.fire(
                        'Deleted!',
                        'Product Deleted',
                        'success'
                        )
                        this.getProducts()
                    }
                })
            },
            editModal(product){
                this.editMode = true
                this.product_id = product.product_id
                this.product_name = product.product_name
                this.category = product.category
                this.price = product.price
                this.barcode = product.barcode
                this.image = product.image
                // this.supplier = product.supplier_id
                $('#exampleModal').modal('show')
            },
            updateProduct(){
                axios.put('/updateProduct/?product_id=' +this.product_id,{
                    product_name: this.product_name,
                    supplier: this.supplier.id,
                    category: this.category,
                    barcode: this.barcode,
                    price: this.price,
                    image: this.image
                })
                    .then((res)=>{
                        this.clearValues()
                        this.editMode = false
                        $('#exampleModal').modal('hide')
                        toastr.success('Product Updated!')
                        this.getProducts()
                    })
            },
            addModal(){
                this.editMode = false
                this.clearValues()
                $('#exampleModal').modal('show')
            }
        },
        mounted() {
            console.log('Component mounted.')
        },
        created(){
            this.getProducts()
            this.getSuppliers()
            window.Echo.channel('Products').listen('ProductsEvent',(e)=>{
                this.getProducts() 
            })
        }
    }
</script>
