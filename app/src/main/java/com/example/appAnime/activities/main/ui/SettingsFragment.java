package com.example.appAnime.activities.main.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.appAnime.R;
import com.example.appAnime.activities.main.MainActivity;

public class SettingsFragment extends Fragment {
    View root;
    Boolean themeMain, themeDark, themeSecond;
    Button btmo, btmb, btmw, btdo, btdb, btddg, btsm, btsg, btsdg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        btmo = (Button) root.findViewById(R.id.btnThemeMainOrange);

        btmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setTheme(R.style.SecondaryTheme_AppCoffee);
                System.out.println("ESEEEEEEEEEEEEEEEEEEE");
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}