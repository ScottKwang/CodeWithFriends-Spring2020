<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;
use App\Foto48;

class FotoId extends Controller
{
    public function foto()
    {
    	$foto48s = Foto48::paginate(40);
    	return view('id.foto.foto', ['foto48s' => $foto48s]);
	}
	
	public function cari(Request $request)
	{
		// menangkap data pencarian
		$cari = $request->cari;
 
    		// mengambil data dari table pegawai sesuai pencarian data
		$foto48s = Foto48::where('tag_namagrup','like',"%".$cari."%")->paginate();
 
    		// mengirim data pegawai ke view index
		return view('id.foto.foto',['foto48s' => $foto48s]);
 
	}

    public function fotoedit()
    {
    	$foto48s = Foto48::paginate(10);
    	return view('id.foto.fotoedit', ['foto48s' => $foto48s]);
    }

    public function foto_tambah()
    {
    	return view('id.foto.foto_tambah');
    }

    public function foto_store(Request $request)
    {
    	$this->validate($request,[
    		'gambar' => 'required',
    		'keterangan' => 'required',
			'jenis' => 'required',
    		'tag_namagrup' => 'required'
    	]);
 
        Foto48::create([
    		'gambar' => $request->gambar,
			'keterangan' => $request->keterangan,
    		'jenis' => $request->jenis,
    		'tag_namagrup' => $request->tag_namagrup
    	]);
 
    	return redirect('/id/foto');
	}

    public function foto_edit($id)
	{
   		$foto48s = Foto48::find($id);
   		return view('id.foto.foto_edit', ['foto48s' => $foto48s]);
	}

	public function foto_update($id, Request $request)
	{
    	$this->validate($request,[
    		'gambar' => 'required',
    		'keterangan' => 'required',
    		'jenis' => 'required',
    		'tag_namagrup' => 'required'
    ]);
 
    	$foto48s = Foto48::find($id);
    	$foto48s->gambar = $request->gambar;
    	$foto48s->keterangan = $request->keterangan;
    	$foto48s->jenis = $request->jenis;
    	$foto48s->tag_namagrup = $request->tag_namagrup;
    	$foto48s->save();
    	return redirect('/id/foto');
	}

}
