<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
 
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
  
</head>
<body style="background-color:#f0f8ff">

<nav class="navbar navbar-expand-md navbar-light" style="background-color:yellow">
        <a class="navbar-brand" href="/">Serba-Serbi 48</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/id/member">Cari Member</a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="/id/lirik">Lirik Lagu</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/id/foto">Kumpulan Foto</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/id/info">Info Menarik</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/id/corona">Info Covid-19</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/home">Login</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/gantibahasa">Tentang</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/gantibahasa">Language</a>
          </li>
        </ul>
          <ul class="navbar-nav">
        <li class="nav-item text-nowrap">
          <a class="nav-link">{{ date('D d-m-y H:i') }}</a>
        </li>
        </ul>
        </div>
      </nav>

      <h3 class = "text-center"> @yield ('judul_halaman')</h3>

      @yield('konten')
  </body>
</html>