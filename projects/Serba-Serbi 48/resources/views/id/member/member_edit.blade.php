@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Kolom Edit Member')

<title>Daftar Member 48(Kolom Edit)</title>
 
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
                Silahkan Edit Data <strong>{{$member48s->nama}}</strong> Di Bawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/member_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
 
                    <form method="post" action="/id/member/update/{{ $member48s ->username}}">
 
                        {{ csrf_field() }}
                        {{ method_field('PUT') }}
 
                        <div class="form-group">
                            <label>Link Gambar Member</label>
                            <textarea name="gambar" class="form-control" placeholder="Masukkan Link Saja (Bukan File Gambar)">{{$member48s->gambar}}</textarea>

                            @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Username Member</label>
                            <textarea name="username" class="form-control" placeholder="Masukkan Username Member Disini">{{$member48s->username}}</textarea>

                             @if($errors->has('username'))
                                <div class="text-danger">
                                    {{ $errors->first('username')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Nama Member</label>
                            <textarea name="nama" class="form-control" placeholder="Masukkan Nama Member Disini">{{$member48s->nama}}</textarea>

                             @if($errors->has('nama'))
                                <div class="text-danger">
                                    {{ $errors->first('nama')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Grup Member</label>
                            <textarea name="grup" class="form-control" placeholder="Masukkan Asal Sister Grup Member Disini">{{$member48s->grup}}</textarea>

                            @if($errors->has('grup'))
                                <div class="text-danger">
                                    {{ $errors->first('grup')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Tim Member</label>
                            <textarea name="tim" class="form-control" placeholder="Bila Double Tim Tuiis Dengan Format 'A/B', dan Bila Sudah Grad Tulis 'Grad'">{{$member48s->tim}}</textarea>

                             @if($errors->has('tim'))
                                <div class="text-danger">
                                    {{ $errors->first('tim')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Asal Generasi Member Di Grup nya</label>
                            <textarea name="generasi" class="form-control" placeholder="Masukkan Generasi Member Disini">{{$member48s->generasi}}</textarea>

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