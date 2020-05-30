@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Kolom Menambah Daftar Member 48 Group')

<title>Daftar Member 48(Tambah)</title>
 
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
                    Silahkan Tambah Daftar Member Di Bawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/member_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
                    <form method="post" action="/id/member/store">

                        {{ csrf_field() }}

                        <div class="form-group">
                            <label>Link Gambar Member</label>
                            <input type="text" name="gambar" class="form-control" placeholder="Masukkan Link Saja (Bukan File Gambar)">

                            @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Username Member</label>
                            <input type="text" name="username" class="form-control" placeholder="Masukkan Usernamme Member Disini">

                             @if($errors->has('username'))
                                <div class="text-danger">
                                    {{ $errors->first('username')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Nama Member</label>
                            <input type="text" name="nama" class="form-control" placeholder="Masukkan Nama Member Disini">

                             @if($errors->has('nama'))
                                <div class="text-danger">
                                    {{ $errors->first('nama')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Grup Member</label>
                            <input type="text" name="grup" class="form-control" placeholder="Masukkan Asal Sister Grup Member Disini">

                            @if($errors->has('grup'))
                                <div class="text-danger">
                                    {{ $errors->first('grup')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Tim Member</label>
                            <input type="text" name="tim" class="form-control" placeholder="Bila Double Tim Tuiis Dengan Format 'A/B', dan Bila Sudah Grad Tulis 'Grad'">

                             @if($errors->has('tim'))
                                <div class="text-danger">
                                    {{ $errors->first('tim')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Generasi Member Di Grup nya</label>
                            <input type="text" name="generasi" class="form-control" placeholder="Masukkan Generasi Member Disini">

                            @if($errors->has('generasi'))
                                <div class="text-danger">
                                    {{ $errors->first('generasi')}}
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