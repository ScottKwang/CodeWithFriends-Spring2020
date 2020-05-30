@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Daftar Edit Info Grup 48')

<title>Info Grup 48(Daftar Edit)</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<div class="container">
<a href="/id/infogrup48/tambah" class="btn btn-primary">Input Lirik Lagu 48 Baru</a>
<br/>
<br/>
@foreach($infogrup48s as $datas)
<div class="row">
<div class="col-lg-3">
<a href ="/id/infogrup48/edit/{{$datas->grup}}" class ="btn btn-link">{{$datas->grup}}</a>
</div></div>
@endforeach
</div>
@endsection
</body>
</html>
