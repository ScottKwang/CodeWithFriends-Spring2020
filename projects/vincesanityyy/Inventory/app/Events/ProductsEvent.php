<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Broadcasting\PrivateChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

use App\Product;
class ProductsEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $user, $product, $type;

    /**
     * Create a new event instance.
     *
     * @return void
     */
    public function __construct($user, $type, Product $product)
    {
        $this->user = $user;
        $this->product = $product;
        $this->type = $type;
    }

    public function broadcastWith(): array
    {
        return [
            // 'product' => $this->product->with('supplier')->orderBy('product_id', 'desc')->first(),
            'product' => $this->product,
            'user' => $this->user,
            'type' => $this->type
        ];
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return \Illuminate\Broadcasting\Channel|array
     */
    public function broadcastOn()
    {
        return new Channel('Products');
    }
}
