@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Daftar Member 48(Edit)')

<title>Daftar Member 48(Edit)</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<p>Cari Daftar Member :</p>
<form action="/id/member/cari" method="GET">
	<input type="text" name="cari" placeholder="Cari Member Disini" value="{{ old('cari') }}">
	<input type="submit" value="CARI">
</form>
<div class="container">
            <div class="card mt-5">
                <div class="card-header text-center">
                    <a>Untuk Mencari Member Keinginan Anda Cari di Kotak Pencarian Disamping Kanan</a>
                </div>
                <div class="card-body">
                    <a href="/id/member/tambah" class="btn btn-primary">Input Member 48 Baru</a>
                    <br/>
                    <br/>
                    <table class="table table-bordered table-hover table-striped">
                        <tbody>
                            @foreach($member48s as $datas)
                            <tr>
                                <td><img src="{{ $datas->gambar}}" style="width:75x;height:100px;"></td>
                                <td>{{ $datas->nama }}</td>
                                <td>{{ $datas->grup }}</td>
                                <td>{{ $datas->tim }}</td>
                                <td>{{ $datas->generasi }}</td>
                                <td><a href ="/id/member/edit/{{$datas->username}}" class ="btn btn-primary">Edit Disini</a></td>
                                </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
@endsection
</body>
</html>