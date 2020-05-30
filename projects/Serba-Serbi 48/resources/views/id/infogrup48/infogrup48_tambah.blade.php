@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

@section ('judul_halaman', 'Kolom Menambah Info Grup 48')

<title>Info Grup 48(Kolom Tambah)</title>
 
 
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
                    Silahkan Tambah Info Grup Dibawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/infogrup48_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
                    <form method="post" action="/id/infogrup48/store">

                        {{ csrf_field() }}

                        
                        <div class="form-group">
                            <label>Nama Grup 48</label>
                            <input type="text" name="grup" class="form-control" placeholder="Masukkan Nama Grup 48">

                            @if($errors->has('grup'))
                                <div class="text-danger">
                                    {{ $errors->first('grup')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Keterangan gambar</label>
                            <input type="text" name="gambar" class="form-control" placeholder="Masukkan Keterangan gambar Disini">

                             @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Keterangan penjelasan</label>
                            <input type="text" name="penjelasan" class="form-control" placeholder="Masukkan Keterangan penjelasan Disini">

                             @if($errors->has('penjelasan'))
                                <div class="text-danger">
                                    {{ $errors->first('penjelasan')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Keterangan single</label>
                            <input type="text" name="single" class="form-control" placeholder="Masukkan Keterangan single Disini">

                             @if($errors->has('single'))
                                <div class="text-danger">
                                    {{ $errors->first('single')}}
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