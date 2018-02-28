package alkanoguz.androidakademiproject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;



public class SplashScreen extends Activity {
    public static int SPLASH_TIME_OUT = 3000;
    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences  = getApplicationContext().getSharedPreferences("User",0);
        boolean login_status = preferences.getBoolean("is_login",false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,RegisterScreen.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
        /*if (login_status==true){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this,Companies.class);
                    startActivity(i);
                    finish();
                }
            },SPLASH_TIME_OUT);

        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this,LoginScreen.class);
                    startActivity(i);
                    finish();
                }
            },SPLASH_TIME_OUT);


        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);*/
    }
}


