@extends('beautymail::templates.widgets')

@section('content')

	@include('beautymail::templates.widgets.newfeatureStart')

		<h4 class="secondary"><strong>Hello motherfucker. Here is your confirmation code:</strong></h4>
		<p><strong>{{$code}}</strong></p>

	@include('beautymail::templates.widgets.newfeatureEnd')

@stop