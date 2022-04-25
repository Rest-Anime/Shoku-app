package com.example.appAnime.activities.detail;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.ReviewAdapter;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Review;
import com.example.appAnime.model.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {
    ImageView image, btnFav, userIcon, report, like, dislike, detailWallpaper;
    TextView info, username, rate, animeTitle, reviewTitle, desc, redesc, wikitext;
    EditText newReviewText;
    LinearLayout layoutAdmin;
    FloatingActionButton addReview;
    FirebaseDatabase bbdd;
    DatabaseReference reference2;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewList;
    Review review;
    Button edit, delete;
    RecyclerView recycler;
    boolean isFav, userAdmin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query reference;
    String descripcion, tituloReview;
    View foto;
    Usuario user;
    int puntuacion;
    List<Integer> listaWalls;

    public static List<Integer> getDrawablesList() throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class<R.drawable> drawableClass = R.drawable.class;
        R.drawable instance = (R.drawable) drawableClass.getDeclaredConstructors()[0].newInstance();
        List<Integer> drawables = new ArrayList<>();
        for (Field field : drawableClass.getDeclaredFields()) {
            if (field.getName().contains("minimalist_wp")) {
                int value = field.getInt(instance);
                drawables.add(value);
            }
        }
        return drawables;
    }

    @SuppressLint("ResourceType")
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
        btnFav = findViewById(R.id.btnFav);
        recycler = findViewById(R.id.rwr);
        userAdmin = false;
        layoutAdmin = findViewById(R.id.layoutAdmin);
        wikitext = findViewById(R.id.wikiTxt);

        userIcon = findViewById(R.id.cover1);
        report = findViewById(R.id.iconReport);
        like = findViewById(R.id.iconLike);
        dislike = findViewById(R.id.iconDislike);
        username = findViewById(R.id.userTxt);
        rate = findViewById(R.id.rating);
        animeTitle = findViewById(R.id.animeReview);
        redesc = findViewById(R.id.reviewTxt2);
        reviewTitle = findViewById(R.id.titleTxt);
        reviewList = new ArrayList<Review>();
        addReview = findViewById(R.id.addReview);
        newReviewText = findViewById(R.id.newReviewText);
        detailWallpaper = findViewById(R.id.imgWallpaperDetail);
        review = (Review) getIntent().getSerializableExtra("review");
        listaWalls = new ArrayList<>();

        String url = getURLForResource(R.drawable.naruto_minimalist_wp);
        //System.out.println("Direccion: " + url);

        try {
            listaWalls = getDrawablesList();
            System.out.println("N LISTA: " + listaWalls.size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        Random rand = new Random();
        int randomElement = listaWalls.get(rand.nextInt(listaWalls.size()));
        detailWallpaper.setImageResource(randomElement);

        bbdd = FirebaseDatabase.getInstance();
        reference2 = bbdd.getReference().child("Reviews");
        recycler.setAdapter(reviewAdapter);
        recycler.setVisibility(View.VISIBLE);

        descripcion = null;
        tituloReview = null;
        foto = findViewById(R.drawable.emptyuser);
        puntuacion = 0;

        EventsInterface function = (pos) -> {
            Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
            launchInfo.putExtra("review", reviewList.get(pos));
            launchInfo.putExtra("pos", pos);
            startActivity(launchInfo);
            /*
            if(listaEleccion == false){
                Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
                launchInfo.putExtra("review", reviewList.get(pos));
                launchInfo.putExtra("pos", pos);
                startActivity(launchInfo);
            }else{
                Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
                launchInfo.putExtra("review", listaFiltrados.get(pos));
                startActivity(launchInfo);
            }*/
        };

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for (DataSnapshot son : snapshot.getChildren()) {
                    Review review = son.getValue(Review.class);
                    review.setUid(Integer.parseInt(son.getKey()));
                    System.out.println(review.getUid());
                    reviewList.add(review);
                }
                reviewAdapter = new ReviewAdapter(reviewList, function);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent intent = getIntent();
        Anime anime = (Anime) intent.getSerializableExtra("anime");
        int pos = intent.getIntExtra("pos", 0);

        Picasso.get().load(anime.getFoto()).resize((int) (260 * 2.5), (int) (370 * 2.5)).into(image);
        Toast.makeText(getApplicationContext(), anime.getTitulo(), Toast.LENGTH_SHORT).show();

        //INFORMACION BASICA
        info.setText(anime.getTitulo() + "\n" +
                "\n" +
                "Puntuación: " + anime.getPuntuacion() + " estrellas" + "\n" +
                "\n" +
                "Animado por " + anime.getEstudio() + "\n" +
                "\n" +
                "Lanzado en " + anime.getLanzamiento());

        //SINOPSIS
        desc.setText(anime.getDescripcion());
        wikitext.setText("Enlace para más información");
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        });

        //evento para que al clicar se cargue una web
        wikitext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String con la direccion web a la que acceder
                //String web = canciones.get(contador).getWeb();
                String web = anime.getWiki();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, web);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        if (userAdmin) {
            layoutAdmin.setVisibility(View.INVISIBLE);
        } else {
            layoutAdmin.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyIntent = new Intent(getApplicationContext(),
                        ModifyAnimeActivity.class);
                modifyIntent.putExtra("anime", anime);
                startActivity(modifyIntent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("animes").document(anime.getUid()).delete();
                finish();
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog =
                        new MaterialAlertDialogBuilder(DetailActivity.this);
                dialog.setIcon(R.drawable.ic_dialog_close_dark);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Are you sure that you want to insert this review?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(view, "Reseña añadida con éxito", Snackbar.LENGTH_SHORT).show();

                        puntuacion = 3; //hacer que pille la puntuacion que el user le dio al
                        // anime en el que esta
                        tituloReview = String.valueOf(reviewList.size() + 1);
                        if (descripcion == null) {
                            descripcion = "No se conoce nada sobre este anime. Como con tu crush"; //hacer que le salte una alerta para que escriba la reseña
                        } else {
                            descripcion = String.valueOf(newReviewText.getText());
                        }

                        reference2 = FirebaseDatabase.getInstance().getReference().child("Reviews");
                        HashMap result = new HashMap<>();
                        result.put("descripcion", descripcion);
                        result.put("foto", foto); //poner que pille la foto del usuario que crea
                        // la reseña
                        result.put("puntuacion", puntuacion);
                        result.put("titulo", tituloReview);


                        reference2.child(String.valueOf(review.getUid())).updateChildren(result);
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
        });

    }

    public String getURLForResource(int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are
        // not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
}