@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Silahkan Edit Data Lagu Di Bawah Ini')

<title>Lirik Lagu 48 Group</title>
 
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
                Lirik Lagu <strong>{{$lirik48s->judul}} - {{$lirik48s->penyanyi}}</strong>
                </div>
                <div class="card-body">
                    <a href="/id/lirik_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
                    <form method="post" action="/id/lirik/update/{{ $lirik48s ->judul_penyanyi }}">
 
                        {{ csrf_field() }}
                        {{ method_field('PUT') }}
 
                        <div class="form-group">
                            <label>Judul Lagu</label>
                            <textarea name="judul" class="form-control" placeholder="Masukkan Judul Lagu Disini">{{$lirik48s->judul}}</textarea>

                            @if($errors->has('judul'))
                                <div class="text-danger">
                                    {{ $errors->first('judul')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Kode Unik Lirik</label>
                            <textarea name="judul_penyanyi" class="form-control" placeholder="Masukkan Kode Unik dengan Format (judul_penyanyi) Ex:aitakatta_akb48">{{$lirik48s->judul_penyanyi}}</textarea>

                             @if($errors->has('judul_penyanyi'))
                                <div class="text-danger">
                                    {{ $errors->first('judul_penyanyi')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Nama Penyanyi</label>
                            <textarea name="penyanyi" class="form-control" placeholder="Masukkan Nama Penyanyi Disini">{{$lirik48s->penyanyi}}</textarea>

                             @if($errors->has('penyanyi'))
                                <div class="text-danger">
                                    {{ $errors->first('penyanyi')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Isi Lirik Lagu</label>
                            <textarea name="isi_lirik" class="form-control" placeholder="Masukkan Isi Lirik Lagu Disini">{{$lirik48s->isi_lirik}}</textarea>

                            @if($errors->has('isi_lirik'))
                                <div class="text-danger">
                                    {{ $errors->first('isi_lirik')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Link Youtube</label>
                            <textarea name="link_yt" class="form-control" placeholder="Masukkan Link Youtube Disini">{{$lirik48s->link_yt}}</textarea>

                             @if($errors->has('link_yt'))
                                <div class="text-danger">
                                    {{ $errors->first('link_yt')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Link Gambar Cuplik dari Youtube</label>
                            <textarea name="gambar" class="form-control" placeholder="Masukkan Link Website Gambar Cuplik nya saja">{{$lirik48s->gambar}}</textarea>

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