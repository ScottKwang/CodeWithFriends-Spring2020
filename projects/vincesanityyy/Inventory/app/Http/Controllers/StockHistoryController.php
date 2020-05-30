<?php

namespace App\Http\Controllers;

use App\StockHistory;
use Illuminate\Http\Request;
use App\Supplier;
use App\User;
use App\Product;
class StockHistoryController extends Controller
{
   public function getHistory(){
        $history = StockHistory::with(['users','stocks'=>function($query){
            $query->with('supplier');
        }])->get();
        return response()->json($history);
   }
}
