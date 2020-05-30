@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    @section ('keterangan_halaman', 'Kolom Edit Foto 48 Group')

<title>Foto 48(Edit)</title>
 
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
                Silahkan Edit Foto Tersebut Di Bawah Ini
                </div>
                <div class="card-body">
                    <a href="/id/foto_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
 
                    <form method="post" action="/id/foto/update/{{ $foto48s ->id }}">
 
                        {{ csrf_field() }}
                        {{ method_field('PUT') }}
 
                        <div class="form-group">
                            <label>Link Gambar Member</label>
                            <textarea name="gambar" class="form-control" placeholder="Masukkan Link Saja (Bukan File Gambar)">{{$foto48s->gambar}}</textarea>

                            @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
                            <label>Keterangan Foto 48</label>
                            <textarea name="keterangan" class="form-control" placeholder="Masukkan Keterangan Disini">{{$foto48s->keterangan}}</textarea>

                             @if($errors->has('keterangan'))
                                <div class="text-danger">
                                    {{ $errors->first('keterangan')}}
                                </div>
                            @endif


                        </div>

                        <div class="form-group">
                            <label>Jenis Foto</label>
                            <textarea name="jenis" class="form-control" placeholder="Masukkan Jenis Foto (PP bila PhotoPack, Instagram bila dari Sosmed IG, dan jenis lainnya">{{$foto48s->jenis}}</textarea>

                            @if($errors->has('jenis'))
                                <div class="text-danger">
                                    {{ $errors->first('jenis')}}
                                </div>
                            @endif


                        </div>

                        <div class="form-group">
                            <label>Tag Nama dan Grup Pada Info</label>
                            <textarea name="tag_namagrup" class="form-control" placeholder="Masukkan Tag Nama dan Grup dengan format nama & grup panggilan/lengkap, dan bila lebih dari satu beri titik sebagai pemisah">{{$foto48s->tag_namagrup}}</textarea>

                             @if($errors->has('tag_namagrup'))
                                <div class="text-danger">
                                    {{ $errors->first('tag_namagrup')}}
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