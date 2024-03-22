package com.example.technical_test_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView TxtNama;
    private ImageView btnPersonal, btnAbsen;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TxtNama = findViewById(R.id.TxtNama);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnAbsen = findViewById(R.id.btnAbsen);

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        TxtNama.setText("Selamat Datang " + preferences.getString("getName", null));

        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PersonalActivity.class));

            }
        });

        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Masih dalam tahap pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

    }
}