@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
 @section('judul_halaman', 'Info Covid-19 di Provinsi')

<title>Info Covid-19 di Provinsi di Indonesia</title>
  
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" >
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" ></script>
</head>
<body style="background-color:#f0f8ff">
@section ('konten')
<br>
<p class = "text-center">Untuk Informasi Detil dari Beberapa Provinsi, <a href="/id/corona/detilprovinsi">Klik Disini</a></p>
<div class="container">
            <table class="table">
                    <thead class="thead-dark">
                      <tr>
                        <th scope="col">No</th>
                        <th scope="col">Provinsi</th>
                        <th scope="col">Positif</th>
                        <th scope="col">Sembuh</th>
                        <th scope="col">Meninggal</th>
                      </tr>
                    </thead>
                    <tbody>
                        @php
                            $no = 0;
                        @endphp
                    @foreach ($data as $datas)   
                        @php
                            $no++;
                        @endphp
                      <tr>
                        <th scope="row">{{ $no }}</th>
                        <td>{{ $datas['attributes']['Provinsi'] }}</td>
                        <td>{{ $datas['attributes']['Kasus_Posi'] }}</td>
                        <td>{{ $datas['attributes']['Kasus_Semb'] }}</td>
                        <td>{{ $datas['attributes']['Kasus_Meni'] }}</td>
                      </tr>
                    @endforeach 
                    </tbody>
                  </table>
    </div>
<nav class="navbar navbar-expand-md navbar-white" style="background-color:red">
<div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link btn btn-danger" href="/id/corona/indonesia">Lihat di Indonesia?</a>
          </li>
        </ul>
        <ul class="navbar-nav px-3">
          <li class="nav-item text-nowrap">
            <a class="nav-link btn btn-warning" href="/id/corona/dunia">Lihat di Dunia?</a>
          </li>
        </ul>
</div>
</nav>
@endsection
</body>
</html>