@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Lirik Lagu Yuuhi Wo Miteiruka - AKB48')

<title>Lirik Lagu</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<div class="text-center">
@foreach($lirik48s as $datas)
Judul : <strong>{{ $datas->judul}}</strong></br>
Oleh : <strong>{{ $datas->penyanyi }}</strong></br></br>
{{ $datas->isi_lirik }}</br></br>
<img src="{{ $datas->gambar}}" style="width:250x;height:500px;"></br></br>
<a href ="{{$datas->link_yt}}" class ="btn btn-primary">Klik Ini Untuk Akses Via Youtube</a>
@endforeach
</div>
@endsection
</body>
</html>