<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Member48 extends Model
{
    protected $table = 'member48s';
    protected $fillable = ['gambar','username','nama','grup','tim','generasi'];
    protected $primaryKey = 'username';
    public $incrementing = false;
    protected $keytape = 'string';
}
