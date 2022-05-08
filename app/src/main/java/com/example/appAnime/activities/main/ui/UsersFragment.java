package com.example.appAnime.activities.main.ui;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.UsuarioAdapter;
import com.example.appAnime.databinding.FragmentUsersBinding;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Usuario;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;


public class UsersFragment extends Fragment {

    FragmentUsersBinding binding;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UsuarioAdapter usuarioAdapter;
    private ArrayList<Usuario> usuarioList = new ArrayList<>(), listaFiltrados = new ArrayList<>();
    private RecyclerView recyclerView;

    private EventsInterface function = (pos) -> {
        System.out.println(usuarioList.get(pos));
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(getLayoutInflater());
        recyclerView = binding.usuariosList;
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        db.collection("usuarios").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                usuarioList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Usuario usuario = doc.toObject(Usuario.class);
                    usuario.setUID(doc.getId());
                    usuarioList.add(usuario);
                }
                Log.e("Lista", usuarioList.toString());
                usuarioAdapter = new UsuarioAdapter(usuarioList, function);
                recyclerView.setAdapter(usuarioAdapter);
            }
        });
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
                for (Usuario usuario : usuarioList) {
                    if (usuario.getUsuario().toUpperCase().startsWith(newText.toUpperCase())){
                        listaFiltrados.add(usuario);
                    }
                }
                usuarioAdapter.setUsuariosList(listaFiltrados);
                usuarioAdapter.notifyDataSetChanged();

                return false;
            }

        });
    }
}