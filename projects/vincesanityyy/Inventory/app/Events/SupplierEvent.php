<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Broadcasting\PrivateChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;
use App\Supplier;
class SupplierEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;
    public $user, $supplier, $type;

    /**
     * Create a new event instance.
     *
     * @return void
     */
    public function __construct($user, $type, Supplier $supplier)
    {
        $this->user = $user;
        $this->supplier = $supplier;
        $this->type = $type;
    }

    public function broadcastWith(): array
    {
        return [
            'supplier' => $this->supplier,
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
        return new Channel('Suppliers');
    }
}
