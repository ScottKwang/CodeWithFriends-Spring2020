@extends('layouts.app')

@section('content')
<div class="container">
<h1 class="jumbotron-heading text-center">Selamat Datang Di Kolom Admin Serba-Serbi 48</h1>
<p class="lead text-muted">Di Kolom Ini Anda Bisa Terhubung Untuk :</p>

- Membuat dan Mengedit Item Member (Utama dan Detil)</br>
- Membuat dan Mengedit Item Lirik Lagu (Utama dan Detil)</br>
- Mencuplik dan Mengedit Info Menarik Member (Utama dan Detil)</br>
- Membuat dan Mengedit Item Foto Member (Utama dan Detil)</br>
- Mengedit Info Sister dan Rival Grup 48

</div>
</br>
</br>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">Klik Tombol Dibawah Ini Untuk Melanjutkan</div>
                <div class="card-body">
                    @if (session('status'))
                        <div class="alert alert-success" role="alert">
                            {{ session('status') }}
                        </div>
                    @endif
            <a href="/id/member_edit" class="btn btn-danger my-1">Edit Member</a>
            <a href="/id/lirik_edit" class="btn btn-primary my-1">Edit Lirik</a>
            <a href="/id/info_edit" class="btn btn-warning my-1">Edit Info</a>
            <a href="/id/foto_edit" class="btn btn-info my-1">Edit Foto</a>
            <a href="/id/infogrup48_edit" class="btn btn-info my-1">Edit Info Grup 48</a>
                </div>
            </div>
        </div>
    </div>
</div>       
@endsection

