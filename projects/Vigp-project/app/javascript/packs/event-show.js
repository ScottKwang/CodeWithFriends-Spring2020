$(document).on('turbolinks:load', function() {
  movieId = $('#movieId').text()
  console.log(movieId);
  $.ajax({
    url: "https://api.themoviedb.org/3/movie/" + movieId,
    type: "get", //send it through get method
    data: {
      api_key: "2b32530982c12bd20d37880a47276b26",
      language: "en-US",
    },
    success: function(response) {
      console.log(response)
      $("#movieImg").attr("src","https://image.tmdb.org/t/p/w500"+ response.poster_path);
      $("#movieId").text(response.title);
      $("#movieDesc").text(response.overview);
    }
  });
})