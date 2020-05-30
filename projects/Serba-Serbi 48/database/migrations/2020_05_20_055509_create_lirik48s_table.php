<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateLirik48sTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('lirik48s', function (Blueprint $table) {
            $table->text('judul');
            $table->text('judul_penyanyi');
            $table->text('penyanyi');
            $table->text('isi_lirik');
            $table->text('link_yt');
            $table->text('gambar');
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
        Schema::dropIfExists('lirik48s');
    }
}
