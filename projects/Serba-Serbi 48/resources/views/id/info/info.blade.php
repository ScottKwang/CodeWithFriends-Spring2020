@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Info Menarik 48')

<title>Info 48(Utama)</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<p>Cari Info Menarik :</p>
<form action="/id/info/cari" method="GET">
	<input type="text" name="cari" placeholder="Cari Grup/Nama/Fakta Unik Disini" value="{{ old('cari') }}">
	<input type="submit" value="CARI">
</form>
<div class="container">
            <div class="card mt-5">
                <div class="card-header text-center">
                <p>Untuk Mencari Info Menarik Member Keinginan Anda Cari di Kotak Pencarian Disamping Kiri</p>
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-hover table-striped">
                        <tbody>
                            @foreach($info48s as $datas)
                            <tr>
                                <td><img src="{{ $datas->gambar}}" style="width:75x;height:100px;"></td>
                                <td><strong>{{ $datas->judul }}</strong></br>
                                {{ $datas->rintisan}}</br>
                                Info tentang :{{ $datas->tag_namagrup }}</td>
                                <td><a href ="{{$datas->selengkapnya}}" class ="btn btn-primary">Selengkapnya</a></td>
                                </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
Halaman : {{ $info48s->currentPage() }} <br/>
Jumlah Data : {{ $info48s->total() }} <br/>
Data Per Halaman : {{ $info48s->perPage() }} <br/>
 
 
{{ $info48s->links() }}

@endsection
</body>
</html>