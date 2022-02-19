package com.example.appAnime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView usuario;
    TextView password;
    Button login;
    Button signUp;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = findViewById(R.id.usuarioTxt);
        password = findViewById(R.id.passwordTxt);
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            user.reload();
        }
        signUp.setOnClickListener(view -> {
            if (!(usuario.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
                auth.createUserWithEmailAndPassword(usuario.getText().toString(),
                        password.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha creado una cuenta " +
                                        "nueva",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "ha habido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        login.setOnClickListener(view -> {
            if (!(usuario.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
                auth.signInWithEmailAndPassword(usuario.getText().toString(),
                        password.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha logeado " +
                                        "correctamente",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "ha habido un error " + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}