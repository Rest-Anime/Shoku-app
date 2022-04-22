package com.example.appAnime.activities.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.detail.CreateAnimeActivity;
import com.example.appAnime.activities.detail.DetailActivity;
import com.example.appAnime.activities.login.LoginActivity;
import com.example.appAnime.activities.main.ui.HomeFragment;
import com.example.appAnime.activities.main.ui.ProfileFragment;
import com.example.appAnime.activities.main.ui.SettingsFragment;
import com.example.appAnime.activities.maps.MapsActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.model.Anime;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView menuLateral;

    BottomNavigationView bottonNavigationView;
    HomeFragment homeFrag = new HomeFragment();
    ProfileFragment profileFrag = new ProfileFragment();
    SettingsFragment settingsFrag = new SettingsFragment();

    MenuItem searchItem;
    RecyclerView recyclerView;
    ArrayList<Anime> listaFiltrados = new ArrayList<>();
    ArrayList<Anime> animeList;
    ImageView menuLatImg;
    AnimeAdapter animeAdapter, animeAdapterCompact;
    View cabecera;
    FloatingActionButton fab;
    MediaPlayer mediaPlayer;
    ToggleButton loop;
    ToggleButton playMusic;
    Retrofit retrofit;
    TextView songName, userName;
    int counter = 0;
    Button play;
    Animation rotopen;
    Animation rotclose;
    Animation bot;
    Animation tobot;
    AppBarConfiguration appbarconfig;
    FirebaseDatabase bbdd;
    FirebaseAuth auth;
    FirebaseUser user;
    int Code_Create_Anime = 2;
    boolean listaEleccion, visualizarLista, userAdmin;
    LinearLayout visualizationMode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            Context context = getApplicationContext();
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(context, Uri.parse("https://www.youtube" +
                        ".com/watch?v=HnbwPbXY450&list=RDHnbwPbXY450&start_radio=1&ab_channel=R" +
                        "-Maldonado84"));
                mediaPlayer.prepare();
            } catch (Exception exception) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        if (listaEleccion == false) {
            Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
            launchInfo.putExtra("anime", animeList.get(pos));
            launchInfo.putExtra("pos", pos);
            startActivity(launchInfo);
        } else {
            Intent launchInfo = new Intent(getApplicationContext(), DetailActivity.class);
            launchInfo.putExtra("anime", listaFiltrados.get(pos));
            startActivity(launchInfo);
        }
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
        searchItem = menu.findItem(R.id.app_bar_search);
        System.out.println(searchItem == null);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // Este método se llama cuando se pulsa el botón de buscar.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // Este método se llama cada vez que el texto de la barra de búsqueda cambia
            @Override
            public boolean onQueryTextChange(String newText) {
                listaFiltrados.clear();
                for (Anime a : animeList) {
                    if (a.getTitulo().toUpperCase().startsWith(newText.toUpperCase())) {
                        listaFiltrados.add(a);
                        listaEleccion = true;
                    }
                }
                animeAdapter.setAnimeList(listaFiltrados);
                animeAdapter.notifyDataSetChanged();

                return false;
            }

        });
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuLateral = findViewById(R.id.navigator);
        cabecera = menuLateral.getHeaderView(0);
        userName = cabecera.findViewById(R.id.actividadLbl);
        animeList = new ArrayList<Anime>();
        recyclerView = findViewById(R.id.rwr);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fabMenu);
        drawer = findViewById(R.id.dl);
        loop = cabecera.findViewById(R.id.playM);
        listaEleccion = false;
        visualizarLista = true;
        userAdmin = false;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userName.setText(user.getEmail());

        /*
        visualizationMode = drawer.findViewById(R.id.visualization);
        ImageView a = visualizationMode.findViewById(R.id.imgListMode);
        ImageView b = visualizationMode.findViewById(R.id.imgGridMode);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

        bottonNavigationView = findViewById(R.id.navBot);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, homeFrag)
        // .commit();
        BadgeDrawable badgeDrawable = bottonNavigationView.getOrCreateBadge(R.id.profile);
        badgeDrawable.setNumber(3);

        if (userAdmin) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }

        bottonNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //setContentView(R.layout.activity_blank);
                switch (item.getItemId()) {
                    case R.id.home:
                        recyclerView.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        searchItem.setVisible(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer
                                , homeFrag).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id
                        // .fragContainer, homeFrag).commit();
                        return true;
                    case R.id.profile:
                        recyclerView.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.INVISIBLE);
                        searchItem.setVisible(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer
                                , profileFrag).commit();
                        return true;
                    case R.id.settings:
                        recyclerView.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.INVISIBLE);
                        searchItem.setVisible(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer
                                , settingsFrag).commit();
                        return true;
                }

                return false;
            }
        });

        appbarconfig = new AppBarConfiguration.Builder(R.id.credits, R.id.maps
                , R.id.logout).build();

        //CREACION OBJETO RETROFIT PARA INSTANCIAR LA API
        retrofit = new Retrofit.Builder().baseUrl("https://proyect-anime-5daac-default-rtdb" +
                ".firebaseio.com/").addConverterFactory(GsonConverterFactory.create()).build();
        //RETROFIT CREA CLASES ASOCIADAS PARA CONECTARNOS CON LOS ENDPOINT

        songName = cabecera.findViewById(R.id.songName);
        playMusic = cabecera.findViewById(R.id.playM);
        playMusic.setChecked(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_abrir, R.string.drawer_cerrar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bbdd = FirebaseDatabase.getInstance();
        recyclerView.setAdapter(animeAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("animes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                animeList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Anime anime = doc.toObject(Anime.class);
                    anime.setUid(doc.getId());
                    animeList.add(anime);
                }
                Log.e("a", animeList.toString());
                animeAdapter = new AnimeAdapter(animeList, function);
            }
        });


        rotopen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotclose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        bot = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        tobot = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        if (visualizarLista) {
            recyclerView.setAdapter(animeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));

        } else {
            recyclerView.setAdapter(animeAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
        }

        /* animeAdapter = new AnimeAdapter(animeList, function);
        recyclerView.setAdapter(animeAdapter);*/


        //MUSICA
        songName.setText("Ghibli Soundtrack");

        //linea original que crea la cancion a reproducir pillada de un array con objetos cancion
        //mediaPlayer = MediaPlayer.create(this, canciones.get(contador).getPista());

        /*
        mediaPlayer = MediaPlayer.create(this, R.raw.ghibliost);
        mediaPlayer.setVolume(3.0f, 3.0f);
        mediaPlayer.start();
        */


        String url = "https://www.youtube.com/watch?v=HnbwPbXY450&list=RDHnbwPbXY450&start_radio" +
                "=1&ab_channel=R-Maldonado84"; // your URL here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                switch (id) {
                    case R.id.credits:
                        Intent launchCreate = new Intent(getApplicationContext(),
                                CreditsActivity.class);
                        startActivity(launchCreate);
                        item.setChecked(false);
                        break;
                    case R.id.maps:
                        Intent launchMap = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(launchMap);
                        item.setChecked(false);
                        break;
                    case R.id.logout:
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.visualization:
                        if (visualizarLista) {
                            visualizarLista = false;
                            recyclerView.setAdapter(animeAdapter);
                            item.setIcon(R.drawable.ic_baseline_view_module_24);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.VERTICAL));
                        } else {
                            visualizarLista = true;
                            recyclerView.setAdapter(animeAdapter);
                            item.setIcon(R.drawable.ic_baseline_view_day_24);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.VERTICAL));
                        }
                        break;
                }
                return false;
            }
        });
    }
}
