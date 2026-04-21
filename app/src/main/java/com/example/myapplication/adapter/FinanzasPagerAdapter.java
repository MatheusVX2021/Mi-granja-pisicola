package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.myapplication.fragments.VentasFragment;
import com.example.myapplication.fragments.GastosFragment;

public class FinanzasPagerAdapter extends FragmentStateAdapter {

    public FinanzasPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new GastosFragment();
        }
        return new VentasFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}