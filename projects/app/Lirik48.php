<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Lirik48 extends Model
{
    protected $table = 'lirik48s';
    protected $fillable = ['judul','judul_penyanyi','penyanyi','isi_lirik','link_yt','gambar'];
    protected $primaryKey = 'judul_penyanyi';
    public $incrementing = false;
    protected $keytape = 'string';
}
