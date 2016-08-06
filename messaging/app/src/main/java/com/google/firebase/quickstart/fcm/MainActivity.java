/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String username, password, ip, mac;
    private final  String prefs="LoginDetails";
    private EditText user, pass;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(prefs,MODE_PRIVATE);
        user = (EditText) findViewById(R.id.edit_text_username);
        pass = (EditText) findViewById(R.id.edit_text_password);
        requestQueue = Volley.newRequestQueue(this);

        user.setText(settings.getString("username",username));
        pass.setText(settings.getString("password",password));

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(getDate());

        FirebaseMessaging.getInstance().subscribeToTopic("news");

    }



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(prefs,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    public void checkin(){
        username = user.getText().toString();
        password = pass.getText().toString();
        mac = getMac(this);

        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, "http://api.ipify.org/?format=json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ip = response.getString("ip");
                    if ((mac.equals("e0:91:f5:7d:e7:f2") && ip.equals("103.204.128.226")) || (mac.equals("c4:a8:1d:8a:56:f4") && ip.equals("103.204.128.226")) || (mac.equals("e4:8d:8c:15:e5:9b") && ip.equals("202.191.122.151"))){
                        StringRequest checkin = new StringRequest(Request.Method.GET, "http://fphantom.com/attendance/android_mod.php?textfield1=" + username + "&textfield2=" + password + "&dw=nothing", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String str[] = response.split("!");

                                if(str.length!=1) {

                                    Intent i = new Intent(getApplicationContext(), Reasons.class);
                                    i.putExtra("user", username);
                                    i.putExtra("pass", password);

                                    startActivity(i);
                                }
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(checkin);
                    }else{
                        Toast.makeText(getApplicationContext(),"You are not at the Office!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(obj);
    }

    public void checkout(View v){
        username = user.getText().toString();
        password = pass.getText().toString();
        mac = getMac(this);

        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, "http://api.ipify.org/?format=json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ip = response.getString("ip");
                    if ((mac.equals("e0:91:f5:7d:e7:f2") && ip.equals("103.204.128.226")) || (mac.equals("c4:a8:1d:8a:56:f4") && ip.equals("103.204.128.226")) || (mac.equals("e4:8d:8c:15:e5:9b") && ip.equals("202.191.122.151"))){
                        StringRequest checkout = new StringRequest(Request.Method.GET, "http://fphantom.com/attendance/android_checkout.php?textfield1=" + username + "&textfield2=" + password, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(checkout);
                    }else{
                        Toast.makeText(getApplicationContext(),"You are not at the Office!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(obj);
    }

    public void report(View v){
        username = user.getText().toString();
        password = pass.getText().toString();

        Intent i = new Intent(getApplicationContext(),ReportActivity.class);

        i.putExtra("user",username);
        i.putExtra("pass",password);

        startActivity(i);

    }

    public void notice(View v){
        Intent i = new Intent(getApplicationContext(), Noti.class);
        i.putExtra("noti","");
        startActivity(i);
    }

    public String getMac(Context context){
        ConnectivityManager conn = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(net.isConnected()){
            WifiManager wifi = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
            WifiInfo wifiInfo= wifi.getConnectionInfo();
            return wifiInfo.getBSSID();
        }
        return "00:00:00:00:00:00";
    }

    public String getDate(){
        String x= DateFormat.getDateInstance().format(new Date());
        return x;
    }
}