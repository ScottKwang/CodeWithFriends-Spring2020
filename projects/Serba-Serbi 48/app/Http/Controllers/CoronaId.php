<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;

class CoronaId extends Controller
{
    /* Data Corona */

    
    public function corona ()
    {
        return view('id.corona.corona');
    }

    
    public function detilprovinsi ()
    {
        return view('id.corona.detilprovinsi');
    }

    public function indonesia()
    {
        $response = Http::get('https://api.kawalcorona.com/indonesia');
        $data = $response->json();
//        dd($data);
        return view('id.corona.indonesia',compact('data'));
    }

    public function provinsi()
    {
        $response = Http::get('https://api.kawalcorona.com/indonesia/provinsi');
        $data = $response->json();
//        dd($data);
        return view('id.corona.provinsi',compact('data'));
    }

    public function dunia()
    {
        $response = Http::get('https://api.kawalcorona.com');
        $data = $response->json();
//        dd($data);
        return view('id.corona.dunia',compact('data'));
    }
}
