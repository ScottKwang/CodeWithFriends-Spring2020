@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    @section ('judul_halaman', 'Kolom Edit Info Menarik 48 Group')

<title>Info 48(Edit)</title>
 
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
                Silahkan Edit Info <strong>{{$info48s->judul}}</strong> Di Bawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/info_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
 
                    <form method="post" action="/id/info/update/{{ $info48s ->id }}">
 
                        {{ csrf_field() }}
                        {{ method_field('PUT') }}
 
                        <div class="form-group">
                            <label>Link Gambar Member</label>
                            <textarea name="gambar" class="form-control" placeholder="Masukkan Link Saja (Bukan File Gambar)">{{$info48s->gambar}}</textarea>

                            @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Judul Info Menarik</label>
                            <textarea name="judul" class="form-control" placeholder="Masukkan Judul Info Disini">{{$info48s->judul}}</textarea>

                             @if($errors->has('judul'))
                                <div class="text-danger">
                                    {{ $errors->first('judul')}}
                                </div>
                            @endif


                        </div>

                        <div class="form-group">
                            <label>Kalimat Rintisan Info Menarik</label>
                            <textarea name="rintisan" class="form-control" placeholder="Masukkan Kalimat Rintisan Info Disini">{{$info48s->rintisan}}</textarea>

                            @if($errors->has('rintisan'))
                                <div class="text-danger">
                                    {{ $errors->first('rintisan')}}
                                </div>
                            @endif


                        </div>

                        <div class="form-group">
                            <label>Tag Nama dan Grup Pada Info</label>
                            <textarea name="tag_namagrup" class="form-control" placeholder="Masukkan Tag Nama dan Grup dengan format nama & grup panggilan/lengkap, dan bila lebih dari satu beri titik sebagai pemisah">{{$info48s->tag_namagrup}}</textarea>

                             @if($errors->has('tag_namagrup'))
                                <div class="text-danger">
                                    {{ $errors->first('tag_namagrup')}}
                                </div>
                            @endif


                        </div>

                        <div class="form-group">
                            <label>Link Selengkapnya</label>
                            <textarea name="selengkapnya" class="form-control" placeholder="Masukkan Link Selengkapnya">{{$info48s->selengkapnya}}</textarea>

                            @if($errors->has('selengkapnya'))
                                <div class="text-danger">
                                    {{ $errors->first('selengkapnya')}}
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