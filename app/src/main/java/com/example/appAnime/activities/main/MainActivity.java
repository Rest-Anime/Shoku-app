package com.example.appAnime.activities.main;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appAnime.R;
import com.example.appAnime.activities.login.LoginActivity;
import com.example.appAnime.activities.main.ui.ListFragment;
import com.example.appAnime.activities.maps.MapsActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.databinding.ActivityMainBinding;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public boolean listaEleccion, visualizarLista, userAdmin;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView menuLateral;
    RecyclerView recyclerView;
    ArrayList<Anime> animeList;
    AnimeAdapter animeAdapter;
    View cabecera;
    MediaPlayer mediaPlayer;
    ToggleButton loop;
    ToggleButton playMusic;
    Retrofit retrofit;
    TextView songName, userName;
    int counter = 0;
    Button play;
    FirebaseAuth auth;
    int Code_Create_Anime = 2;
    LinearLayout visualizationMode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityMainBinding binding;
    public Usuario usuario = new Usuario();

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
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        menuLateral = findViewById(R.id.navigator);
        cabecera = menuLateral.getHeaderView(0);
        userName = cabecera.findViewById(R.id.actividadLbl);
        animeList = new ArrayList<Anime>();
        recyclerView = findViewById(R.id.rwr);
        toolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.dl);
        loop = cabecera.findViewById(R.id.playM);
        listaEleccion = false;
        visualizarLista = true;
        userAdmin = false;
        userName.setText(usuario.getUsuario());


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

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, homeFrag)
        // .commit()

        //region BOTNAV
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navBot);
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navigation, navController);
        //endregion

        //region RETROFIT
        retrofit = new Retrofit.Builder().baseUrl("https://proyect-anime-5daac-default-rtdb" +
                ".firebaseio.com/").addConverterFactory(GsonConverterFactory.create()).build();
        //endregion

        //region MUSICA
        songName = cabecera.findViewById(R.id.songName);
        playMusic = cabecera.findViewById(R.id.playM);
        playMusic.setChecked(true);
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

        //endregion

        //region Navigator
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_abrir, R.string.drawer_cerrar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
                    case R.id.faq:
                        Intent faqIntent = new Intent(getApplicationContext(), FaqActivity.class);
                        startActivity(faqIntent);
                        finish();
                        break;
                    case R.id.visualization:
                        if (visualizarLista) {
                            visualizarLista = false;
                            item.setIcon(R.drawable.ic_baseline_view_module_24);
                        } else {
                            visualizarLista = true;
                            item.setIcon(R.drawable.ic_baseline_view_day_24);
                        }
                        break;
                }
                return false;
            }
        });
        //endregion
    }
}
