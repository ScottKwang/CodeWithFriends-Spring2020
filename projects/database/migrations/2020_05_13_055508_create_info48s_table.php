<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateInfo48sTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('info48s', function (Blueprint $table) {
            $table->increments('id');
            $table->text('gambar');
            $table->text('judul');
            $table->text('rintisan');
            $table->text('tag_namagrup');
            $table->text('selengkapnya');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('info48s');
    }
}
