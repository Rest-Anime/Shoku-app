package com.example.appAnime.activities.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appAnime.activities.login.CallBackFragment;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    CallBackFragment callBackFragment;
    FirebaseAuth auth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(new View.OnClickListener() {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            @Override
            public void onClick(View view) {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(),
                            "No puede haber campos vacios",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Se ha producido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getContext(), "Se ha logeado " +
                                    "correctamente",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("user", task.getResult().getUser());
                    startActivity(intent);
                });
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBackFragment != null) {
                    callBackFragment.changeRegisterFragment();
                }
            }
        });
        return binding.getRoot();
    }

    public void setCallBackFragment(CallBackFragment callBackFragment) {
        this.callBackFragment = callBackFragment;
    }
}
