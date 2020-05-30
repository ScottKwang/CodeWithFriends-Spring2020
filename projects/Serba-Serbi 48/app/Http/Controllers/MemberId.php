<?php

namespace App\Http\Controllers;


use Illuminate\Database\Eloquent\Model;
use Illuminate\Http\Request;
use App\Member48;

class MemberId extends Controller
{
    public function member()
    {
    	$member48s = Member48::paginate(10);
    	return view('id.member.member', ['member48s' => $member48s]);
	}
	
	public function cari(Request $request)
	{
		// menangkap data pencarian
		$cari = $request->cari;
 
    		// mengambil data dari table pegawai sesuai pencarian data
		$member48s = Member48::where('username','like',"%".$cari."%")->paginate();
 
    		// mengirim data pegawai ke view index
		return view('id.member.member',['member48s' => $member48s]);
 
	}

    public function memberedit()
    {
    	$member48s = Member48::paginate(10);
    	return view('id.member.memberedit', ['member48s' => $member48s]);
    }

    public function member_tambah()
    {
    	return view('id.member.member_tambah');
    }

    public function member_store(Request $request)
    {
    	$this->validate($request,[
    		'gambar' => 'required',
    		'nama' => 'required',
			'username' => 'required',
			'grup' => 'required',
    		'tim' => 'required',
			'generasi' => 'required'
    	]);
 
        Member48::create([
    		'gambar' => $request->gambar,
			'nama' => $request->nama,
			'username' => $request->username,
    		'grup' => $request->grup,
    		'tim' => $request->tim,
			'generasi' => $request->generasi
    	]);
 
    	return redirect('/id/member');
	}

    public function member_edit($username)
	{
   		$member48s = Member48::find($username);
   		return view('id.member.member_edit', ['member48s' => $member48s]);
	}

	public function member_update($username, Request $request)
	{
    	$this->validate($request,[
    		'gambar' => 'required',
    		'username' => 'required',
    		'nama' => 'required',
    		'grup' => 'required',
    		'tim' => 'required',
            'generasi' => 'required'
    ]);
 
    	$member48s = Member48::find($username);
    	$member48s->gambar = $request->gambar;
    	$member48s->username = $request->username;
    	$member48s->nama = $request->nama;
    	$member48s->grup = $request->grup;
    	$member48s->tim = $request->tim;
		$member48s->generasi = $request->generasi;
    	$member48s->save();
    	return redirect('/id/member');
	}

	public function atsukomaeda()
    {
    	$member48s = Member48::where('username', '=', 'atsukomaeda')->get();
    	return view('id.member.memberdetil.atsukomaeda', ['member48s' => $member48s]);
	}
	
	public function shimazakiharuka()
    {
    	$member48s = Member48::where('username', '=', 'shimazakiharuka')->get();
    	return view('id.member.memberdetil.shimazakiharuka', ['member48s' => $member48s]);
    }
}
