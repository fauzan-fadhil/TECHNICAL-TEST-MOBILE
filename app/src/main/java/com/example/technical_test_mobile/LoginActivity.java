package com.example.technical_test_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText edtemail,edtpassword;
    private Button ButtonLogin;
    private JSONObject jsonObject;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String RestAPI = "https://staging.anyargroup.co.id/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        ButtonLogin = findViewById(R.id.ButtonLogin);

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        editor = preferences.edit();

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtemail.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (edtpassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Logindata();
                }
            }
        });
    }

    private void Logindata() {
        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            Log.e("jason", jsonObject.toString());
                            if (jsonObject.getString("code").equals("reg3")) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                String name = jsonObject.getString("username");
                                String token = jsonObject.getString("token");
                                editor.putString("getName", name);
                                editor.putString("getToken", token);
                                editor.apply();
                                LoginActivity.this.finish();
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this,
                            "Oops. request ke server gagal coba lagi",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this,
                            String.valueOf(error),
                            Toast.LENGTH_LONG).show();
                }
                logingagal();
                Log.e("API Error", error.toString());
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", edtemail.getText().toString().trim());
                params.put("password", edtpassword.getText().toString().trim());
                // Log.e("Json", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void logingagal() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Login Gagal");
        alertDialogBuilder
                .setMessage("Email dan password salah atau periksa internet anda!!!")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}