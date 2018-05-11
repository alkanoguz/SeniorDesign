package alkanoguz.androidakademiproject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;



public class SplashScreen extends Activity {
    public static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    boolean login_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        boolean login_status = pref.getBoolean("is_login", false);
        String name = pref.getString("name",null); */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        if (login_status){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this,CamTestActivity.class);
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


    }
}


