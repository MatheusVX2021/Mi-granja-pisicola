package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.myapplication.fragments.ClientesFragment;
import com.example.myapplication.fragments.ProveedoresFragment;

public class PersonasPagerAdapter extends FragmentStateAdapter {

    public PersonasPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ProveedoresFragment();
        }
        return new ClientesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}