<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateProductsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('products', function (Blueprint $table) {
            $table->bigIncrements('product_id');
            $table->string('barcode');
            $table->string('product_name');
            $table->double('price');
            $table->bigInteger('quantity');
            $table->string('category');
            $table->bigInteger('supplier_id')->unsigned();
            $table->timestamps();
            $table->softDeletes();
        });

        Schema::table('products', function($table)
        {
            $table->foreign('supplier_id')
                    ->references('supplier_id')
                    ->on('suppliers');      
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('products');
    }
}
