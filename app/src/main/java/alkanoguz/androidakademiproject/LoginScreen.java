package alkanoguz.androidakademiproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class LoginScreen extends Activity {
    EditText et_tckimlik, et_password;
    TextView register_tv, deneme_tv;
    Button login;
    String user_info;
    String jsonResponse = "";
    String login_url;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RequestQueue queue;
    String name,surname;
    String uid;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mContext = this;
        queue = Volley.newRequestQueue(this);
        pref = getSharedPreferences("User", 0);
        editor = pref.edit();
        et_tckimlik = (EditText) findViewById(R.id.login_input_tckimlik);
        et_password = (EditText) findViewById(R.id.login_input_password);
        register_tv = (TextView) findViewById(R.id.register_tv);
        login = (Button) findViewById(R.id.btn_login);
        deneme_tv = (TextView) findViewById(R.id.deneme2_tv);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tckimlik = et_tckimlik.getText().toString();
                String password = et_password.getText().toString();
                loginUser(tckimlik, password);
            }
        });
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(i);
            }
        });
    }

    private void loginUser(String tckimlik, String password) {

        login_url = getResources().getString(R.string.login_url);

        JSONObject header = new JSONObject();
        JSONObject information = new JSONObject();


        try {
            information.put("password", password.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            information.put("tc_kimlik", tckimlik);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            header.put("login", information);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, login_url, header, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                JSONObject data = response;
                JSONObject information = new JSONObject();
                try {
                    information = data.getJSONObject("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    name = information.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    uid = information.getString("uid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    surname = information.getString("surname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                deneme_tv.setText("Hoşgeldiniz " + name);
                Toast.makeText(getApplicationContext(),"Hoşgeldin " + name +" "+ surname, Toast.LENGTH_LONG).show();
                if(name != null) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("is_login", true);
                    editor.putString("name", name);
                    editor.putString("uid",uid);
                    editor.putString("surname",surname);
                    editor.apply();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(LoginScreen.this,ServerTest.class);
                            startActivity(i);
                            finish();
                        }
                    },3000);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                Toast.makeText(getApplicationContext(), "Hatalı Giriş. Tekrar Deneyin !", Toast.LENGTH_LONG).show();

            }

        });


        queue.add(jsonObjectRequest);
    }
}




