package com.example.sergi.meusacadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class agregarCursoActividadActivity extends AppCompatActivity {

    Spinner combo;
    ArrayList<String> list = new ArrayList<String>();
    EditText nombre;
    Button agregar;
    RequestQueue requestQueue;
    String eleccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_curso_actividad);

        combo = (Spinner) findViewById(R.id.comboEleccion);
        nombre = (EditText) findViewById(R.id.txtActCurNombre);
        agregar = (Button) findViewById(R.id.btnActCurActividad);

        list.add("Seleccione una opcion");
        list.add("Curso");
        list.add("Actividad");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combo.setAdapter(dataAdapter);

        combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eleccion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    requestQueue = Volley.newRequestQueue(agregarCursoActividadActivity.this);
                    Toast.makeText(agregarCursoActividadActivity.this, eleccion, Toast.LENGTH_LONG).show();
                    if(eleccion.equals("Curso")){
                        AddCursos();
                    }else if(eleccion.equals("Actividad")){
                        AddActividad();
                    }else{
                        Toast.makeText(agregarCursoActividadActivity.this, "Elija una opcion", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d("Aqui","catch");
                    e.printStackTrace();
                }
            }
        });

    }

    //----------------------------------------------------------------------------------------------------------------
    private void AddCursos() throws JSONException {
        //String URL = "http://10.112.1.136:3306/asistencia";
        String URL = "http://192.168.0.4:7000/curso";
        // Post params to be sent to the server
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("nombreCurso", nombre.getText().toString());
        Log.d(nombre.getText().toString(),"try");

        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Aqui","onResponse");
                Log.i("LOG_RESPONSE", response);
                if (response.equals("Updated successfully")) {
                    //Redirect to next activity
                    //Intent i = new Intent(ReaderActivity.this, PaginaPrincipalActivity.class);
                    //i.putExtra("usuario",carnet);
                    //startActivity(i);
                    Toast.makeText(agregarCursoActividadActivity.this, "Se agrego el curso", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(agregarCursoActividadActivity.this, "Error: No se agrego el curso", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }
    //----------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------
    private void AddActividad() throws JSONException {
        //String URL = "http://10.112.1.136:3306/asistencia";
        String URL = "http://192.168.43.72:3306/actividad";
        // Post params to be sent to the server
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("nombreActividad", nombre.getText().toString());
        Log.d(nombre.getText().toString(),"try");

        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Aqui","onResponse");
                Log.i("LOG_RESPONSE", response);
                if (response.equals("Updated successfully")) {
                    //Redirect to next activity
                    //Intent i = new Intent(ReaderActivity.this, PaginaPrincipalActivity.class);
                    //i.putExtra("usuario",carnet);
                    //startActivity(i);
                    Toast.makeText(agregarCursoActividadActivity.this, "Se agrego la actividad", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(agregarCursoActividadActivity.this, "Error: No se agrego la actividad", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }
    //----------------------------------------------------------------------------------------------------------------


}
