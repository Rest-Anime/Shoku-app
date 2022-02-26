package com.example.appAnime.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ModifyAnimeActivity extends AppCompatActivity {
    EditText inTitle;
    AutoCompleteTextView genre;
    NumberPicker nEpisodios;
    NumberPicker nTemporadas;
    String genero;
    ImageView imgMuestra;
    RatingBar rating;
    Date lanzamiento;
    String titulo;
    int duracion;
    int temporadas;
    String descripcion;
    String estudio;
    Anime anime;
    int puntuacion;
    int img;
    String foto = "https://t2.uc.ltmcdn" +
            ".com/images/1/7/4/img_como_se_usan_los_signos_de_interrogacion_19471_600" +
            ".jpg";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    FirebaseDatabase bbdd;
    DatabaseReference reference;

    AdapterView.OnItemClickListener funcionSpinner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            genero = (String) parent.getItemAtPosition(position);
            if (genero.equals("Accion/Aventura")) {
                Picasso.get().load(R.drawable.armoredchibi).into(imgMuestra);
                img = R.drawable.armoredchibi;
            } else if (genero.equals("Drama")) {
                Picasso.get().load(R.drawable.sadchibi).into(imgMuestra);
                img = R.drawable.sadchibi;
            } else if (genero.equals("Fantasia")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genero.equals("Ciencia Ficcion")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genero.equals("Slice of Life")) {
                Picasso.get().load(R.drawable.lifechibi).into(imgMuestra);
                img = R.drawable.lifechibi;
            } else if (genero.equals("Shonen")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genero.equals("Romance")) {
                Picasso.get().load(R.drawable.lovechibi).into(imgMuestra);
                img = R.drawable.armoredchibi;
            } else if (genero.equals("Comedy")) {
                Picasso.get().load(R.drawable.happychibi).into(imgMuestra);
                img = R.drawable.happychibi;
            } else {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        bbdd = FirebaseDatabase.getInstance();
        reference = bbdd.getReference().child("Animes");
        Intent intent = getIntent();
        anime = (Anime) intent.getSerializableExtra("anime");
        inTitle = findViewById(R.id.inTitle);
        inTitle.setText(anime.getTitulo());
        nEpisodios = findViewById(R.id.picker2);
        nEpisodios.setValue(anime.getDuracion());
        nTemporadas = findViewById(R.id.picker);
        nTemporadas.setValue(anime.getTemporadas());
        genre = findViewById(R.id.act);
        String[] generos = getResources().getStringArray(R.array.genres);
        ArrayAdapter arrayList2 = new ArrayAdapter(this, R.layout.dropdown_item, generos);
        genre.setAdapter(arrayList2);
        genre.setOnItemClickListener(funcionSpinner);
        genre.setText(anime.getGenero(), false);
        imgMuestra = findViewById(R.id.imageView);
        Picasso.get().load(anime.getFoto()).into(imgMuestra);
        rating = findViewById(R.id.ratingBar2);
        rating.setRating(anime.getPuntuacion());

        descripcion = null;
        estudio = null;
        lanzamiento = null;

        nEpisodios.setMaxValue(99);
        nEpisodios.setMinValue(1);
        nEpisodios.setValue(anime.getDuracion());
        nTemporadas.setMaxValue(10);
        nTemporadas.setMinValue(1);
        nTemporadas.setValue(anime.getTemporadas());


        System.out.println(anime.toString());
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog =
                new MaterialAlertDialogBuilder(ModifyAnimeActivity.this);
        dialog.setIcon(R.drawable.ic_dialog_close_dark);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Are you sure that you want to insert this order?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Snackbar.make(,"See you later", Snackbar.LENGTH_SHORT).show();
                if (inTitle.getText() != null) {
                    titulo = String.valueOf(inTitle.getText());
                } else if (inTitle.getText().equals("Name") || inTitle.getText() == null) {
                    titulo = "Anónimo";
                }

                duracion = nEpisodios.getValue();
                temporadas = nTemporadas.getValue();
                puntuacion = (int) rating.getRating();

                if (descripcion == null) {
                    descripcion = "No se conoce nada sobre este anime. Como con tu crush";
                } else {
                    //pillar valor del formulario
                }
                if (estudio == null) {
                    estudio = "Desconocido. Como la motivación de tu vida.";
                } else {
                    //pillar valor del formulario
                }
                if (lanzamiento == null) {
                    lanzamiento = new Date();
                }

                reference = FirebaseDatabase.getInstance().getReference().child("Animes");
                HashMap result = new HashMap<>();
                result.put("descripcion", descripcion);
                result.put("duracion", duracion);
                result.put("estudio", estudio);
                result.put("foto", foto);
                result.put("genero", genero);
                result.put("lanzamiento", sdf.format(lanzamiento));
                result.put("puntuacion", puntuacion);
                result.put("temporadas", temporadas);
                result.put("titulo", titulo);


                reference.child(anime.getKey()).updateChildren(result);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNeutralButton("STAY", null);
        dialog.show();


    }
}