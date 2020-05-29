<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\Providers\RouteServiceProvider;
use App\User;
use Illuminate\Foundation\Auth\RegistersUsers;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class RegisterController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Register Controller
    |--------------------------------------------------------------------------
    |
    | This controller handles the registration of new users as well as their
    | validation and creation. By default this controller uses a trait to
    | provide this functionality without requiring any additional code.
    |
    */

    use RegistersUsers;

    /**
     * Where to redirect users after registration.
     *
     * @var string
     */
    // protected $redirectTo = '/test';

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('guest');
    }

    /**
     * Get a validator for an incoming registration request.
     *
     * @param  array  $data
     * @return \Illuminate\Contracts\Validation\Validator
     */
    protected function validator(array $data)
    {
        return Validator::make($data, [
            'name' => ['required', 'string', 'max:255'],
            'email' => ['required', 'string', 'email', 'max:255', 'unique:users'],
            'password' => ['required', 'string', 'min:8'],
            'phone' => ['required', 'string'],
        ]);
    }

    /**
     * Create a new user instance after a valid registration.
     *
     * @param  array  $data
     * @return \App\User
     */
    protected function create(array $data)
    {
        // dd($data);
        $code = mt_rand(2000,9000);


        //For sms

        $token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImNkZDdlMTI3MjUzMDcxNDQyOWExZGJlYWJhOGMwOTk4Njk2Y2Q5NzU1M2I1ZWRiMTAyOTg4ZTlkZjQzODNjNTU5MWZiYmViZjFkY2IwYThhIn0.eyJhdWQiOiIxIiwianRpIjoiY2RkN2UxMjcyNTMwNzE0NDI5YTFkYmVhYmE4YzA5OTg2OTZjZDk3NTUzYjVlZGIxMDI5ODhlOWRmNDM4M2M1NTkxZmJiZWJmMWRjYjBhOGEiLCJpYXQiOjE1ODkxMDkyMzgsIm5iZiI6MTU4OTEwOTIzOCwiZXhwIjoxNjIwNjQ1MjM4LCJzdWIiOiIxMjg5OCIsInNjb3BlcyI6W119.gPc1ogk8H5Bs62FStK0MVsqRTT_oKoybTjLG2S5UwwygwzE4dz91TykwLuMum4uWK0nScyzvD-I7Rp1_HpMFWK-fmx35dUNTC3K_TLzSjwFDxm_4hB7eTzRrDAmSv4KyQX9qymSsA2JjHrZcNDvoZ0AZECwiW9qb3NbAFAt9V6_pCYccn8QD_jZoxosG4XKGVSZbz22vSCE6VY9UK9boRXUS1lFqrib57IIGoHVEkhfwQfQeE7vE97-xSsynECZO27Ces2WqwfcMtsvRNhnc4YWxpb1GgcVnuuQ75Py79G4Ds-SsDG3OiZIRiHlpnHXXPHzddLUs68BjTOu-4fElymWtDMFkuV2QakgRhLKWEgzvITVBHQLo4chsp3Pwr2aeWXHxS1qg5PCzFnF116zVsiF3mGvABYiZKO9onKRkB4iqrSXW7SC1TQI9SAljTQa7QPlSvelhdhJXMGFsl0dp8peG_CEguuO0Rvoa08NQnXBZ9xA54Vxv_wGUlhgj3SmJzv6XvOJd2TwhjDV94ASccAQUDuxBd4fIPeLvk0Objlm-jx1-BfwNt3KP6J-ABODICNe2LDSV1S-88E-Xi4cTUFdV6Cx0J8EBgNInAWQQsxIXvrkC7HE5M9za-mbLs50Cm2sm1zJ3q-QHVkOr13bAwOSf5aIeSCyT7xAkVq0CV38';
        $textko = new \Textko\Sms($token);
        $sms = $textko->send($data['phone'], 'Hello, your verification code is '. $code);


        //For email
        $beautymail = app()->make(\Snowfire\Beautymail\Beautymail::class);
        $beautymail->send('emails.welcome', ['code' => $code], function($message) use($data)
        {
            $message
                ->from('bustillov9@gmail.com', 'Inventory App')
                ->to($data['email'], 'Inventory App')
                ->subject('YO WASSUP!');
        });


        return User::create([
            'name' => $data['name'],
            'email' => $data['email'],
            'mobile' => $data['phone'],
            'password' => Hash::make($data['password']),
            'status' => 0,
            'confirmation_code' => $code
        ]);
    }   
}
