package com.google.firebase.quickstart.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.quickstart.fcm.R;

public class Reasons extends AppCompatActivity {
    EditText reason;
    String user,pass;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);
        Intent i = getIntent();
        user = i.getStringExtra("user");
        pass = i.getStringExtra("pass");
        reason = (EditText) findViewById(R.id.reasonText);
        requestQueue = Volley.newRequestQueue(this);
    }


    public void ashiNai(View v){
        String res = reason.getText().toString();
        StringRequest resasons = new StringRequest(Request.Method.GET, "http://fphantom.com/attendance/reasons.php?textfield1=" + user + "&textfield2=" + pass + "&dw=" + res, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(resasons);
        finish();
    }
}
