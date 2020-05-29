<?php
use App\Events\InventoryEvent;
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
    // broadcast(new InventoryEvent('some data'));
    event(new InventoryEvent('somedata'));
    return view('welcome');
});

Auth::routes();

Route::get('/home', 'HomeController@index');
//products
Route::get('/getProducts','ProductController@index');
Route::post('/addProduct','ProductController@store');
Route::put('/updateProduct/{product_id?}','ProductController@update');
Route::delete('/deleteProduct/{product_id?}','ProductController@destroy');
Route::put('/stockIn/{product_id?}','ProductController@stockIn');

//suppliers
Route::get('/getSuppliers','SupplierController@index');
Route::post('/addSupplier','SupplierController@store');
Route::put('/updateSupplier/{supplier_id?}','SupplierController@update');
Route::delete('/deleteSupplier/{supplier_id?}','SupplierController@destroy');
Route::get('/getSuppliersCombo','SupplierController@getCombo');

//reports
Route::get('/getProductPercentageBySupplier','SupplierController@getProductPercentageBySupplier');
//stockHistory
Route::get('/getHistory','StockHistoryController@getHistory');
//regEx for route

// TestSMS
Route::get('/sendSMS','ProductController@sendSMS');
  
Route::get('/verifyUser','Auth\VerificationController@verify');
//Resend Code
Route::post('/resend','Auth\VerificationController@resend');
//Verfiy Code
Route::post('/verifyUser','Auth\VerificationController@verifyUser');

Route::get('{path}','HomeController@index')->where( 'path', '([A-z]+)?' );


  