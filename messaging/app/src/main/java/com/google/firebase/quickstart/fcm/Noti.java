package com.google.firebase.quickstart.fcm;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Noti extends ListActivity {
    private ArrayList<HashMap<String,String>> list;
    private HashMap<String,String> map;
    private ProgressDialog progressDialog;
    private final String url="http://fphantom.com/attendance/android_notice.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications2);
        list = new ArrayList<HashMap<String, String>>();
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=response.length()-1;i>=0;i--){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String id = obj.getString("id");
                        String noti = obj.getString("title");
                        String time = obj.getString("start");
                        map= new HashMap<>();
                        map.put("key",noti);
                        map.put("time",time);
                        list.add(map);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                ListAdapter la = new SimpleAdapter(Noti.this,list,R.layout.list_item,new String[]{"key","time"},new int[]{R.id.noticeText, R.id.timestamp});
                setListAdapter(la);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jar);

    }

    public void back_button(View v){
        finish();
    }
}
