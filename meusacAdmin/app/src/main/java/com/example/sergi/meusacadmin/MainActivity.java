package com.example.sergi.meusacadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnLogin;
    EditText eduser, edpass;
    Boolean should = false;
    RequestQueue requestQueue;
    public static String muser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eduser = (EditText) findViewById(R.id.txtUsuario);
        edpass = (EditText) findViewById(R.id.txtPassword);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JsonParse();
            }
        });


    }

    private void JsonParse(){
        //String URL = "http://10.112.1.136:3306/catedratico";
        String URL = "http://192.168.0.4:7000/catedratico";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i =0; i < jsonArray.length();i++) {
                            try{
                                JSONObject user = jsonArray.getJSONObject(i);
                                String username = user.getString("usuario");
                                String password = user.getString("contrasenia");
                                if (eduser.getText().toString().equals("") || edpass.getText().toString().equals("")){
                                    Toast.makeText(MainActivity.this,"Debe ingresar sus datos",Toast.LENGTH_LONG).show();
                                }else if (username.equals(eduser.getText().toString()) && password.equals(edpass.getText().toString())){
                                    muser = username;
                                    Intent intent = new Intent(MainActivity.this,
                                            PaginaPrincipalActivity.class);
                                    intent.putExtra("usuario",muser);
                                    startActivity(intent);
                                }
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
