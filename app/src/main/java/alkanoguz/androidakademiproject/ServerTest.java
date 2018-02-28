package alkanoguz.androidakademiproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ServerTest extends Activity{
    Button button_send,button_send_json;
    EditText editText;
    TextView message_sent,message_received;
    String post_url =  "http://192.168.1.10/php/index.php";

    RequestQueue queue ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_test);
        button_send = findViewById(R.id.btn_send);
        editText = findViewById(R.id.et_1);
        message_received = findViewById(R.id.tv_received);
        message_sent = findViewById(R.id.tv_sent);
        queue = Volley.newRequestQueue(this);
        button_send_json=findViewById(R.id.btn_2);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest postRequest = new StringRequest(Request.Method.POST, post_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        message_received.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response",error.toString());
                        message_received.setText(error.toString());
                    }
                })
                {
                    protected Map<String,String> getParams(){
                        Map <String,String> params = new HashMap<String, String>();
                        Map <String,String> params2 = new HashMap<String, String>();
                        params.put("name","Ömer");
                        params.put("url","http:asdad");
                        params.put("surname","Demir");

                        return params;

                    }

                };

                queue.add(postRequest);
                editText.setText("Pressed");
            }
        });

        button_send_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject main = new JSONObject();

                JSONObject main2 = new JSONObject();
                try {
                    main.put("name","Sevgül");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    main.put("surname","Sönmez");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    main.put("url","aylavyu");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    main2.put("deneme",main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                message_sent.setText(main2.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,post_url,main2,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response",response.toString());
                        message_received.setText(response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
                        message_received.setText(error.getMessage());
                    }

                });
                        queue.add(jsonObjectRequest);

               /* queue.getCache().clear();*/


}

});
        }
}