package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.repository.RegAlimentacionRep;
import com.example.myapplication.ui.adapter.RegAlimentacionAdapter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegAlimentacionFragment extends Fragment {
    private RecyclerView rvAlimentacion;
    private RegAlimentacionAdapter adapter;
    private RegAlimentacionRep regAlimentacionRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_alimentacion, container, false);
        rvAlimentacion = view.findViewById(R.id.rvRegAlimentacion);
        rvAlimentacion.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new RegAlimentacionAdapter(item -> {
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Eliminar Registro")
                    .setMessage("¿Estás seguro de eliminar este registro de alimentación?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        executor.execute(() -> {
                            com.example.myapplication.data.local.entity.RegAlimentacion reg = regAlimentacionRep.getAlimentacionById(item.getId());
                            if (reg != null) {
                                regAlimentacionRep.delete(reg);
                                cargarDatos();
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        rvAlimentacion.setAdapter(adapter);

        regAlimentacionRep = new RegAlimentacionRep(requireActivity().getApplication());

        cargarDatos();

        return view;
    }

    private void cargarDatos() {
        executor.execute(() -> {
            List<com.example.myapplication.data.model.RegAlimentacionUI> lista = regAlimentacionRep.getAllAlimentacionesUI();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(lista);
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }
}
