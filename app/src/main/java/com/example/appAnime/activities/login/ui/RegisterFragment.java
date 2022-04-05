package com.example.appAnime.activities.login.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appAnime.activities.login.CallBackFragment;
import com.example.appAnime.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    CallBackFragment callBackFragment;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                    Toast.makeText(getContext(),
                            "No puede haber campos vacios",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Se ha producido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getContext(), "Se ha creado una cuenta " +
                                    "nueva",
                            Toast.LENGTH_LONG).show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("usuario", user);
                    db.collection("usuarios").document(task.getResult().getUser().getUid()).set(data);
                });
                if (callBackFragment != null) {
                    callBackFragment.changeLoginFragment();
                }
            }
        });
        return binding.getRoot();
    }

    public void setCallBackFragment(CallBackFragment callBackFragment) {
        this.callBackFragment = callBackFragment;
    }
}
