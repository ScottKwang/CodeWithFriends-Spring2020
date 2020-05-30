<template>
    <section class="conten">
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 class="m-0 text-dark">Reports</h1>
                    </div>
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Reports</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card card-danger card-outline">
                    <div class="card-header">
                        <h3 class="card-title">
                            Graphs
                        </h3>
                    </div>
                    <div class="card-body">
                        <div class="col-xs-12">
                            <div class="card card-primary card-outline">
                                <div class="card-header">
                                    <h4 class="card-title">
                                        Product Count per Supplier
                                    </h4>
                                </div>
                                <div class="card-body">
                                    <div id="supplierPercentageChart">
                                           <fusioncharts :type="supplierPercentageChart.type" 
                                            :width="supplierPercentageChart.width" 
                                            :height="supplierPercentageChart.height" 
                                            :dataFormat="supplierPercentageChart.dataFormat" 
                                            :dataSource="supplierPercentageChart.dataSource ">
                                            </fusioncharts>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                      <div class="card-body">
                        <div class="col-xs-12">
                            <div class="card card-primary card-outline">
                                <div class="card-header">
                                    <h4 class="card-title">
                                        Another Chart wala ko kabalo unsa ibutang
                                    </h4>
                                </div>
                                <div class="card-body">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import Vue from 'vue';
    import VueFusionCharts from 'vue-fusioncharts';
    import FusionCharts from 'fusioncharts';
    import Column2D from 'fusioncharts/fusioncharts.charts';
    import Charts from 'fusioncharts/fusioncharts.charts';
    import excelexport from 'fusioncharts/fusioncharts.excelexport';
    import FusionTheme from 'fusioncharts/themes/fusioncharts.theme.fusion';
  
    Vue.use(VueFusionCharts, FusionCharts, Column2D, FusionTheme, Charts, excelexport);
    export default {
        data(){
            return{
                supplierPercentageChart:{
                    type: 'column2d',
                    renderAt: 'supplier-chart',
                    width:'950',
                    height:'300',
                    dataFormat:'json',
                    dataSource:{
                        chart:{
                            theme:'fusion',
                            exportenabled: '1',
                            exportfilename: 'Supplier Products',
                        },
                        data:[]
                    }
                }
            }
        },
        methods:{
            populateSupplierChart(){
                axios.get('/getProductPercentageBySupplier')
                    .then((res)=>{
                        console.log(res)
                        this.supplierPercentageChart.dataSource.data = []
                        res.data.forEach(val=>{
                            this.supplierPercentageChart.dataSource.data.push(val)
                        })
                    })
            }
        },
        created(){
            this.populateSupplierChart()
        },
        mounted() {
            console.log('Component mounted.')
        }
    }
</script>
