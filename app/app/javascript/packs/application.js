// This file is automatically compiled by Webpack, along with any other files
// present in this directory. You're encouraged to place your actual application logic in
// a relevant structure within app/javascript and only use these pack files to reference
// that code so it'll be compiled.

require("trix")
require("@rails/actiontext")
require("@rails/ujs").start()
require("turbolinks").start()
require("@rails/activestorage").start()
require("channels")
require('bootstrap')
require("moment")
require('jquery')
import flatpickr from 'flatpickr'
import "flatpickr/dist/flatpickr.min.css";


$(document).on('turbolinks:load', function() {
  flatpickr('.datepicker', {
    enableTime: true,
    minDate: new Date()
  });

  setTimeout(function(){
    $('#flash').remove();
  }, 5000);
})


// Uncomment to copy all static images under ../images to the output folder and reference
// them with the image_pack_tag helper in views (e.g <%= image_pack_tag 'rails.png' %>)
// or the `imagePath` JavaScript helper below.
//
// const images = require.context('../images', true)
// const imagePath = (name) => images(name, true)