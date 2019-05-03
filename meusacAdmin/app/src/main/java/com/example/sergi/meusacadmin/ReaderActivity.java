package com.example.sergi.meusacadmin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ReaderActivity extends AppCompatActivity {

    private Button scan_btn;
    String carnet;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------
    private void AddAsistencia() throws JSONException {
        //String URL = "http://10.112.1.136:3306/asistencia";
        String URL = "http://192.168.0.4:7000/asistencia";
        // Post params to be sent to the server
        JSONObject jsonBody = new JSONObject();
        Log.d("AQUI----",carnet);
        jsonBody.put("carnet", carnet);


        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE", response);
                if (response.equals("Updated successfully")) {
                    //Redirect to next activity
                    //Intent i = new Intent(ReaderActivity.this, PaginaPrincipalActivity.class);
                    //i.putExtra("usuario",carnet);
                    //startActivity(i);
                    Toast.makeText(ReaderActivity.this, "Se registro asistencia", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReaderActivity.this, "Error: No se registro asistencia", Toast.LENGTH_LONG).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        carnet = result.getContents();
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, carnet, Toast.LENGTH_LONG).show();//CODIGO QR
                requestQueue = Volley.newRequestQueue(ReaderActivity.this);
                try {
                    AddAsistencia();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
