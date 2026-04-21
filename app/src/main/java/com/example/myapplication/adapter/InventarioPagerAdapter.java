package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.myapplication.fragments.LotesFragment;
import com.example.myapplication.fragments.AlimentosFragment;

public class InventarioPagerAdapter extends FragmentStateAdapter {

    public InventarioPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AlimentosFragment();
        }
        return new LotesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}