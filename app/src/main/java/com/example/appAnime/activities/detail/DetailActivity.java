package com.example.appAnime.activities.detail;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.ReviewAdapter;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Review;
import com.example.appAnime.model.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {
    ImageView image, btnFav, userIcon, report, like, dislike, detailWallpaper;
    TextView info, username, rate, animeTitle, reviewTitle, desc, redesc, wikitext, likeCount,
            dislikeCount;
    EditText newReviewText, newReviewTitle;
    LinearLayout layoutAdmin;
    FloatingActionButton addReview;
    FirebaseDatabase bbdd;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewList;
    Review review;
    Button edit, delete;
    Usuario usuario = new Usuario();
    RecyclerView recycler;
    boolean isFav;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String comentario, tituloReview;
    View foto;
    Anime anime = new Anime();
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
        layoutAdmin = findViewById(R.id.layoutAdmin);
        wikitext = findViewById(R.id.wikiTxt);

        userIcon = findViewById(R.id.userPhoto);
        report = findViewById(R.id.iconReport);
        like = findViewById(R.id.iconLike);
        dislike = findViewById(R.id.iconDislike);
        username = findViewById(R.id.userTxt);
        rate = findViewById(R.id.followers);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        animeTitle = findViewById(R.id.animeReview);
        redesc = findViewById(R.id.userDescription);
        reviewTitle = findViewById(R.id.titleTxt);
        reviewList = new ArrayList<Review>();
        addReview = findViewById(R.id.addReview);
        newReviewText = findViewById(R.id.newReviewText);
        newReviewTitle = findViewById(R.id.newReviewTitle);
        detailWallpaper = findViewById(R.id.imgWallpaperDetail);
        review = (Review) getIntent().getSerializableExtra("review");
        listaWalls = new ArrayList<>();
        isFav = false;

        Intent intent = getIntent();
        anime = (Anime) intent.getSerializableExtra("anime");
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        int pos = intent.getIntExtra("pos", 0);

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
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recycler.setVisibility(View.VISIBLE);
        //recycler.setAdapter(reviewAdapter);
        //recycler.setVisibility(View.VISIBLE);

        comentario = "";
        tituloReview = "";
        foto = findViewById(R.drawable.emptyuser);
        puntuacion = 0;

        EventsInterface function = (x) -> {
            Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
            launchInfo.putExtra("review", reviewList.get(x));
            launchInfo.putExtra("usuario", usuario);
            launchInfo.putExtra("pos", x);
            startActivity(launchInfo);
        };

        //todas las reviews referentes al anime en cuestion
        db.collection("reviews").whereEqualTo("animeID", anime.getUID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                reviewList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Review review = doc.toObject(Review.class);
                    review.setUID(doc.getId());
                    review.setAnime(anime);
                    review.setUsuario(usuario);
                    //review.setLikes(review.getLikes());
                    //review.setDislikes(review.getDislikes());
                    reviewList.add(review);
                }
                //likes = review.getLikes();
                //dislikes = review.getDislikes();
                Log.e("Lista reviews ", reviewList.toString());
                reviewAdapter = new ReviewAdapter(reviewList, function);
                recycler.setAdapter(reviewAdapter);

            }
        });
        checkFav();
        Picasso.get().load(anime.getFoto()).resize((int) (260 * 2.5), (int) (370 * 2.5)).into(image);
        Toast.makeText(getApplicationContext(), anime.getTitulo(), Toast.LENGTH_SHORT).show();

        //int nLikes = db.collection("usuarios").where("reviews.documentID", "==", true).get();

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
                if (isFav) {
                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(getApplicationContext(), anime.getTitulo() + " eliminado de " +
                            "favoritos", Toast.LENGTH_SHORT).show();
                    //eliminar de la bbdd
                    usuario.removeAnimeFromList(anime.getUID());
                    Log.d("R", anime.getTitulo() + " ELIMINADO DE FAVORITOS");

                } else {
                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(getApplicationContext(), anime.getTitulo() + " añadido a " +
                            "favoritos", Toast.LENGTH_SHORT).show();
                    //añadirlo a la bbdd
                    usuario.addAnimeToList(anime.getUID(), "Completed", anime.getPuntuacion());
                }
                db.collection("usuarios").document(usuario.getUID()).update(usuario.setFirestore());
                checkFav();
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
                        tituloReview = newReviewTitle.getText().toString();
                        if (comentario == null) {
                            comentario = "No se conoce nada sobre este anime."; //hacer que le
                            // salte una alerta para que escriba la reseña
                        } else {
                            comentario = newReviewText.getText().toString();
                        }

                        Review review = new Review();
                        review.setTitulo(tituloReview);
                        review.setAnimeID(anime.getUID());
                        review.setUsuarioID(usuario.getUID());
                        review.setComentario(comentario);
                        review.setRating(anime.getPuntuacion());
                        review.setLikes(0);
                        review.setDislikes(0);

                        db.collection("reviews").document().set(review.setFirestore());
                        //Log.e("NUEVA REVIEW CREADA: ", reviewList.toString());
                        //Log.e("REVIEW: ", review.getComentario());
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                //dialog.setNeutralButton("STAY", null);
                dialog.show();
            }
        });

    }

    private void checkFav() {
        if (usuario.getAnimes().containsKey(anime.getUID())) {
            isFav = true;
            btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
            Log.d("R", "COINCIDEN");
        } else {
            isFav = false;
            btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            Log.d("R", "NO COINCIDEN");
        }
    }
}