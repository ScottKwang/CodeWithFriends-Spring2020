@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<body style="background-color:#f0f8ff">
@section('konten')
<section class="jumbotron text-center">
        <div class="container">
          <h1 class="jumbotron-heading">Detil Informasi Jumlah Korban Corona Covid-19</h1>
          <p class="lead text-muted">Tekan tombol ini untuk informasi lebih lanjut</p>
            <a href="https://corona.jakarta.go.id/" class="btn btn-danger my-1">DKI Jakarta</a>
            <a href="https://pikobar.jabarprov.go.id/" class="btn btn-primary my-1">Jawa Barat</a>
            <a href="http://checkupcovid19.jatimprov.go.id/" class="btn btn-warning my-1">Jawa Timur</a>
            <a href="https://corona.jatengprov.go.id/" class="btn btn-info my-1">Jawa Tengah</a>
            <br>
            <a href="https://covid19.sulselprov.go.id/" class="btn btn-danger my-1">Sulawesi Selatan</a>
            <a href="https://infocorona.bantenprov.go.id/" class="btn btn-primary my-1">Banten</a>
            <a href="https://infocorona.baliprov.go.id/" class="btn btn-warning my-1">Bali</a>
            <a href="https://corona.ntbprov.go.id/" class="btn btn-info my-1">Nusa Tenggara Barat</a>
            <br>
            <a href="http://corona.kalselprov.go.id/" class="btn btn-danger my-1">Kalimantan Selatan</a>
            <a href="http://corona.sumselprov.go.id/" class="btn btn-primary my-1">Sumatera Selatan</a>
            <a href="https://corona.sumbarprov.go.id/" class="btn btn-warning my-1">Sumatera Barat</a>
            <a href="http://corona.kalteng.go.id/" class="btn btn-info my-1">Kalimantan Tengah</a>
            <br>
            <a href="http://covid19.kaltimprov.go.id/" class="btn btn-danger my-1">Kalimantan Timur</a>
            <a href="http://corona.kepriprov.go.id/" class="btn btn-primary my-1">Kepulauan Riau</a>
            <a href="http://humas.kaltaraprov.go.id/" class="btn btn-warning my-1">Kalimantan Utara</a>
            <a href="http://covid19.kalbarprov.go.id/" class="btn btn-info my-1">Kalimantan Barat</a>
            <br>
            <a href="http://dinkes.sultraprov.go.id/" class="btn btn-danger my-1">Sulawesi Tenggara</a>
            <a href="http://dinkes.lampungprov.go.id/covid19" class="btn btn-primary my-1">Lampung</a>
            <a href="https://corona.riau.go.id/" class="btn btn-warning my-1">Riau</a>
            <a href="http://corona.sulutprov.go.id/" class="btn btn-info my-1">Sulawesi Utara</a>
            <br>
            <a href="http://dinkes.sulbarprov.go.id/" class="btn btn-danger my-1">Sulawesi Barat</a>
            <a href="http://dinkes.sultengprov.go.id/category/covid-19" class="btn btn-primary my-1">Sulawesi Tengah</a>
            <a href="http://covid19.jambikota.go.id//" class="btn btn-warning my-1">Jambi</a>
            <a href="http://corona.malukuprov.go.id/" class="btn btn-info my-1">Maluku</a>
            <br>
            <a href="https://malutprov.go.id/tag/satgas-covid" class="btn btn-danger my-1">Maluku Utara</a>
            <a href="http://dinkes.gorontaloprov.go.id/" class="btn btn-primary my-1">Gorontalo</a>
            <a href="http://covid19.babelprov.go.id/" class="btn btn-warning my-1">Bangka Belitung</a>
            <a href="https://covid19.acehprov.go.id/" class="btn btn-info my-1">Aceh</a>
            <br>
          </p>
        </div>
      </section>
@endsection 
</body>
</html>