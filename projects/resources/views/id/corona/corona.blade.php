@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<body style="background-color:#f0f8ff">
@section('konten')
<section class="jumbotron text-center">
        <div class="container">
          <h1 class="jumbotron-heading">Info Jumlah Korban Corona Covid-19</h1>
          <p class="lead text-muted">Tekan tombol ini untuk informasi lebih lanjut</p>
            <a href="/id/corona/indonesia" class="btn btn-danger my-1">Indonesia</a>
            <a href="/id/corona/provinsi" class="btn btn-primary my-1">Provinsi di Indonesia</a>
            <a href="/id/corona/dunia" class="btn btn-warning">Dunia</a>
          </p>
        </div>
      </section>
@endsection 
</body>
</html>