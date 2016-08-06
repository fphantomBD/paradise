package com.google.firebase.quickstart.fcm;

import android.app.Activity;
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

public class ReportActivity extends AppCompatActivity {
    EditText report;
    String user,pass;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent i = getIntent();
        user = i.getStringExtra("user");
        pass = i.getStringExtra("pass");
        report = (EditText) findViewById(R.id.report);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void reportSubmit(View v){
        String rep = report.getText().toString();

        String prams = "http://fphantom.com/attendance/daily_works.php?textfield1="+user+"&textfield2="+pass+"&dw="+rep;
        StringRequest resasons = new StringRequest(Request.Method.GET, prams, new Response.Listener<String>() {
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
