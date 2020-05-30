@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Atsuko Maeda')

<title>Atsuko Maeda Biodata</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
@foreach($member48s as $datas)
<img src="{{ $datas->gambar}}"></br>
<b>{{ $datas->nama }}</b></br>
{{ $datas->grup }}</br>
{{ $datas->tim }}<br>
{{ $datas->generasi }}
@endforeach
@endsection
</body>
</html>