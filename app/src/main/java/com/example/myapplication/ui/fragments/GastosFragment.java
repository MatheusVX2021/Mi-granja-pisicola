package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.CatGasto;
import com.example.myapplication.data.local.entity.Gasto;
import com.example.myapplication.data.local.entity.GastoWithCategory;
import com.example.myapplication.data.repository.CatGastoRep;
import com.example.myapplication.data.repository.GastoRep;
import com.example.myapplication.ui.adapter.GastoAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GastosFragment extends Fragment {
    private RecyclerView rvGastos;
    private FloatingActionButton fabAddGasto;
    private GastoAdapter adapter;
    private GastoRep gastoRep;
    private CatGastoRep catGastoRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);
        
        gastoRep = new GastoRep(requireActivity().getApplication());
        catGastoRep = new CatGastoRep(requireActivity().getApplication());

        rvGastos = view.findViewById(R.id.rvGastos);
        fabAddGasto = view.findViewById(R.id.fabAddGasto);

        rvGastos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GastoAdapter(new ArrayList<>(), new GastoAdapter.OnGastoClickListener() {
            @Override
            public void onEditClick(GastoWithCategory gasto) {
                mostrarDialogoGasto(gasto);
            }

            @Override
            public void onDeleteClick(GastoWithCategory gasto) {
                executor.execute(() -> {
                    gastoRep.delete(gasto.gasto);
                });
            }
        });
        rvGastos.setAdapter(adapter);
        
        fabAddGasto.setOnClickListener(v -> mostrarDialogoGasto(null));

        gastoRep.getAllGastosWithCategory().observe(getViewLifecycleOwner(), lista -> {
            adapter.setGastos(lista);
        });

        return view;
    }

    private void cargarGastos() {
        // Ya no es necesario cargar manualmente gracias a LiveData
    }

    private void mostrarDialogoGasto(GastoWithCategory gastoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_gasto, null);
        builder.setView(view);

        EditText etMonto = view.findViewById(R.id.etMonto);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Spinner spCategoria = view.findViewById(R.id.spCategoria);
        EditText etFecha = view.findViewById(R.id.etFecha);

        // Configurar fecha por defecto
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        if (gastoExistente != null) {
            etMonto.setText(String.valueOf(gastoExistente.gasto.getMonto()));
            etDescripcion.setText(gastoExistente.gasto.getDescripcion());
            etFecha.setText(gastoExistente.gasto.getFecha());
            builder.setTitle("Editar Gasto");
        } else {
            etFecha.setText(sdf.format(new Date()));
            builder.setTitle("Nuevo Gasto");
        }

        etFecha.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etFecha.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Cargar categorías en el Spinner
        executor.execute(() -> {
            List<CatGasto> categorias = catGastoRep.getAllCategorias();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<CatGasto> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categorias);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategoria.setAdapter(spinnerAdapter);

                    if (gastoExistente != null && gastoExistente.category != null) {
                        for (int i = 0; i < categorias.size(); i++) {
                            if (categorias.get(i).getId() == gastoExistente.category.getId()) {
                                spCategoria.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String montoStr = etMonto.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
            CatGasto categoria = (CatGasto) spCategoria.getSelectedItem();

            if (montoStr.isEmpty() || categoria == null) {
                Toast.makeText(getContext(), "Monto y categoría son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double monto = Double.parseDouble(montoStr);
            executor.execute(() -> {
                if (gastoExistente == null) {
                    Gasto nuevoGasto = new Gasto(categoria.getId(), monto, descripcion, fecha);
                    gastoRep.insert(nuevoGasto);
                } else {
                    Gasto g = gastoExistente.gasto;
                    g.setIdCatGasto(categoria.getId());
                    g.setMonto(monto);
                    g.setDescripcion(descripcion);
                    g.setFecha(fecha);
                    gastoRep.update(g);
                }
            });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
