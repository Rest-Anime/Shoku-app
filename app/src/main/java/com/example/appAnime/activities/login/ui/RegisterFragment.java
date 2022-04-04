package com.example.appAnime.activities.login.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appAnime.R;
import com.example.appAnime.activities.login.CallBackFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    CallBackFragment callBackFragment;
    EditText email, password, user;
    Button register;
    View view;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        user = view.findViewById(R.id.user);
        auth = FirebaseAuth.getInstance();
        register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || user.getText().toString().isEmpty()) {
                    System.out.println("error");
                    return;
                }
                auth.createUserWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString()).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "ha habido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getContext(), "Se ha creado una cuenta " +
                                    "nueva",
                            Toast.LENGTH_LONG).show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("usuario", user.getText().toString());
                    db.collection("usuarios").document(task.getResult().getUser().getUid()).set(data);
                });
                if (callBackFragment != null) {
                    callBackFragment.changeLoginFragment();
                }
            }
        });
        return view;
    }

    public void setCallBackFragment(CallBackFragment callBackFragment) {
        this.callBackFragment = callBackFragment;
    }
}
