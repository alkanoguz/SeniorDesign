package alkanoguz.androidakademiproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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


public class RegisterScreen extends Activity {
    TextView login_tv,deneme_tv;
    EditText et_name,et_lastname,et_email,et_password,et_address,et_mobile_phone,et_tckimlik;
    Button btn_register;
    RequestQueue queue ;
    String register_url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        queue = Volley.newRequestQueue(this);
        login_tv = (TextView) findViewById(R.id.login_tv);
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterScreen.this,LoginScreen.class);
                startActivity(i);
            }
        });

        et_name = (EditText) findViewById(R.id.register_input_name);
        et_lastname = (EditText) findViewById(R.id.register_input_lastname);
        et_email = (EditText) findViewById(R.id.register_input_email);
        et_password = (EditText) findViewById(R.id.register_input_password);
        et_tckimlik = findViewById(R.id.register_input_tckimlik);
        et_mobile_phone = (EditText) findViewById(R.id.register_input_mobile_phone);
        et_address = (EditText) findViewById(R.id.register_input_address);
        btn_register = (Button) findViewById(R.id.btn_register);
        deneme_tv = (TextView) findViewById(R.id.deneme_tv);

        final Editable name= et_name.getText();
        final Editable lastname = et_lastname.getText();
        final Editable email = et_email.getText();
        final Editable password = et_password.getText();
        final Editable tckimlik = et_tckimlik.getText();
        final Editable mobile_phone = et_mobile_phone.getText();
        final Editable address = et_address.getText();

        int sasasa;

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name2 = et_name.getText().toString();
                String lastname2 = et_lastname.getText().toString();
                String email2 = et_email.getText().toString();
                String password2 = et_password.getText().toString();
                String tckimlik = et_tckimlik.getText().toString();
                String mobile_phone_2 = et_mobile_phone.getText().toString();
                String address2 = et_address.getText().toString();

                registerUser(name2,lastname2,email,password,tckimlik,mobile_phone_2,address2);

            }
        });

    }
    private void registerUser(String name, String lastname, Editable email, Editable password,String tckimlik,
                              String mobile_phone,String address) {
        register_url = getResources().getString(R.string.register_url);


        JSONObject header = new JSONObject();
        JSONObject information = new JSONObject();
        try {
            information.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            information.put("lastname",lastname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            information.put("email",email.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            information.put("password",password.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            information.put("tc_kimlik",tckimlik);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            information.put("mobile_phone",mobile_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            information.put("address",address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            header.put("register",information);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,register_url,header, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response",response.toString());
                deneme_tv.setText(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }

        });


        queue.add(jsonObjectRequest);
    }

    }
