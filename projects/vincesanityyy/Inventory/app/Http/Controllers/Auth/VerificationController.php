<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\Providers\RouteServiceProvider;
use Illuminate\Foundation\Auth\VerifiesEmails;
use Illuminate\Http\Request;
class VerificationController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Email Verification Controller
    |--------------------------------------------------------------------------
    |
    | This controller is responsible for handling email verification for any
    | user that recently registered with the application. Emails may also
    | be re-sent if the user didn't receive the original email message.
    |
    */

    use VerifiesEmails;

    /**
     * Where to redirect users after verification.
     *
     * @var string
     */
    protected $redirectTo = RouteServiceProvider::HOME;

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
        // $this->middleware('signed')->only('verify');
        // $this->middleware('throttle:6,1')->only('verify', 'resend');
    }

    
    protected function resend(){
        $user = \Auth::user();
        $code = mt_rand(2000,9000);
        $user->confirmation_code = $code;
        $user->save();
        $beautymail = app()->make(\Snowfire\Beautymail\Beautymail::class);
        $beautymail->send('emails.welcome', ['code' => $code], function($message)
        {
            $message
                ->from('bustillov9@gmail.com', 'Inventory App')
                ->to(\Auth::user()->email, 'Inventory App')
                ->subject('YO WASSUP!');
        });
    }

    public function verify(){
        $user = \Auth::user();
        if($user->status == 0){
            return view('auth.verify');
        }
        return redirect('/home');  
    }

    public function verifyUser(Request $request){
        $user = \Auth::user();
        
        if($user->confirmation_code == $request->code){
            $user->confirmation_code = '';
            $user->status = 1;
            $user->save();
            return redirect('/home');
        }else{
            return response()->json('Invalid Code',422);
        }
    }
    
}
