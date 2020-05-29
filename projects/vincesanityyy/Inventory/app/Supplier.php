<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
class Supplier extends Model
{
    use SoftDeletes;
    
    protected $primaryKey = 'supplier_id';
    protected $fillable = [
        'supplier_id','supplier_name',
        'supplier_address','supplier_email','supplier_phone',
    ];

    public function products()
    {
        return $this->hasMany('App\Product','product_id');
    }
}
