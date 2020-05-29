<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;
use App\Lirik48;

class LirikId extends Controller
{
    public function lirik()
    {
    	$lirik48s = Lirik48::paginate(10);
    	return view('id.lirik.lirik', ['lirik48s' => $lirik48s]);
	}
	
	public function cari(Request $request)
	{
		// menangkap data pencarian
		$cari = $request->cari;
 
    		// mengambil data dari table pegawai sesuai pencarian data
		$lirik48s = Lirik48::where('judul_penyanyi','like',"%".$cari."%")->paginate();
 
    		// mengirim data pegawai ke view index
		return view('id.lirik.lirik',['lirik48s' => $lirik48s]);
 
	}

    public function lirikedit()
    {
    	$lirik48s = Lirik48::paginate(10);
    	return view('id.lirik.lirikedit', ['lirik48s' => $lirik48s]);
    }

    public function lirik_tambah()
    {
    	return view('id.lirik.lirik_tambah');
    }

    public function lirik_store(Request $request)
    {
    	$this->validate($request,[
    		'judul' => 'required',
    		'penyanyi' => 'required',
			'judul_penyanyi' => 'required',
			'isi_lirik' => 'required',
    		'link_yt' => 'required',
			'gambar' => 'required'
    	]);
 
        Lirik48::create([
    		'judul' => $request->judul,
			'penyanyi' => $request->penyanyi,
			'judul_penyanyi' => $request->judul_penyanyi,
    		'isi_lirik' => $request->isi_lirik,
    		'link_yt' => $request->link_yt,
			'gambar' => $request->gambar
    	]);
 
    	return redirect('/id/lirik');
	}

    public function lirik_edit($judul_penyanyi)
	{
   		$lirik48s = Lirik48::find($judul_penyanyi);
   		return view('id.lirik.lirik_edit', ['lirik48s' => $lirik48s]);
	}

	public function lirik_update($judul_penyanyi, Request $request)
	{
    	$this->validate($request,[
    		'judul' => 'required',
    		'judul_penyanyi' => 'required',
    		'penyanyi' => 'required',
    		'isi_lirik' => 'required',
    		'link_yt' => 'required',
            'gambar' => 'required'
    ]);
 
    	$lirik48s = Lirik48::find($judul_penyanyi);
    	$lirik48s->judul = $request->judul;
    	$lirik48s->judul_penyanyi = $request->judul_penyanyi;
    	$lirik48s->penyanyi = $request->penyanyi;
    	$lirik48s->isi_lirik = $request->isi_lirik;
    	$lirik48s->link_yt = $request->link_yt;
		$lirik48s->gambar = $request->gambar;
    	$lirik48s->save();
    	return redirect('/id/lirik');
	}

	public function hightension_jkt48()
    {
    	$lirik48s = Lirik48::where('judul_penyanyi', '=', 'hightension_jkt48')->get();
    	return view('id.lirik.lirikdetil.hightension_jkt48', ['lirik48s' => $lirik48s]);
	}
	
	public function yuuhiwomiteiruka_akb48()
    {
    	$lirik48s = Lirik48::where('judul_penyanyi', '=', 'yuuhiwomiteiruka_akb48')->get();
    	return view('id.lirik.lirikdetil.yuuhiwomiteiruka_akb48', ['lirik48s' => $lirik48s]);
    }
}
