package com.example.technical_test_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class PersonalActivity extends AppCompatActivity {

    private ImageView Btnback;
    private TextView FullName, TxtEmail, TxtGenter;
    private TextView PlaceofBirth, DateofBirth;
    private TextView MobilePhone, MaterialStatus, NoInduk;
    private TextView TxtReligion, IdIjazah, IdNumber;
    private TextView CitizenIdAddress, LastStudy,InstitutName;
    private SharedPreferences preferences;
    JSONObject jsonObject, getJson;
    JSONArray jsonArray;
    String RestApiPersonal = "https://staging.anyargroup.co.id/api/profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        NoInduk = findViewById(R.id.NoInduk);
        FullName = findViewById(R.id.FullName);
        TxtEmail = findViewById(R.id.TxtEmail);
        TxtGenter = findViewById(R.id.TxtGenter);
        PlaceofBirth = findViewById(R.id.PlaceofBirth);
        DateofBirth = findViewById(R.id.DateofBirth);
        MobilePhone = findViewById(R.id.MobilePhone);
        MaterialStatus = findViewById(R.id.MaterialStatus);
        TxtReligion = findViewById(R.id.TxtReligion);
        IdIjazah = findViewById(R.id.IdIjazah);
        IdNumber = findViewById(R.id.IdNumber);
        CitizenIdAddress = findViewById(R.id.CitizenIdAddress);
        LastStudy = findViewById(R.id.LastStudy);
        InstitutName = findViewById(R.id.InstitutName);

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        Btnback = findViewById(R.id.Btnback);
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this, MainActivity.class));
                finish();
            }
        });
        getProfile();
    }
    private void getProfile(){
        final RequestQueue requestQueue = Volley.newRequestQueue(PersonalActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiPersonal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            getJson = new JSONObject(response);
                            jsonArray = getJson.getJSONArray("kar");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                NoInduk.setText(jsonObject.getString("nomor_induk_karyawan"));
                                FullName.setText(jsonObject.getString("nama_lengkap"));
                                TxtEmail.setText(jsonObject.getString("email"));
                                TxtGenter.setText(jsonObject.getString("gender"));
                                PlaceofBirth.setText(jsonObject.getString("tempat_lahir"));
                                DateofBirth.setText(jsonObject.getString("tgl_lahir"));
                                MobilePhone.setText(jsonObject.getString("no_hp"));
                                MaterialStatus.setText(jsonObject.getString("status_pernikahan"));
                                TxtReligion.setText(jsonObject.getString("agama"));
                                IdIjazah.setText(jsonObject.getString("no_ijazah"));
                                IdNumber.setText(jsonObject.getString("no_identitas"));
                                CitizenIdAddress.setText(jsonObject.getString("alamat_domisili"));
                                LastStudy.setText(jsonObject.getString("pendidikan_terakhir"));
                                InstitutName.setText(jsonObject.getString("nama_institusi"));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(PersonalActivity.this,
                            "Oops. request ke server gagal coba lagi",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {

                }
                Toast.makeText(PersonalActivity.this, error.toString(), Toast.LENGTH_LONG);
                Log.e("Profile API Error", error.toString());
                requestQueue.stop();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + preferences.getString("getToken", null));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}