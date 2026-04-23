package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.RegReubicacionAdapter;
import com.example.myapplication.data.local.database.AppDatabase;
import java.util.ArrayList;

import com.example.myapplication.data.local.entity.RegReubicacion;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegReubicacionFragment extends Fragment {
    private RecyclerView rvReubicacion;
    private RegReubicacionAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_reubicacion, container, false);
        rvReubicacion = view.findViewById(R.id.rvRegReubicacion);
        
        adapter = new RegReubicacionAdapter(new ArrayList<>(), item -> {
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Eliminar Registro")
                    .setMessage("¿Estás seguro de eliminar este registro de reubicación?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        executor.execute(() -> {
                            RegReubicacion reg = AppDatabase.getDatabase(requireContext()).regReubicacionDao().getReubicacionById(item.getIdRegReubicacion());
                            if (reg != null) {
                                AppDatabase.getDatabase(requireContext()).regReubicacionDao().delete(reg);
                                // No es necesario cargarDatos manualmente si usamos LiveData en el observer de abajo
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        rvReubicacion.setAdapter(adapter);

        AppDatabase.getDatabase(requireContext()).regReubicacionDao().getAllRegistrosUI().observe(getViewLifecycleOwner(), list -> {
            adapter.setList(list);
        });

        return view;
    }
}
