@extends ('index')
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 
<title>Ganti Bahasa</title>
 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
<body style="background-color:#f0f8ff">

@section('konten')

<section class="jumbotron text-center">
        <div class="container">
          <h1 class="jumbotron-heading">Choose Language Do you Want to Use?</h1>
            <p>(For now, just in Tentang/About/Yaku Page)</p>
            <a href="/tentang" class="btn btn-danger my-1">Indonesia</a>
            <a href="/en/about" class="btn btn-primary my-1">English</a>
            <a href="/jp/yaku" class="btn btn-warning my-1">Japanese</a>
        </div>
</section>

@endsection

</body>
</html>