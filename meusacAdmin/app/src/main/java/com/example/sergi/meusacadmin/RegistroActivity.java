package com.example.sergi.meusacadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistroActivity extends AppCompatActivity {

    Button btnregistro;
    EditText nombre, apellido, dpi, numeroTelefono, direccion, fecha, password, usuario;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = (EditText) findViewById(R.id.txtRegistroNombre);
        apellido = (EditText) findViewById(R.id.txtRegistroApellido);
        dpi = (EditText) findViewById(R.id.txtRegistroDpi);
        usuario = (EditText) findViewById(R.id.txtRegistroUsuario);
        direccion = (EditText) findViewById(R.id.txtRegistroDireccion);
        numeroTelefono = (EditText) findViewById(R.id.txtRegistroTelefono);
        fecha = (EditText) findViewById(R.id.txtRegistroFecha);
        password = (EditText) findViewById(R.id.txtRegistroPassword);

        requestQueue = Volley.newRequestQueue(RegistroActivity.this);
        btnregistro = (Button)findViewById(R.id.btnRegistrarse);

        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddModEstudiante();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void AddModEstudiante() throws JSONException {
        Log.d("Aqui","-----------------------------Entro al AddModEstudiante");
        //String URL = "http://10.112.1.136:3306/catedratico";
        String URL = "http://192.168.0.4:7000/catedratico";
        // Post params to be sent to the server
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("dpi", dpi.getText().toString());
        jsonBody.put("nombre", nombre.getText().toString());
        jsonBody.put("apellido", apellido.getText().toString());
        jsonBody.put("telefono", numeroTelefono.getText().toString());
        jsonBody.put("direccion", direccion.getText().toString());
        jsonBody.put("fechaNacimiento", fecha.getText().toString());
        jsonBody.put("contrasenia", password.getText().toString());
        jsonBody.put("usuario", usuario.getText().toString());

        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Aqui","-----------------------------Entro al OnResponse");
                Log.i("LOG_RESPONSE", response);
                if (response.equals("Updated successfully")) {
                    //Redirect to next activity
                    Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(RegistroActivity.this, "Error: No se pudo agregar estudiante", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
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

}
