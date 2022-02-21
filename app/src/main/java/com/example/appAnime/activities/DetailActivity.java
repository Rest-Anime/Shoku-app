package com.example.appAnime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ImageView image;
    TextView info;
    TextView desc;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        image = findViewById(R.id.iv);
        info = findViewById(R.id.infoTxt);
        desc = findViewById(R.id.descTxt);
        image.setImageResource(R.drawable.coffeeart);
        edit = findViewById(R.id.btnEdit);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("desc");
        int duration = intent.getIntExtra("duration", 24);
        String studio = intent.getStringExtra("studio");
        String img = intent.getStringExtra("cover");
        String genre = intent.getStringExtra("genre");
        String release = intent.getStringExtra("release");


        int rate = intent.getIntExtra("rate", 5);
        int seasons = intent.getIntExtra("seasons", 1);
        int pos = intent.getIntExtra("pos", 0);

        Picasso.get().load(img).resize((int) (260 * 2.5), (int) (370 * 2.5)).into(image);
        Toast.makeText(getApplicationContext(), "e", Toast.LENGTH_SHORT).show();

        //INFORMACION BASICA
        info.setText("El título del anime es " + title + "\n" +
                "Cuya posicion en la lista es " + pos + "\n" +
                "Su puntuacion es de: " + rate + " estrellas" + "\n" +
                "Fue animado por " + studio + "\n" +
                "Lanzado en " + release);

        //SINOPSIS
        desc.setText(description);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}