package com.example.sergi.meusacadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class actividadActivity extends AppCompatActivity {

    Spinner curso, actividad;
    RequestQueue requestQueue;
    String idCurso, idEstado,nota;
    EditText txtNota, txtNombre;
    Button agregar;
    Bundle dato;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list2 = new ArrayList<String>();
    ArrayList<String> list3 = new ArrayList<String>();
    ArrayList<String> list4 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        curso = (Spinner) findViewById(R.id.comboCurso);
        actividad = (Spinner) findViewById(R.id.comboActividad);
        txtNota = (EditText) findViewById(R.id.txtActividadNota);
        txtNombre = (EditText) findViewById(R.id.txtActividadNombre);

        /*ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.seleCurso, android.R.layout.simple_spinner_item);*/

        /*ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,
                R.array.seleTipo, android.R.layout.simple_spinner_item);*/

        list.add("Seleccione un curso");

        requestQueue = Volley.newRequestQueue(actividadActivity.this);
        ParseGetCurso();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        curso.setAdapter(dataAdapter);

        curso.setAdapter(dataAdapter);

        list2.add("Seleccione el tipo de actividad");

        //requestQueue = Volley.newRequestQueue(actividadActivity.this);
        ParseGetActividad();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actividad.setAdapter(dataAdapter2);

        actividad.setAdapter(dataAdapter2);

        curso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCurso = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEstado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        agregar = (Button)findViewById(R.id.btnAgregarActividad);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddActividad();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*requestQueue = Volley.newRequestQueue(ReaderActivity.this);
        try {
            AddAsistencia();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        /*og.d("CURSO----",idCurso);
        for (int i = 0; i < list.size(); i++){
                    /*if (list.get(i).equals(idCurso)){
                        int aux = i-1;
                        idCurso= list3.get(i);
                        break;
                    }
        }*/

    }

    //----------------------------------------------------------------------------------------------------------------
    private void AddActividad() throws JSONException {
        //String URL = "http://10.112.1.136:3306/detalleCursoActividad";
        String URL = "http://192.168.0.4:7000/detalleCursoActividad";
        dato = getIntent().getExtras();
        // Post params to be sent to the server
        JSONObject jsonBody = new JSONObject();
        for (int i = 0; i < list.size(); i++){
                if (list.get(i).equals(idCurso)){
                    int aux = i-1;
                    idCurso= list3.get(aux);
                    break;
                }
        }
        for (int i = 0; i < list2.size(); i++){
            if (list2.get(i).equals(idEstado)){
                int aux = i-1;
                idEstado= list4.get(aux);
                break;
            }
        }
        jsonBody.put("nota",txtNota.getText().toString());
        jsonBody.put("idcurso", idCurso);
        jsonBody.put("idactividad",idEstado);
        jsonBody.put("nombreActividad",txtNombre.getText().toString());


        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE", response);
                if (response.equals("Updated successfully")) {
                    //Redirect to next activity
                    Toast.makeText(actividadActivity.this, "Se registro actividad", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(actividadActivity.this, PaginaPrincipalActivity.class);
                    String usuario = dato.getString("usuario");
                    i.putExtra("usuario",usuario);
                    startActivity(i);
                } else {
                    Toast.makeText(actividadActivity.this, "Error: No se registro actividad", Toast.LENGTH_LONG).show();

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

           /* @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }*/
        };
        requestQueue.add(stringRequest);
    }
    //----------------------------------------------------------------------------------------------------------------

    private void ParseGetCurso() {
        //String URL = "http://10.112.1.136:3306/curso";
        String URL = "http://192.168.43.72:3306/curso";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject user = jsonArray.getJSONObject(i);
                                String id = user.getString("id");
                                String nombreCurso = user.getString("nombreCurso");
                                Log.d("**VALOR:",id);
                                Log.d("**VALOR:",nombreCurso);
                                list.add(nombreCurso);
                                list3.add(id);
                                //actions to fill table
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });


        //Toast.makeText(Login.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
        //Log.e("Rest Response:", jsonObject.toString());
        requestQueue.add(arrayRequest);
    }

    private void ParseGetActividad() {
        //String URL = "http://10.112.1.136:3306/actividad";
        String URL = "http://192.168.43.72:3306/actividad";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject user = jsonArray.getJSONObject(i);
                                String id = user.getString("id");
                                String nombreActividad = user.getString("nombreActividad");
                                Log.d("**VALOR:",id);
                                Log.d("**VALOR:",nombreActividad);
                                list2.add(nombreActividad);
                                list4.add(id);
                                //actions to fill table
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });


        //Toast.makeText(Login.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
        //Log.e("Rest Response:", jsonObject.toString());
        requestQueue.add(arrayRequest);
    }

}
