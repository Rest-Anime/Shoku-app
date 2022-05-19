package com.example.appAnime.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
    }

    @Override
    public void onBackPressed() {
        finish();
        //Toast.makeText(getApplicationContext(),"onBackPressed",Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }
}
