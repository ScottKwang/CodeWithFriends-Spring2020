window.$ = window.jQuery = require('jquery');
window.Bootstrap = require('bootstrap');
require('bootstrap-tagsinput')
require('select2')
import "bootstrap-tagsinput/dist/bootstrap-tagsinput.css";
import "select2/dist/css/select2.css";

$(document).on('turbolinks:load', function() {
  window.$('.tags').tagsinput({
    focusClass: 'my-focus-class',
  });
  $(".bootstrap-tagsinput").addClass('form-control')

  function formatMovie(movie) {
    console.log(movie);
    if (!movie.id) {
      return movie.text;
    }
    var baseUrl = "https://image.tmdb.org/t/p/w500";
    var $movie = $(
      `<span class="movie-option"><img src="${baseUrl}${movie.img}" class="movie-thumb"/>${movie.text}</span>`
    );
    return $movie;
  };

  $(".movieSearch").select2({
    minimumInputLength: 3,
    templateResult: formatMovie, //this is for append country flag.
    ajax: {
      url: "https://api.themoviedb.org/3/search/movie",
      dataType: 'json',
      type: "get",
      data: function(params) {
        return {
          api_key: "2b32530982c12bd20d37880a47276b26",
          language: "en-US",
          include_adult: false,
          query: params.term
        };
      },
      processResults: function(data) {
        return {
          results: $.map(data.results, function(item) {
            return {
              text: item.title,
              id: item.id,
              img: item.poster_path
            }
          })
        };
      }
    }
  });
})