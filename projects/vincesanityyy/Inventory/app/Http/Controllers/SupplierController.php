<?php

namespace App\Http\Controllers;

use App\Supplier;
use Illuminate\Http\Request;
use App\Product;
use App\Events\SupplierEvent;
class SupplierController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $suppliers = Supplier::all();
        return response()->json($suppliers);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $supplier = new Supplier;

        $validator = \Validator::make($request->all(), [
            'supplier_name' => 'required',
            'supplier_email' => 'required|unique:suppliers|email',
            'supplier_address'=> 'required',
            'supplier_phone' => 'required|unique:suppliers',
        ]);

        if ($validator->fails()) {
            $errors = json_decode($validator->errors());
            return response()->json([
                'success' => false,
                'message' => $errors
            ],422);
            
        } else {
            $supplier->supplier_name = $request->supplier_name;
            $supplier->supplier_address = $request->supplier_address;
            $supplier->supplier_phone = $request->supplier_phone;
            $supplier->supplier_email = $request->supplier_email;
            $supplier->save();

            broadcast(new SupplierEvent(\Auth::user()->name, 'add', $supplier))->toOthers();
        }

    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Supplier  $supplier
     * @return \Illuminate\Http\Response
     */
    public function show(Supplier $supplier)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Supplier  $supplier
     * @return \Illuminate\Http\Response
     */
    public function edit(Supplier $supplier)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Supplier  $supplier
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Supplier $supplier)
    {
        $supplier = Supplier::findOrFail($request->supplier_id);
        $supplier->supplier_name = $request->supplier_name;
        $supplier->supplier_email = $request->supplier_email;
        $supplier->supplier_phone = $request->supplier_phone;
        $supplier->supplier_address = $request->supplier_address;
        // dd($request->all());
        $supplier->save();

        broadcast(new SupplierEvent(\Auth::user()->name, 'update', $supplier))->toOthers();
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Supplier  $supplier
     * @return \Illuminate\Http\Response
     */
    public function destroy(Supplier $supplier, Request $request)
    {
        $supplier = Supplier::findOrFail($request->supplier_id);
        broadcast(new SupplierEvent(\Auth::user()->name, 'delete', $supplier))->toOthers();
        $supplier->delete();

    }

    public function getCombo(){
        $supplier = \DB::table('suppliers')
            ->select('supplier_name as label','supplier_id as id')
            ->where('deleted_at',null)
            ->get();
        return response()->json($supplier);
    }

    public function getProductPercentageBySupplier(){
    
        $count= \DB::table('suppliers')
                ->join('products','products.supplier_id','=','suppliers.supplier_id')
                ->select(\DB::raw('suppliers.supplier_name as label'),\DB::raw("COUNT('products.*') as value"))
                ->groupBy('suppliers.supplier_name')
                ->get();

        return response()->json($count);
    }
}
