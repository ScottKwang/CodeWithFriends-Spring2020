window._ = require('lodash');



try {
    window.Popper = require('popper.js').default;
    window.$ = window.jQuery = require('jquery');

    require('bootstrap');
    require('admin-lte');
    require('admin-lte/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js');
    require('admin-lte/plugins/toastr/toastr.min.js');
} catch (e) {}



window.axios = require('axios');

window.axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


let token = document.head.querySelector('meta[name="csrf-token"]');

if (token) {
    window.axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content;
} else {
    console.error('CSRF token not found: https://laravel.com/docs/csrf#csrf-x-csrf-token');
}



import Echo from "laravel-echo"

window.Pusher = require('pusher-js');

window.Echo = new Echo({
    broadcaster: 'pusher',
    key: '644a4ac1988060882370',
    wsHost: window.location.hostname,
    wsPort: 6001,
    disableStats: true,
    enabledTransports: ['ws', 'wss']
});

window.Echo.channel('Inventory').listen('InventoryEvent',(e)=>{
    console.log('hello')
})