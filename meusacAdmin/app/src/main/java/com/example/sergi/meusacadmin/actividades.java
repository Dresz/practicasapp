package com.example.sergi.meusacadmin;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class actividades extends AppCompatActivity {

    private TextView usname;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        requestQueue = Volley.newRequestQueue(this);
        usname = (TextView) findViewById(R.id.member_t);
        String memering = "";
        usname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JsonParse2();// startActivity allow you to move
            }
        });
        listView = (ListView) findViewById(R.id.list);

        JsonParse2();

    }


    private void JsonParse2( ) {
        //String URL = "http://172.16.179.108:3000/actividad";
        String URL = "http://192.168.0.4:7000/ActividadCurso";
        JsonArrayRequest arrayRequest2 = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        dataModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject activity = jsonArray.getJSONObject(i);
                                String act = activity.getString("nombreActividad");
                                String nota = activity.getString("nota");

                                dataModels.add(new DataModel("Asignada",    nota + " puntos",act,"Accion"));
                                //usname.append(song_title + "-" + audio_file + "\n");

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //usname.setText("aca");

                            }
                        }
                        adapter = new CustomAdapter(dataModels, getApplicationContext());

                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                DataModel dataModel = dataModels.get(position);

                                Snackbar.make(view, dataModel.getName() + "\n" + dataModel.getState() + " API: " + dataModel.getGrade(), Snackbar.LENGTH_LONG)
                                        .setAction("No action", null).show();
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        //usname.setText("acaq");

                    }
                });


        //Toast.makeText(Login.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
        //Log.e("Rest Response:", jsonObject.toString());

        requestQueue.add(arrayRequest2);

    }
}
