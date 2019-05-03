package com.example.sergi.meusacadmin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import cz.msebera.android.httpclient.Header;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Archivos extends AppCompatActivity {
    Button button;
    Button button2;
    TextView textView;
    Spinner spinner;
    public static EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button)findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView6);
        //editText = (EditText) findViewById(R.id.editText3);
        requestQueue = Volley.newRequestQueue(this);
        JsonParse2();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(Archivos.this)
                        .withRequestCode(1000)
                        //.withFilter(Pattern.compile(".*\\.jpg$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });
        button2 = (Button)findViewById(R.id.button2);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MyTask().execute();
                Snackbar.make(view, "Archivo Subido", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                file = "";
                textView.setText("");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1001:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Permiso Permitido",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this,"Permiso Denegado",Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            file = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            //textView.setText(file);
            Log.d("LOG_RESPONSE", String.valueOf(file));
           // textView = (TextView) findViewById(R.id.textView6);
            textView.setText(file);
        }
        catch (Exception e)
        {

        }
    }
    Bundle dato;
    static ArrayList<String> dataModels = new ArrayList<>();;
    ListView listView;
    private static CustomAdapter adapter;
    RequestQueue requestQueue;

    private void JsonParse2( ) {
        String URL = "http://192.168.0.4:7000/Curso2";
        //dato = getIntent().getExtras();
        //String usuario = dato.getString("usuario");
        JsonArrayRequest arrayRequest2 = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Archivos.dataModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("Aqui","*******************************Entro al onResponses");
                            try {
                                JSONObject activity = jsonArray.getJSONObject(i);
                                String archivo = activity.getString("nombreCurso");
                                Log.d("Aqui",archivo);
                                dataModels.add(archivo);
                                Log.d("Aqui",Archivos.dataModels.size()+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("Aqui",Archivos.dataModels.size()+" segunda ves");
                        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>
                                (Archivos.this, android.R.layout.simple_spinner_item, Archivos.dataModels);
                        comboAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        Spinner textbox=(Spinner) findViewById(R.id.spinner);
                        textbox.setAdapter(comboAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });
        requestQueue.add(arrayRequest2);
    }


    public static String file="Def";

    private class MyTask extends AsyncTask<Void,Void,Void>
    {
        //RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        private DefaultHttpClient mHttpClient;
        @Override
        protected Void doInBackground(Void... voids) {

            // String urlString = "http://192.168.0.4:7000/upload?imagename";
/*
            Log.d("Aqui","Archivos");
            String url = "http://192.168.0.4:7000/upload";
            Log.d("LOG_RESPONSE", String.valueOf(url));
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "Download/1.jpg");
            RequestParams params = new RequestParams();
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(url);

                InputStreamEntity reqEntity = new InputStreamEntity(
                        new FileInputStream(file), -1);
                reqEntity.setContentType("binary/octet-stream");
                reqEntity.setChunked(true); // Send in multiple parts if needed
                httppost.setEntity(reqEntity);
                HttpResponse response = httpclient.execute(httppost);
                //Do something with response...
                Log.d("LOG_RESPONSE", String.valueOf("si jalo"));

            } catch (Exception e) {
                // show error
                Log.d("LOG_RESPONSE", String.valueOf(e));
            }*/

            try {
                String url = "http://192.168.0.4:7000/upload";
                Spinner textbox=(Spinner) findViewById(R.id.spinner);
                Log.d("LOG_RESPONSE", String.valueOf(Archivos.file));
                File file = new File(Archivos.file);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                InputStreamEntity reqEntity = new InputStreamEntity(
                        new FileInputStream(file), -1);
                reqEntity.setContentType("binary/octet-stream");
                reqEntity.setChunked(true); // Send in multiple parts if needed
                params.put("upfile", file);
                params.put("curso",textbox.getSelectedItem().toString());
                //params.put("more", "data");
                client.post(url, params, new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {
                                // called when response HTTP status is "200 OK"
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            }


                            // ----New Overridden method
                            @Override
                            public boolean getUseSynchronousMode() {
                                return false;
                            }
                        }
                );
                Log.d("LOG_RESPONSE", String.valueOf("win!!"));
            }
            catch (Exception e)
            {
                Log.d("LOG_RESPONSE", String.valueOf(e));
            }

            return null;
        }
        protected void onPostExecute(Void result) {

        }
    }
}
