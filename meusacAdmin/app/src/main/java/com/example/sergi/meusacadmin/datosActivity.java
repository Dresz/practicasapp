package com.example.sergi.meusacadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class datosActivity extends AppCompatActivity {

    Bundle dato;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        //***********************************
        requestQueue = Volley.newRequestQueue(this);
        JsonParse();
        //***********************************
    }


    private void JsonParse(){
        //String URL = "http://10.112.1.136:3306/catedratico/";
        String URL = "http://192.168.0.4:7000/catedratico/";
        dato = getIntent().getExtras();
        String usuario = dato.getString("usuario");
        URL+=usuario;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i =0; i < jsonArray.length();i++) {
                            try{
                                JSONObject user = jsonArray.getJSONObject(i);
                                String name = user.getString("nombre");
                                name += " ";
                                name+= user.getString("apellido");
                                String dpi = user.getString("dpi");
                                String telefono = user.getString("telefono");
                                String direccion = user.getString("direccion");
                                String fechaNacimiento = user.getString("fechaNacimiento");
                                String usuario = user.getString("usuario");

                                TextView nombre = (TextView) findViewById(R.id.txtDatosNombre);
                                nombre.setText("Nombre: "+name);
                                TextView id = (TextView) findViewById(R.id.txtDatosDpi);
                                id.setText("Carnet: "+dpi);
                                TextView numero = (TextView) findViewById(R.id.txtDatosTelefono);
                                numero.setText("Telefono: "+telefono);
                                TextView dir = (TextView) findViewById(R.id.txtDatosDireccion);
                                dir.setText("Direccion: "+direccion);
                                TextView fecha = (TextView) findViewById(R.id.txtDatosFecha);
                                fecha.setText("Fecha: "+fechaNacimiento);
                                TextView num = (TextView) findViewById(R.id.txtDatosUsuario);
                                num.setText("Usuario: "+usuario);
                            }catch(JSONException e){
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
        requestQueue.add(arrayRequest);
    }

}
