<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class InfoGrup48 extends Model
{
    protected $table = 'infogrup48s';
    protected $fillable = ['grup','gambar','penjelasan','single'];
    protected $primaryKey = 'grup';
    public $incrementing = false;
    protected $keytape = 'string';
}

