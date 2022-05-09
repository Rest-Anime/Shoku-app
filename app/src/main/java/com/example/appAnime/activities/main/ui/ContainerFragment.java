package com.example.appAnime.activities.main.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appAnime.adapter.TabAdapter;
import com.example.appAnime.databinding.FragmentContainerBinding;

public class ContainerFragment extends Fragment {

    FragmentContainerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContainerBinding.inflate(getLayoutInflater());
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        TabAdapter tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(new ListFragment(), "ALL");
        tabAdapter.addFragment(new ListFragment(), "2");
        tabAdapter.addFragment(new ListFragment(), "3");

        binding.viewPager.setAdapter(tabAdapter);
        return binding.getRoot();

    }
}