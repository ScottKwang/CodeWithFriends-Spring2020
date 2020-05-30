<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;
use App\InfoGrup48;

class InfoGrup48Id extends Controller
{

    public function infogrup48edit()
    {
    	$infogrup48s = InfoGrup48::paginate(100);
    	return view('id.infogrup48.infogrup48edit', ['infogrup48s' => $infogrup48s]);
    }

    public function infogrup48_tambah()
    {
    	return view('id.infogrup48.infogrup48_tambah');
    }

    public function infogrup48_store(Request $request)
    {
    	$this->validate($request,[
    		'grup' => 'required',
    		'gambar' => 'required',
			'penjelasan' => 'required',
			'single' => 'required'
    	]);
 
        InfoGrup48::create([
    		'grup' => $request->grup,
			'gambar' => $request->gambar,
			'penjelasan' => $request->penjelasan,
    		'single' => $request->single,
    	]);
 
    	return redirect('/');
	}

    public function infogrup48_edit($grup)
	{
   		$infogrup48s = InfoGrup48::find($grup);
   		return view('id.infogrup48.infogrup48_edit', ['infogrup48s' => $infogrup48s]);
	}

	public function infogrup48_update($grup, Request $request)
	{
    	$this->validate($request,[
    		'grup' => 'required',
    		'gambar' => 'required',
			'penjelasan' => 'required',
			'single' => 'required'
    ]);
 
    	$infogrup48s = InfoGrup48::find($grup);
    	$infogrup48s->grup = $request->grup;
    	$infogrup48s->gambar = $request->gambar;
    	$infogrup48s->penjelasan = $request->penjelasan;
    	$infogrup48s->single = $request->single;
    	$infogrup48s->save();
    	return redirect('/');
	}

	public function akb48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Akb48')->get();
    	return view('id.infogrup48.infogrup48detil.akb48', ['infogrup48s' => $infogrup48s]);
	}
	
	public function jkt48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Jkt48')->get();
    	return view('id.infogrup48.infogrup48detil.jkt48', ['infogrup48s' => $infogrup48s]);
    }

	public function mnl48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Mnl48')->get();
    	return view('id.infogrup48.infogrup48detil.mnl48', ['infogrup48s' => $infogrup48s]);
    }

	public function ske48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Ske48')->get();
    	return view('id.infogrup48.infogrup48detil.ske48', ['infogrup48s' => $infogrup48s]);
    }

	public function hkt48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Hkt48')->get();
    	return view('id.infogrup48.infogrup48detil.hkt48', ['infogrup48s' => $infogrup48s]);
    }

	public function bnk48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Bnk48')->get();
    	return view('id.infogrup48.infogrup48detil.bnk48', ['infogrup48s' => $infogrup48s]);
    }

	public function cgm48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Cgm48')->get();
    	return view('id.infogrup48.infogrup48detil.cgm48', ['infogrup48s' => $infogrup48s]);
    }

	public function ngt48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Ngt48')->get();
    	return view('id.infogrup48.infogrup48detil.ngt48', ['infogrup48s' => $infogrup48s]);
    }

	public function sgo48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Sgo48')->get();
    	return view('id.infogrup48.infogrup48detil.sgo48', ['infogrup48s' => $infogrup48s]);
	}
	
	public function del48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Del48')->get();
    	return view('id.infogrup48.infogrup48detil.del48', ['infogrup48s' => $infogrup48s]);
    }

	public function stu48()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Stu48')->get();
    	return view('id.infogrup48.infogrup48detil.stu48', ['infogrup48s' => $infogrup48s]);
    }

	public function akb48sh()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Akb48 Tim Shanghai')->get();
    	return view('id.infogrup48.infogrup48detil.akb48sh', ['infogrup48s' => $infogrup48s]);
    }

	public function akb48tp()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Akb48 Tim Taiwan')->get();
    	return view('id.infogrup48.infogrup48detil.akb48tp', ['infogrup48s' => $infogrup48s]);
    }

	public function nogizaka46()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Nogizaka46')->get();
    	return view('id.infogrup48.infogrup48detil.nogizaka46', ['infogrup48s' => $infogrup48s]);
	}
	
	public function keyakizaka46()
    {
    	$infogrup48s = InfoGrup48::where('grup', '=', 'Keyakizaka46')->get();
    	return view('id.infogrup48.infogrup48detil.keyakizaka46', ['infogrup48s' => $infogrup48s]);
    }
}
