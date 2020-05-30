<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateStockHistoriesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('stock_histories', function (Blueprint $table) {
            $table->bigIncrements('history_id');
            $table->bigInteger('user_id')->unsigned();
            $table->bigInteger('product_id')->unsigned();
            $table->string('stock_added');
            $table->string('date_added');
            $table->timestamps();
            $table->softDeletes();
        });

        Schema::table('stock_histories', function($table)
        {
            $table->foreign('product_id')
                    ->references('product_id')
                    ->on('products'); 
            $table->foreign('user_id')
                    ->references('id')
                    ->on('users');         
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('stock_histories');
    }
}
