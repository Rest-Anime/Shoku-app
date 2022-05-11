package com.example.appAnime.activities.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.appAnime.R;
import com.example.appAnime.databinding.FragmentRegisterBinding;
import com.example.appAnime.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    Context context;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String user = binding.user.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty() || user.isEmpty()) {
                    Toast.makeText(context,
                            "No puede haber campos vacios",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context,
                                "Se ha producido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(context, "Se ha creado una cuenta " +
                                    "nueva",
                            Toast.LENGTH_LONG).show();
                    Usuario usuario = new Usuario(user);
                    db.collection("usuarios").document(task.getResult().getUser().getUid()).set(usuario.setFirestore());
                });
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
