package com.example.tepukapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SuccesBuy extends AppCompatActivity {
    Button btnTrack,btnHome;
    private SharedPreferences userPref,paymentPref;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_buy);
        btnTrack = findViewById(R.id.btnTrack);
        btnHome = findViewById(R.id.btnHome);
        paymentPref = getSharedPreferences("payment",Context.MODE_PRIVATE);
        id = paymentPref.getInt("id",0);
        Log.d("kahla", String.valueOf(id));


        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shipping();
            }
        });
    }

    private void shipping() {
        userPref = getSharedPreferences("user", Context.MODE_PRIVATE);


        StringRequest request = new StringRequest(Request.Method.POST, Constant.CREATE_SHIPPING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        startActivity(new Intent(SuccesBuy.this,HomeActivity.class));
                    } else {
                        Toast.makeText(SuccesBuy.this, "Shipping Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                Log.d("ojan", String.valueOf(map));
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}
