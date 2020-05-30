@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
@section ('judul_halaman', 'Kolom Edit Info Grup 48')

<title>Info Grup 48(Kolom Edit)</title>
  
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
                Edit Info <strong>{{$infogrup48s->grup}}</strong>
                </div>
                <div class="card-body">
                    <a href="/id/infogrup48_edit" class="btn btn-primary">Kembali</a>
                    <br/>
                    <br/>
                    
                    <form method="post" action="/id/infogrup48/update/{{ $infogrup48s ->grup }}">
 
                        {{ csrf_field() }}
                        {{ method_field('PUT') }}
 
                        <div class="form-group">
                            <label>Nama Grup 48</label>
                            <textarea name="grup" class="form-control" placeholder="Masukkan Nama Grup 48">{{$infogrup48s->grup}}</textarea>

                            @if($errors->has('grup'))
                                <div class="text-danger">
                                    {{ $errors->first('grup')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
					 <label>Keterangan gambar</label>
                            <textarea name="gambar" class="form-control" placeholder="Masukkan Keterangan gambar Disini">{{$infogrup48s->gambar}}</textarea>

                             @if($errors->has('gambar'))
                                <div class="text-danger">
                                    {{ $errors->first('gambar')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
					 <label>Keterangan penjelasan</label>
                            <textarea name="penjelasan" class="form-control" placeholder="Masukkan Keterangan penjelasan Disini">{{$infogrup48s->penjelasan}}</textarea>

                             @if($errors->has('penjelasan'))
                                <div class="text-danger">
                                    {{ $errors->first('penjelasan')}}
                                </div>
                            @endif

                        </div>

                        <div class="form-group">
					 <label>Keterangan single</label>
                            <textarea name="single" class="form-control" placeholder="Masukkan Keterangan single Disini">{{$infogrup48s->single}}</textarea>

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