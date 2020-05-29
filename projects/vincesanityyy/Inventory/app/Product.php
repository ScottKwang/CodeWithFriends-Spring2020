<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
class Product extends Model
{
    use SoftDeletes;
    protected $primaryKey = 'product_id';
    protected $fillable = [
        'product_id','price',
        'product_name','quantity','supplier_id','category','barcode','image',
    ];

    public function supplier()
    {
        return $this->belongsTo('App\Supplier','supplier_id');
    }

}
