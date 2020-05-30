@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Kolom Menambah Lirik Lagu 48 Group')

<title>Lirik Lagu 48(Tambah)</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">
@section('konten')
<div class="container">
            <div class="card mt-5">
                <div class="card-header text-center">
                    Silahkan Tambah Lirik Lagu Di Bawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/lirik_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
                    <form method="post" action="/id/lirik/store">

                        {{ csrf_field() }}

                        <div class="form-group">
                            <label>Judul Lagu</label>
                            <input type="text" name="judul" class="form-control" placeholder="Masukkan Judul Lagu Disini">

                            @if($errors->has('judul'))
                                <div class="text-danger">
                                    {{ $errors->first('judul')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Kode Unik Lirik</label>
                            <input type="text" name="judul_penyanyi" class="form-control" placeholder="Masukkan Kode Unik dengan Format (judul_penyanyi) Ex:aitakatta_akb48">

                             @if($errors->has('judul_penyanyi'))
                                <div class="text-danger">
                                    {{ $errors->first('judul_penyanyi')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Nama Penyanyi</label>
                            <input type="text" name="penyanyi" class="form-control" placeholder="Masukkan Nama Penyanyi Disini">

                             @if($errors->has('penyanyi'))
                                <div class="text-danger">
                                    {{ $errors->first('penyanyi')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Isi Lirik Lagu</label>
                            <input type="text" name="isi_lirik" class="form-control" placeholder="Masukkan Isi Lirik Lagu Disini">

                            @if($errors->has('isi_lirik'))
                                <div class="text-danger">
                                    {{ $errors->first('isi_lirik')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Link Youtube</label>
                            <input type="text" name="link_yt" class="form-control" placeholder="Masukkan Link Youtube Disini">

                             @if($errors->has('link_yt'))
                                <div class="text-danger">
                                    {{ $errors->first('link_yt')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Link Gambar Cuplik dari Youtube</label>
                            <input type="text" name="gambar" class="form-control" placeholder="Masukkan Link Website Gambar Cuplik nya saja">

                            @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <input type="submit" class="btn btn-success" value="Simpan">
                        </div>

                    </form>

                </div>
            </div>
        </div>
        @endsection
</body>
</html>