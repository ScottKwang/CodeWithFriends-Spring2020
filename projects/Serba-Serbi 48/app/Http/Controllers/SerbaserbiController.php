<?php
 
namespace App\Http\Controllers;
 
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

 
class SerbaserbiController extends Controller
{
   
    public function index ()
    {
        return view('index');
    }

    public function gantibahasa ()
    {
        return view('gantibahasa');
    }

    public function tentang ()
    {
        return view('tentang');
    }

    public function about ()
    {
        return view('en.about');
    }
    
    public function yaku()
    {
        return view('jp.yaku');
    }
}