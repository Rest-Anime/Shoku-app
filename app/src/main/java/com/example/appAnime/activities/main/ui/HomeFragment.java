package com.example.appAnime.activities.main.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.detail.CreateAnimeActivity;
import com.example.appAnime.activities.detail.DetailActivity;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.databinding.FragmentHomeBinding;
import com.example.appAnime.model.Anime;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    final int CODE_CREATE_ANIME = 2;
    private FragmentHomeBinding binding;
    Context context;
    AnimeAdapter animeAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Anime> animeList = new ArrayList<>(), listaFiltrados = new ArrayList<>();
    boolean listaEleccion, visualizarLista;

    private EventsInterface function = (pos) -> {
        if (listaEleccion == false) {
            Intent launchInfo = new Intent(context, DetailActivity.class);
            launchInfo.putExtra("anime", animeList.get(pos));
            launchInfo.putExtra("pos", pos);
            startActivity(launchInfo);
        } else {
            Intent launchInfo = new Intent(context, DetailActivity.class);
            launchInfo.putExtra("anime", listaFiltrados.get(pos));
            startActivity(launchInfo);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        RecyclerView recyclerView = binding.rwr;

        //region FIRESTORE
        this.listaEleccion = ((MainActivity) getActivity()).listaEleccion;
        db.collection("animes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                animeList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Anime anime = doc.toObject(Anime.class);
                    anime.setUID(doc.getId());
                    animeList.add(anime);
                }
                Log.e("Lista", animeList.toString());
                animeAdapter = new AnimeAdapter(animeList, function);
                recyclerView.setAdapter(animeAdapter);
            }
        });
        //endregion

        //region Visualizacion Lista
        this.visualizarLista = ((MainActivity) getActivity()).visualizarLista;
        if (visualizarLista) {
            recyclerView.setAdapter(animeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL));

        } else {
            recyclerView.setAdapter(animeAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL));
        }
        //endregion

        //region Floating Button
        binding.fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogoCrear =
                        new MaterialAlertDialogBuilder(context);
                dialogoCrear.setIcon(R.drawable.mr_cast_thumb);
                dialogoCrear.setTitle("Operacion Crear Anime");
                dialogoCrear.setMessage("¿Estas seguro de que quieres crear un Anime?");
                dialogoCrear.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent launchCreate = new Intent(context,
                                CreateAnimeActivity.class);
                        startActivityForResult(launchCreate, CODE_CREATE_ANIME);
                    }
                });
                dialogoCrear.setNegativeButton("Cancelar", null);
                dialogoCrear.show();
            }
        });
        //endregion

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
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
    }
}