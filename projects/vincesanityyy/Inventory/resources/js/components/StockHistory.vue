<template>
 <section class="content">
     <div class="content-header">
         <div class="container-fluid">
             <div class="row mb-2">
                 <div class="col-sm-6">
                     <h1 class="m-0 text-dard">Stock History</h1>
                 </div>
                 <div class="col-sm-6">
                      <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Stock History</li>
                        </ol>
                 </div>
             </div>
         </div>
     </div>
     <div class="row">
         <div class="col-md-12">
             <div class="card card-primary card-outline">
                 <div class="card-header">
                     <h3 class="card-title">Stock History</h3>
                 </div>
                 <div class="card-body">
                     <table id="myTable" class="table table-bordered table-striped">
                         <thead>
                             <tr>
                                 <th>History Id</th>
                                 <th>Product Name</th>
                                 <th>Supplier</th>
                                 <th>No. of Stocks Added</th>
                                 <th>Added By</th>
                                 <th>Date Added</th>
                             </tr>
                         </thead>
                         <tbody>
                             <tr v-for="history in histories" :key="history.history_id">
                                 <td>{{history.history_id}}</td>
                                 <td>{{history.stocks.product_name}}</td>
                                 <td>{{history.stocks.supplier.supplier_name}}</td>
                                 <td>{{history.stock_added}}</td>
                                 <td>{{history.users.name}}</td>
                                 <td>{{history.date_added | moment("dddd, MMMM Do YYYY, h:mm a")}}</td>
                             </tr>
                         </tbody>
                     </table>
                 </div>
             </div>
         </div>
     </div>
 </section>
</template>

<script>
    export default {
        data(){
            return{
                histories:[]
            }
        },
        mounted() {
            console.log('Component mounted.')
        },
        created(){
            this.getHistory()
        },
        methods:{
            getHistory(){
                axios.get('/getHistory')
                    .then((res)=>{
                        this.histories = res.data
                        this.myTable()
                    })
            },
            myTable(){
               $(document).ready( function () {
                     $('#myTable').DataTable();
                  });
            },
        }
    }
</script>
