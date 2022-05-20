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
import android.widget.ImageView;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.login.LoginActivity;
import com.example.appAnime.activities.maps.MapsActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

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
    MediaPlayer mediaPlayer = new MediaPlayer();
    ToggleButton loop;
    ToggleButton playMusic;
    Retrofit retrofit;
    ImageView pfp;
    TextView songName, userName;
    int counter = 0;
    Button play;
    FirebaseAuth auth;
    int Code_Create_Anime = 2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Usuario usuario = new Usuario();



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
        pfp = cabecera.findViewById(R.id.pfp);
        animeList = new ArrayList<Anime>();
        recyclerView = findViewById(R.id.rwr);
        toolbar = findViewById(R.id.toolbar2);

        drawer = findViewById(R.id.dl);
        loop = cabecera.findViewById(R.id.playM);
        listaEleccion = false;
        visualizarLista = true;
        userAdmin = false;
        userName.setText(usuario.getUsuario());
        if (usuario.getFoto() != null) {
            String urlImagen = String.valueOf(usuario.getFoto());
            Picasso.get().load(urlImagen).into(pfp);
        } else {
            Picasso.get().load(R.drawable.emptyuser).into(pfp);
        }

        //region BOTNAV
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navBot);
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navigation, navController);
        //endregion

        //region MUSICA
        songName = cabecera.findViewById(R.id.songName);
        playMusic = cabecera.findViewById(R.id.playM);
        playMusic.setChecked(true);
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        });
        songName.setText("Ghibli Soundtrack");
        mediaPlayer = MediaPlayer.create(this, R.raw.ghibliost);
        mediaPlayer.setVolume(3.0f, 3.0f);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            loop.setText("On");
            counter = counter;
            mediaPlayer.setLooping(true);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
        });
        //endregion

        //region NAVIGATOR
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
                        item.setChecked(false);
                        break;
                }
                return false;
            }
        });
        //endregion
    }
}
