require('./bootstrap');

window.Vue = require('vue');
import Vue from 'vue'
import VueRouter from 'vue-router'
import toastr from 'admin-lte/plugins/toastr/toastr.min.js'
import swal from 'admin-lte/plugins/sweetalert2/sweetalert2.min.js'
import VueFusionCharts from 'vue-fusioncharts';

import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

Vue.use(VueRouter)
Vue.use(toastr)
Vue.use(Loading)
Vue.use(require('vue-moment'));

window.swal = swal;
window.toastr = require('toastr')

Vue.component('example-component', require('./components/ExampleComponent.vue').default);
Vue.component('navbar-notifications', require('./components/NavbarNotifications.vue').default);
Vue.component('verify', require('./components/Verify.vue').default);
Vue.component('login',require('./components/Login.vue').default);
Vue.component('register',require('./components/Register.vue').default);

const routes = [
    { path: '/products', component: require('./components/Products.vue').default },
    { path: '/history', component: require('./components/StockHistory.vue').default },
    { path: '/suppliers', component: require('./components/Suppliers.vue').default },
    { path: '/reports', component: require('./components/Reports.vue').default },
]


const router = new VueRouter({
    mode: 'history',
    routes
})

const app = new Vue({
    el: '#app',
    router,
});
