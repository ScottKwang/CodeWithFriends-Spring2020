<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
class StockHistory extends Model
{
    use SoftDeletes;

    public function stocks(){
       return $this->belongsTo('App\Product','product_id');
    }

    public function users(){
        return $this->belongsTo('App\User','user_id');
    }
    
}
