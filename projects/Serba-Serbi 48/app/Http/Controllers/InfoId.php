<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;
use App\Info48;

class InfoId extends Controller
{
    public function info()
    {
    	$info48s = Info48::paginate(10);
    	return view('id.info.info', ['info48s' => $info48s]);
	}
	
	public function cari(Request $request)
	{
		// menangkap data pencarian
		$cari = $request->cari;
 
    		// mengambil data dari table pegawai sesuai pencarian data
		$info48s = Info48::where('tag_namagrup','like',"%".$cari."%")->paginate();
 
    		// mengirim data pegawai ke view index
		return view('id.info.info',['info48s' => $info48s]);
 
	}

    public function infoedit()
    {
    	$info48s = Info48::paginate(10);
    	return view('id.info.infoedit', ['info48s' => $info48s]);
    }

    public function info_tambah()
    {
    	return view('id.info.info_tambah');
    }

    public function info_store(Request $request)
    {
    	$this->validate($request,[
    		'gambar' => 'required',
    		'judul' => 'required',
			'rintisan' => 'required',
    		'tag_namagrup' => 'required',
			'selengkapnya' => 'required'
    	]);
 
        Info48::create([
    		'gambar' => $request->gambar,
			'judul' => $request->judul,
    		'rintisan' => $request->rintisan,
    		'tag_namagrup' => $request->tag_namagrup,
			'selengkapnya' => $request->selengkapnya
    	]);
 
    	return redirect('/id/info');
	}

    public function info_edit($id)
	{
   		$info48s = Info48::find($id);
   		return view('id.info.info_edit', ['info48s' => $info48s]);
	}

	public function info_update($id, Request $request)
	{
    	$this->validate($request,[
    		'gambar' => 'required',
    		'judul' => 'required',
    		'rintisan' => 'required',
    		'tag_namagrup' => 'required',
            'selengkapnya' => 'required'
    ]);
 
    	$info48s = Info48::find($id);
    	$info48s->gambar = $request->gambar;
    	$info48s->judul = $request->judul;
    	$info48s->rintisan = $request->rintisan;
    	$info48s->tag_namagrup = $request->tag_namagrup;
		$info48s->selengkapnya = $request->selengkapnya;
    	$info48s->save();
    	return redirect('/id/info');
	}

}
