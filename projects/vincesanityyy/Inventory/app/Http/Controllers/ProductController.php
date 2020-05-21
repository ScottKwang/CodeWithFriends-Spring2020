<?php

namespace App\Http\Controllers;

use App\Product;
use Illuminate\Http\Request;
use App\StockHistory;
use Carbon\Carbon;
use App\Events\ProductsEvent;
class ProductController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $products = Product::with('supplier')->get();

        return response()->json($products);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {

    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $product = new Product;
        $validator = \Validator::make($request->all(), [
            'product_name' => 'required',
            'barcode' => 'required|unique:products',
            'price'=> 'required',
            'category' => 'required',
            'supplier' => 'required',
            // 'image' => 'required|image64:jpeg,jpg,png'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => $validator->errors()
            ],422);

        } else {
            $product->barcode = $request->barcode;
            $product->product_name = $request->product_name;
            $product->price = $request->price;
            $product->quantity = 0;
            $product->category = $request->category;
            $product->supplier_id = $request->supplier;
            $imageData = $request->image;
            $fileName = time().'.'. explode('/', explode(':', substr($imageData, 0, strpos($imageData, ';'))) [1])[1];
            \Image::make($request->image)->save(public_path('img/').$fileName);
            $product->image = $fileName;
            $product->save();;

            broadcast(new ProductsEvent(\Auth::user()->name, 'add', $product))->toOthers();
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Product  $product
     * @return \Illuminate\Http\Response
     */
    public function show(Product $product)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Product  $product
     * @return \Illuminate\Http\Response
     */
    public function edit(Product $product)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Product  $product
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Product $product)
    {
        $product = Product::findOrFail($request->product_id);
        $product->barcode = $request->barcode;
        $product->product_name = $request->product_name;
        $product->price = $request->price;
        $product->category = $request->category;
        $product->supplier_id = $request->supplier;

        $currentImage = $product->image;

         if ($request->image != $currentImage){
             $name = time().'.'. explode('/', explode(':', substr($request->image, 0, strpos($request->image, ';'))) [1])[1];
             \Image::make($request->image)->save(public_path('img/').$name);

             $image = public_path('img/').$currentImage;
             if(file_exists($image)){
                @unlink($image);
             }
         }else{
             $name = $currentImage;
         }
        
        //  dd($currentImage);
        $product->image = $name;
        $product->save();
        broadcast(new ProductsEvent(\Auth::user()->name, 'update', $product))->toOthers();
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Product  $product
     * @return \Illuminate\Http\Response
     */
    public function destroy(Product $product_id,Request $request)
    {
        $product = Product::findOrFail($request->product_id);
        // broadcast(new ProductsEvent(\Auth::user()->name, $product));
        broadcast(new ProductsEvent(\Auth::user()->name, 'delete', $product))->toOthers();
        $product->delete();

    }

    public function stockIn(Request $request){

        $stock_history = new StockHistory;
        $stock_history->user_id = \Auth::user()->id;
        $stock_history->product_id = $request->product_id;
        $stock_history->stock_added = $request->stocks;
        $stock_history->date_added = Carbon::now()->toDateTimeString();
        $stock_history->save();

        $product_id = $request->product_id;
        $product = Product::findOrFail($product_id);
        $product->increment('quantity',$request->stocks);
        // dd($request->all());
        $product->save();
        broadcast(new ProductsEvent(\Auth::user()->name, 'stockin', $product))->toOthers();
    }
    

    public function sendSMS(Request $request){
        // $token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImNkZDdlMTI3MjUzMDcxNDQyOWExZGJlYWJhOGMwOTk4Njk2Y2Q5NzU1M2I1ZWRiMTAyOTg4ZTlkZjQzODNjNTU5MWZiYmViZjFkY2IwYThhIn0.eyJhdWQiOiIxIiwianRpIjoiY2RkN2UxMjcyNTMwNzE0NDI5YTFkYmVhYmE4YzA5OTg2OTZjZDk3NTUzYjVlZGIxMDI5ODhlOWRmNDM4M2M1NTkxZmJiZWJmMWRjYjBhOGEiLCJpYXQiOjE1ODkxMDkyMzgsIm5iZiI6MTU4OTEwOTIzOCwiZXhwIjoxNjIwNjQ1MjM4LCJzdWIiOiIxMjg5OCIsInNjb3BlcyI6W119.gPc1ogk8H5Bs62FStK0MVsqRTT_oKoybTjLG2S5UwwygwzE4dz91TykwLuMum4uWK0nScyzvD-I7Rp1_HpMFWK-fmx35dUNTC3K_TLzSjwFDxm_4hB7eTzRrDAmSv4KyQX9qymSsA2JjHrZcNDvoZ0AZECwiW9qb3NbAFAt9V6_pCYccn8QD_jZoxosG4XKGVSZbz22vSCE6VY9UK9boRXUS1lFqrib57IIGoHVEkhfwQfQeE7vE97-xSsynECZO27Ces2WqwfcMtsvRNhnc4YWxpb1GgcVnuuQ75Py79G4Ds-SsDG3OiZIRiHlpnHXXPHzddLUs68BjTOu-4fElymWtDMFkuV2QakgRhLKWEgzvITVBHQLo4chsp3Pwr2aeWXHxS1qg5PCzFnF116zVsiF3mGvABYiZKO9onKRkB4iqrSXW7SC1TQI9SAljTQa7QPlSvelhdhJXMGFsl0dp8peG_CEguuO0Rvoa08NQnXBZ9xA54Vxv_wGUlhgj3SmJzv6XvOJd2TwhjDV94ASccAQUDuxBd4fIPeLvk0Objlm-jx1-BfwNt3KP6J-ABODICNe2LDSV1S-88E-Xi4cTUFdV6Cx0J8EBgNInAWQQsxIXvrkC7HE5M9za-mbLs50Cm2sm1zJ3q-QHVkOr13bAwOSf5aIeSCyT7xAkVq0CV38';
        // $textko = new \Textko\Sms($token);
        // $sms = $textko->send('09270277397', 'My awesome text message');

        $senderAddress = '5692';
        // $access_token = '9qpvt8Q0zngP6CwbaZQ4v0BmaLX68INjg6j_h4nqpEU';
        $access_token = 'U9fhRf3HeSEm2kv4VxfOqct_12UQQhhQoAszUsk8Ad0';
        // $ch = curl_init();
        // curl_setopt($ch, CURLOPT_URL, "https://devapi.globelabs.com.ph/smsmessaging/v1/outbound/$senderAddress/requests?access_token=$access_token");
        // curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
        // curl_setopt($ch, CURLOPT_POST,1 );
        // curl_setopt($ch, CURLOPT_HTTPHEADER, array(
        //     'Content-Type: application/json',
        // ));

        // $postData = [
        //     'address' => '+639308146127',
        //     'message' => 'Hi babe I love you <3. Mao ning tong sa need e register',
        // ];
        // curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));

        // $result=curl_exec ($ch);
        // dd($result);

      
    }
}
