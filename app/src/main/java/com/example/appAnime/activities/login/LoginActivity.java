package com.example.appAnime.activities.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appAnime.R;
import com.example.appAnime.activities.login.ui.LoginFragment;
import com.example.appAnime.activities.login.ui.RegisterFragment;


public class LoginActivity extends AppCompatActivity implements CallBackFragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginFragment();
    }

    public void loginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.setCallBackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
        fragmentTransaction.commit();
    }

    public void registerFragment() {
        RegisterFragment fragment = new RegisterFragment();
        fragment.setCallBackFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void changeRegisterFragment() {
        registerFragment();
    }

    @Override
    public void changeLoginFragment() {
        loginFragment();
    }
}