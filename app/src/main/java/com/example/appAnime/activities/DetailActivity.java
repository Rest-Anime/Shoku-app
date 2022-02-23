package com.example.appAnime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ImageView image;
    TextView info;
    TextView desc;
    Button edit;
    Button delete;

    Query reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        image = findViewById(R.id.iv);
        info = findViewById(R.id.infoTxt);
        desc = findViewById(R.id.descTxt);
        image.setImageResource(R.drawable.coffeeart);
        edit = findViewById(R.id.btnEdit);
        delete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        Anime anime = (Anime) intent.getSerializableExtra("anime");
        int pos = intent.getIntExtra("pos", 0);

        Picasso.get().load(anime.getFoto()).resize((int) (260 * 2.5), (int) (370 * 2.5)).into(image);
        Toast.makeText(getApplicationContext(), "e", Toast.LENGTH_SHORT).show();

        //INFORMACION BASICA
        info.setText("El título del anime es " + anime.getTitulo() + "\n" +
                "Cuya posicion en la lista es " + pos + "\n" +
                "Su puntuacion es de: " + anime.getPuntuacion() + " estrellas" + "\n" +
                "Fue animado por " + anime.getEstudio() + "\n" +
                "Lanzado en " + anime.getLanzamiento());

        //SINOPSIS
        desc.setText(anime.getDescripcion());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference =
                        FirebaseDatabase.getInstance().getReference().child("Animes").orderByChild("titulo").equalTo(anime.getTitulo());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ERROR", "onCancelled", databaseError.toException());
                    }
                });

            }
        });
    }
}