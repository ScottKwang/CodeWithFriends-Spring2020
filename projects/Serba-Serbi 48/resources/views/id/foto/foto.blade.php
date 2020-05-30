@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Daftar Foto 48')

<title>Daftar Foto 48(Utama)</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<p>Cari Foto Member :</p>
<form action="/id/foto/cari" method="GET">
	<input type="text" name="cari" placeholder="Cari Grup/Nama Member Disini" value="{{ old('cari') }}">
	<input type="submit" value="CARI">
</form>
<div class="container">
            <div class="card mt-5">
                <div class="card-header text-center">
                <p>Untuk Mencari Foto Member Keinginan Anda Cari di Kotak Pencarian Disamping Kiri</p>
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-hover table-striped">
                        <tbody>
                            @foreach($foto48s as $datas)
                            <tr>
                                <td><img src="{{ $datas->gambar}}" style="width:75x;height:100px;"><br>Keterangan : {{ $datas->keterangan }}</strong></br>Jenis : {{ $datas->jenis}}</br>Foto tentang :{{ $datas->tag_namagrup }}</br></td>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
Halaman : {{ $foto48s->currentPage() }} <br/>
Jumlah Data : {{ $foto48s->total() }} <br/>
Data Per Halaman : {{ $foto48s->perPage() }} <br/>
 
 
{{ $foto48s->links() }}

@endsection
</body>
</html>