package com.example.appAnime.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.adapter.ApiAnime;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Raiz;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView menuLateral;
    RecyclerView recyclerView;
    ArrayList<Anime> listaFiltrados = new ArrayList<>();
    ArrayList<Anime> animeList;
    ImageView menuLatImg;
    AnimeAdapter animeAdapter;
    View cabecera;
    FloatingActionButton fab;
    MediaPlayer mediaPlayer;
    ToggleButton loop;
    ToggleButton playMusic;
    Retrofit retrofit;
    ApiAnime apiAnime;
    TextView songName;
    int counter = 0;
    Button play;
    Animation rotopen;
    Animation rotclose;
    Animation bot;
    Animation tobot;
    AppBarConfiguration appbarconfig;
    FirebaseDatabase bbdd;
    DatabaseReference reference;
    int Code_Create_Anime = 2;


    /*
        El evento OnPrepared se lanzaría una vez, cuando el mp se encuentra listo para
        reproducir audio.
        En nuestro caso, como se obtiene el audio por medios locales, no hace falta caputar este
        evento.
        Si tuvieramos que obtener las canciones de la SD o por Streaming, entonces si nos
        interesaría c
        capturar este evento.
    */

    private MediaPlayer.OnPreparedListener funcionPrepared = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //mediaPlayer.start();
            //seekbar.setMax(mp.getDuration());
        }
    };
    //METODO MUSICA BUCLE
    private CompoundButton.OnCheckedChangeListener loopFunction =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        loop.setText("On");
                        counter = counter;
                        mediaPlayer.setLooping(true);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, "Bucle Activado", duration).show();
                    } else {
                        loop.setText("Off");
                        mediaPlayer.setLooping(false);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, "Bucle Desactivado", duration).show();
                    }
                }
            };

    private EventsInterface function = (pos) -> {
        Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
        launchInfo.putExtra("anime", animeList.get(pos));
        launchInfo.putExtra("pos", pos);
        startActivity(launchInfo);
    };

    /*
    //evento para que al clicar se cargue una web
    private View.OnClickListener funcionWiki = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //String con la direccion web a la que acceder
            String web = canciones.get(contador).getWeb();
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, web);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };
    */

    //metodo para parar la cancion en reproduccion
    public void pararCancion() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            //play es el boton que, al ser clicado, para o reanuda la cancion
        } else {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code_Create_Anime) {
            if (resultCode == RESULT_OK) {
                Anime anime = (Anime) data.getSerializableExtra("anime");
                animeList.add(anime);
                Snackbar.make(recyclerView, anime.getTitulo() + " ha sido creado",
                        Snackbar.LENGTH_SHORT).show();
                animeAdapter.notifyItemInserted(0);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        System.out.println("Inflate");
        getMenuInflater().inflate(R.menu.menuconfig, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        System.out.println(searchItem == null);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                /* ¿Crees que tenemos que hacer algo cuendo el user cierre la barra de búsqueda?
                ¿TODO? */
                return false;
            }
        });

        // Este método se llama cuando se pulsa el botón de buscar.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //@Override
            public boolean onQueryTextSubmit(String query) {
                /*TODO: Programar este metodo para que cuando se pulse el botón de buscar,
                se haga una llamada a la API pasando como parámetro de series la variable query,
                que representa lo que el usuario ha escrito en la barra de búsqueda
                 */

                Call<Raiz> obtenerAnimes = apiAnime.obtenerAnimes();
                Call<Raiz> obtenerAnimesNombre = apiAnime.obtenerAnimesNombre(query);
                obtenerAnimes.enqueue(new Callback<Raiz>() {
                    @Override
                    public void onResponse(Call<Raiz> call, Response<Raiz> response) {
                        Raiz respuesta = response.body();
                        int codigo = response.code();
                        if (respuesta != null) {
                            animeList.clear();
                            animeList.addAll(respuesta.getAnime());
                            animeAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Raiz> call, Throwable t) {

                    }
                });

                return false;
            }

            // Este método se llama cada vez que el texto de la barra de búsqueda cambia
            //@Override
            public boolean onQueryTextChange(String newText) {
               /*TODO: Programar este metodo para que cuando el user escriba,
               se filtre la lista mostrando sólo aquellos cuyo nombre contenga el string incluido
               como parámetro de entrada (newText). No hacer una nueva petición, no es necesario.
                 */
                System.out.println("Searching");
                listaFiltrados.clear();
                for (Anime a : animeList) {
                    if (a.getTitulo().toUpperCase().startsWith(newText.toUpperCase())) {
                        listaFiltrados.add(a);
                    }
                }
                animeAdapter.setAnimeList(listaFiltrados);
                animeAdapter.notifyDataSetChanged();

                return false;
            }

        });

        //return super.onCreateOptionsMenu(menu);
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animeList = new ArrayList<Anime>();
        recyclerView = findViewById(R.id.rw);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fabMenu);
        drawer = findViewById(R.id.dl);

        appbarconfig = new AppBarConfiguration.Builder(R.id.fragment_credits, R.id.fragment_first
                , R.id.fragment_logout).build();

        //CREACION OBJETO RETROFIT PARA INSTANCIAR LA API
        retrofit = new Retrofit.Builder().baseUrl("https://proyect-anime-5daac-default-rtdb" +
                ".firebaseio.com/").addConverterFactory(GsonConverterFactory.create()).build();
        //RETROFIT CREA CLASES ASOCIADAS PARA CONECTARNOS CON LOS ENDPOINT
        apiAnime = retrofit.create(ApiAnime.class);


        menuLateral = findViewById(R.id.navigator);
        cabecera = menuLateral.getHeaderView(0);
        songName = cabecera.findViewById(R.id.songName);
        playMusic = cabecera.findViewById(R.id.playM);
        playMusic.setChecked(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_abrir, R.string.drawer_cerrar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bbdd = FirebaseDatabase.getInstance();
        reference = bbdd.getReference().child("Animes");

        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(reference.getKey());
                int count = 1;
                for (DataSnapshot son : snapshot.getChildren()) {

                    String tittle = (String) son.child("titulo").getValue();
                    String desc = (String) son.child("descripcion").getValue();
                    String studio = (String) son.child("estudio").getValue();
                    String photo = (String) son.child("foto").getValue();
                    String genre = (String) son.child("genero").getValue();
                    int duration =
                            Integer.valueOf(String.valueOf(son.child("duracion").getValue()));
                    int seasons =
                            Integer.valueOf(String.valueOf(son.child("temporadas").getValue()));
                    int rate = Integer.valueOf(String.valueOf(son.child("puntuacion").getValue()));
                    String
                            realease =
                            String.valueOf(son.child("lanzamiento").getValue());

                    //System.out.println("El Anime numero: " + count + " se llama: " + son.child
                    // ("Titulo").getValue() + " " + son.child("Foto"));
                    animeList.add(new Anime(tittle, desc, duration, studio, photo, genre,
                            realease, rate, seasons));
                    count++;
                }
                for (Anime a : animeList) {
                    System.out.println(a.toString());
                }
                animeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rotopen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotclose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        bot = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        tobot = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);


        animeAdapter = new AnimeAdapter(animeList, function);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(animeAdapter);

        //MUSICA
        songName.setText("Ghibli Soundtrack");

        //linea original que crea la cancion a reproducir pillada de un array con objetos cancion
        //mediaPlayer = MediaPlayer.create(this, canciones.get(contador).getPista());
        mediaPlayer = MediaPlayer.create(this, R.raw.ghibliost);
        mediaPlayer.setVolume(3.0f, 3.0f);
        mediaPlayer.start();
        //metodo llamada a funcion loop cuando la llame un boton
        //mediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) loopFunction);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                loop.setText("On");
                counter = counter;
                mediaPlayer.setLooping(true);
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
            }
        });
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pararCancion();
            }
        });

        //PARTE RETROFIT
        Call<Raiz> obtenerAnimes = apiAnime.obtenerAnimes();

        obtenerAnimes.enqueue(new Callback<Raiz>() {
            @Override
            public void onResponse(Call<Raiz> call, Response<Raiz> response) {
                // Obtenemos la respuesta
                Raiz cuerpoVuelta = response.body();

                // OIbtenemos el codigo asicoado a la respuesta: 200,404...
                int codigo = response.code();

                // Sacamos la lista de amiibos
                List<Anime> returnAnimeList = cuerpoVuelta.getAnime();

                animeList.clear();
                animeList.addAll(returnAnimeList);
                animeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Raiz> call, Throwable t) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogoCrear =
                        new MaterialAlertDialogBuilder(MainActivity.this);
                dialogoCrear.setIcon(R.drawable.mr_cast_thumb);
                dialogoCrear.setTitle("Operacion Crear Anime");
                dialogoCrear.setMessage("¿Estas seguro de que quieres crear un Anime?");
                dialogoCrear.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent launchCreate = new Intent(getApplicationContext(),
                                CreateAnimeActivity.class);
                        startActivityForResult(launchCreate, Code_Create_Anime);
                    }
                });
                dialogoCrear.setNegativeButton("Cancelar", null);
                dialogoCrear.show();
            }
        });

        menuLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                if (id == R.id.fragment_credits) {
                    Intent launchCreate = new Intent(getApplicationContext(),
                            CreditsActivity.class);
                    startActivity(launchCreate);
                    item.setChecked(false);
                }

                return false;
            }
        });
    }
}
