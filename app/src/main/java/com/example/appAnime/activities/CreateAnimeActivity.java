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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateAnimeActivity extends AppCompatActivity {
    TextView lblname;
    TextView lbltype;
    TextView lblgr;

    EditText inTitle;
    AutoCompleteTextView genre;
    NumberPicker nEpisodios;
    NumberPicker nTemporadas;
    String genreSelected;
    ImageView imgMuestra;
    RatingBar rating;
    Date lanzamiento;
    String title;
    int episodeQuant;
    int seasonQuant;
    String desc;
    String studio;
    int rate;
    int img;
    String cover = "https://t2.uc.ltmcdn" +
            ".com/images/1/7/4/img_como_se_usan_los_signos_de_interrogacion_19471_600" +
            ".jpg";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");

    FirebaseDatabase bbdd;
    DatabaseReference reference;

    AdapterView.OnItemClickListener funcionSpinner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            genreSelected = (String) parent.getItemAtPosition(position);
            if (genreSelected.equals("Accion/Aventura")) {
                Picasso.get().load(R.drawable.armoredchibi).into(imgMuestra);
                img = R.drawable.armoredchibi;
            } else if (genreSelected.equals("Drama")) {
                Picasso.get().load(R.drawable.sadchibi).into(imgMuestra);
                img = R.drawable.sadchibi;
            } else if (genreSelected.equals("Fantasia")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genreSelected.equals("Ciencia Ficcion")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genreSelected.equals("Slice of Life")) {
                Picasso.get().load(R.drawable.lifechibi).into(imgMuestra);
                img = R.drawable.lifechibi;
            } else if (genreSelected.equals("Shonen")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
                img = R.drawable.neko1;
            } else if (genreSelected.equals("Romance")) {
                Picasso.get().load(R.drawable.lovechibi).into(imgMuestra);
                img = R.drawable.armoredchibi;
            } else if (genreSelected.equals("Comedy")) {
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
        setContentView(R.layout.activity_create);
        bbdd = FirebaseDatabase.getInstance();
        reference = bbdd.getReference().child("Animes");

        lblgr = findViewById(R.id.lblgr);
        lbltype = findViewById(R.id.lbltype);
        lblname = findViewById(R.id.lblname);

        inTitle = findViewById(R.id.inTitle);
        nEpisodios = findViewById(R.id.picker);
        nTemporadas = findViewById(R.id.picker2);
        genre = findViewById(R.id.act);
        imgMuestra = findViewById(R.id.imageView);
        rating = findViewById(R.id.ratingBar2);

        desc = null;
        studio = null;
        lanzamiento = null;

        String[] generos = getResources().getStringArray(R.array.geners);
        ArrayAdapter arrayList2 = new ArrayAdapter(this, R.layout.dropdown_item, generos);
        genre.setAdapter(arrayList2);
        genre.setOnItemClickListener(funcionSpinner);

        nEpisodios.setMaxValue(99);
        nEpisodios.setMinValue(0);
        nTemporadas.setMaxValue(10);
        nTemporadas.setMinValue(0);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog =
                new MaterialAlertDialogBuilder(CreateAnimeActivity.this);
        dialog.setIcon(R.drawable.ic_dialog_close_dark);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Are you sure that you want to insert this order?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Snackbar.make(,"See you later", Snackbar.LENGTH_SHORT).show();
                if (inTitle.getText() != null) {
                    title = String.valueOf(inTitle.getText());
                } else if (inTitle.getText().equals("Name") || inTitle.getText() == null) {
                    title = "Anónimo";
                }

                episodeQuant = nEpisodios.getValue();
                seasonQuant = nTemporadas.getValue();
                rate = (int) rating.getRating();

                if (desc == null) {
                    desc = "No se conoce nada sobre este anime. Como con tu crush";
                } else {
                    //pillar valor del formulario
                }
                if (studio == null) {
                    studio = "Desconocido. Como la motivación de tu vida.";
                } else {
                    //pillar valor del formulario
                }
                if (lanzamiento == null) {
                    lanzamiento = new Date();
                }

                Intent newAnime = new Intent();
                newAnime.putExtra("title", title);
                newAnime.putExtra("desc", desc);
                newAnime.putExtra("studio", studio);
                newAnime.putExtra("genre", genreSelected);
                newAnime.putExtra("duration", episodeQuant);
                newAnime.putExtra("release", sdf.format(lanzamiento));
                newAnime.putExtra("cover", cover);
                newAnime.putExtra("rate", rate);
                newAnime.putExtra("seasons", seasonQuant);
                reference.push().setValue(new Anime(title, desc, episodeQuant, studio, cover,
                        genreSelected, sdf.format(lanzamiento), rate, seasonQuant));
                setResult(RESULT_OK, newAnime);
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();


    }
}