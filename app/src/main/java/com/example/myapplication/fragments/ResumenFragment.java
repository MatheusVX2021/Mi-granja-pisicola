package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.ui.EspecieListActivity;

public class ResumenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        ImageButton btnIrAEspecies = view.findViewById(R.id.btnIrAEspecies);
        btnIrAEspecies.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EspecieListActivity.class);
            startActivity(intent);
        });

        return view;
    }
}