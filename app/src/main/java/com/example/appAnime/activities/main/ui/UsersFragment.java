package com.example.appAnime.activities.main.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.UsuarioAdapter;
import com.example.appAnime.databinding.FragmentUsersBinding;
import com.example.appAnime.model.Usuario;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class UsersFragment extends Fragment {

    FragmentUsersBinding binding;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UsuarioAdapter usuarioAdapter;
    private ArrayList<Usuario> usuarioList;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        usuarioList = new ArrayList<>();
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
}