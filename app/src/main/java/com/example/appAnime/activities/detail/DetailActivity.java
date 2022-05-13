package com.example.appAnime.activities.detail;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    EditText newReviewText, newReviewTitle;
    LinearLayout layoutAdmin;
    FloatingActionButton addReview;
    FirebaseDatabase bbdd;
    DatabaseReference reference2;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewList;
    Review review;
    Button edit, delete;
    Usuario usuario = new Usuario();
    RecyclerView recycler;
    boolean isFav;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query reference;
    String descripcion, tituloReview;
    View foto;
    Usuario user;
    int puntuacion;
    List<Integer> listaWalls;
    ArrayList<Anime> animeFavs = new ArrayList<>();

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
        layoutAdmin = findViewById(R.id.layoutAdmin);
        wikitext = findViewById(R.id.wikiTxt);

        userIcon = findViewById(R.id.userPhoto);
        report = findViewById(R.id.iconReport);
        like = findViewById(R.id.iconLike);
        dislike = findViewById(R.id.iconDislike);
        username = findViewById(R.id.userTxt);
        rate = findViewById(R.id.followers);
        animeTitle = findViewById(R.id.animeReview);
        redesc = findViewById(R.id.userDescription);
        reviewTitle = findViewById(R.id.titleTxt);
        reviewList = new ArrayList<Review>();
        addReview = findViewById(R.id.addReview);
        newReviewText = findViewById(R.id.newReviewTitle);
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
        RecyclerView recyclerView = recycler;


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

        Intent intent = getIntent();
        Anime anime = (Anime) intent.getSerializableExtra("anime");
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        int pos = intent.getIntExtra("pos", 0);


        String animeTitle = anime.getTitulo();
        DocumentReference usuarios = db.collection("usuarios").document("reviews");
        com.google.firebase.firestore.Query reviewsAnime = db.collection("reviews").whereEqualTo(
                "titulo", animeTitle);

        //db.collection("reviews").where("titulo", "==", animeTitle).get().addOnSuccessListener;
        db.collection("reviews").whereEqualTo("titulo", animeTitle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                reviewList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Review review = doc.toObject(Review.class);
                    review.setUID(doc.getId());
                    reviewList.add(review);
                }
                Log.e("Lista", reviewList.toString());
                reviewAdapter = new ReviewAdapter(reviewList, function);
                recycler.setAdapter(reviewAdapter);
            }
        });

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
                if (btnFav.isPressed()) {
                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                    db.collection("usuarios").document("animes").getId();

                    Toast.makeText(getApplicationContext(), anime.getTitulo() + " eliminado de " +
                            "favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    animeFavs.add(anime);
                    db.collection("animes").document("");

                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(getApplicationContext(), anime.getTitulo() + " añadido a " +
                            "favoritos", Toast.LENGTH_SHORT).show();
                }
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

        if (!usuario.isAdmin()) {
            layoutAdmin.setVisibility(View.GONE);
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
                db.collection("animes").document(anime.getUID()).delete();
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


                        reference2.child(String.valueOf(review.getUID())).updateChildren(result);
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