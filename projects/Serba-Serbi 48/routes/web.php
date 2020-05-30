<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|

*/
Route::get('/', function () {
    return view('awal');});

Route::get('/gantibahasa', 'SerbaserbiController@gantibahasa');
/* Route Tentang */
Route::get('/tentang', 'SerbaserbiController@tentang');
Route::get('/en/about', 'SerbaserbiController@about');
Route::get('/jp/yaku', 'SerbaserbiController@yaku');

/* Route Info Corona */
Route::get('id/corona', 'CoronaId@corona');
Route::get('id/corona/indonesia', 'CoronaId@indonesia');
Route::get('id/corona/provinsi', 'CoronaId@provinsi');
Route::get('id/corona/dunia', 'CoronaId@dunia');
Route::get('id/corona/detilprovinsi', 'CoronaId@detilprovinsi');

/* Route Member Utama (Member48) */
Route::get('/id/member', 'MemberId@member');
Route::get('/id/member_edit', 'MemberId@memberedit');
Route::get('/id/member/tambah', 'MemberId@member_tambah');
Route::get('/id/member/edit/{username}', 'MemberId@member_edit');
Route::get('/id/member/cari', 'MemberId@cari');
Route::post('/id/member/store', 'MemberId@member_store');
Route::put('/id/member/update/{username}', 'MemberId@member_update');

/*Route Member Detil */
Route::get('/id/member/atsukomaeda', 'MemberId@atsukomaeda');
Route::get('/id/member/shimazakiharuka', 'MemberId@shimazakiharuka');


/* Route Lirik Utama (Lirik48) */
Route::get('/id/lirik', 'LirikId@lirik');
Route::get('/id/lirik_edit', 'LirikId@lirikedit');
Route::get('/id/lirik/tambah', 'LirikId@lirik_tambah');
Route::get('/id/lirik/edit/{judul_penyanyi}', 'LirikId@lirik_edit');
Route::get('/id/lirik/cari', 'LirikId@cari');
Route::post('/id/lirik/store', 'LirikId@lirik_store');
Route::put('/id/lirik/update/{judul_penyanyi}', 'LirikId@lirik_update');

/*Route lirik Detil */
Route::get('/id/lirik/hightension_jkt48', 'LirikId@hightension_jkt48');
Route::get('/id/lirik/yuuhiwomiteiruka_akb48', 'LirikId@yuuhiwomiteiruka_akb48');

/* Route Foto Utama (Foto48) */
Route::get('/id/foto', 'FotoId@foto');
Route::get('/id/foto_edit', 'FotoId@fotoedit');
Route::get('/id/foto/tambah', 'FotoId@foto_tambah');
Route::get('/id/foto/edit/{id}', 'FotoId@foto_edit');
Route::get('/id/foto/cari', 'FotoId@cari');
Route::post('/id/foto/store', 'FotoId@foto_store');
Route::put('/id/foto/update/{id}', 'FotoId@foto_update');

/* Route Info Utama (Info48) */
Route::get('/id/info', 'InfoId@info');
Route::get('/id/info_edit', 'InfoId@infoedit');
Route::get('/id/info/tambah', 'InfoId@info_tambah');
Route::get('/id/info/edit/{id}', 'InfoId@info_edit');
Route::get('/id/info/cari', 'InfoId@cari');
Route::post('/id/info/store', 'InfoId@info_store');
Route::put('/id/info/update/{id}', 'InfoId@info_update');

/* Route Info Grup 48 Utama*/
Route::get('/id/infogrup48_edit', 'InfoGrup48Id@infogrup48edit');
Route::get('/id/infogrup48/tambah', 'InfoGrup48Id@infogrup48_tambah');
Route::get('/id/infogrup48/edit/{grup}', 'InfoGrup48Id@infogrup48_edit');
Route::post('/id/infogrup48/store', 'InfoGrup48Id@infogrup48_store');
Route::put('/id/infogrup48/update/{grup}', 'InfoGrup48Id@infogrup48_update');

/*Route Info Grup 48 Detil */
Route::get('/id/akb48', 'InfoGrup48Id@akb48');
Route::get('/id/akb48tp', 'InfoGrup48Id@akb48tp');
Route::get('/id/akb48sh', 'InfoGrup48Id@akb48sh');
Route::get('/id/jkt48', 'InfoGrup48Id@jkt48');
Route::get('/id/mnl48', 'InfoGrup48Id@mnl48');
Route::get('/id/ske48', 'InfoGrup48Id@ske48');
Route::get('/id/hkt48', 'InfoGrup48Id@hkt48');
Route::get('/id/bnk48', 'InfoGrup48Id@bnk48');
Route::get('/id/cgm48', 'InfoGrup48Id@cgm48');
Route::get('/id/ngt48', 'InfoGrup48Id@ngt48');
Route::get('/id/sgo48', 'InfoGrup48Id@sgo48');
Route::get('/id/del48', 'InfoGrup48Id@del48');
Route::get('/id/stu48', 'InfoGrup48Id@stu48');
Route::get('/id/keyakizaka46', 'InfoGrup48Id@keyakizaka46');
Route::get('/id/nogizaka46', 'InfoGrup48Id@nogizaka46');


Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');
