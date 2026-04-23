package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Alimento;
import com.example.myapplication.data.local.entity.AlimentoWithProveedor;
import com.example.myapplication.data.local.entity.Proveedor;
import com.example.myapplication.data.repository.AlimentoRep;
import com.example.myapplication.data.repository.ProveedorRep;
import com.example.myapplication.ui.adapter.AlimentoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlimentosFragment extends Fragment {
    private RecyclerView rvAlimentos;
    private FloatingActionButton fabAddAlimento;
    private AlimentoAdapter adapter;
    private AlimentoRep alimentoRep;
    private ProveedorRep proveedorRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alimentos, container, false);

        alimentoRep = new AlimentoRep(requireActivity().getApplication());
        proveedorRep = new ProveedorRep(requireActivity().getApplication());

        rvAlimentos = view.findViewById(R.id.rvAlimentos);
        fabAddAlimento = view.findViewById(R.id.fabAddAlimento);

        rvAlimentos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlimentoAdapter(new ArrayList<>(), new AlimentoAdapter.OnAlimentoClickListener() {
            @Override
            public void onEditClick(AlimentoWithProveedor alimento) {
                mostrarDialogoAlimento(alimento);
            }

            @Override
            public void onDeleteClick(AlimentoWithProveedor alimento) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar Alimento")
                        .setMessage("¿Estás seguro de que deseas eliminar el alimento \"" + alimento.alimento.getNombre() + "\"?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            executor.execute(() -> {
                                alimentoRep.delete(alimento.alimento);
                            });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
        rvAlimentos.setAdapter(adapter);

        fabAddAlimento.setOnClickListener(v -> mostrarDialogoAlimento(null));

        alimentoRep.getAllAlimentosWithProveedor().observe(getViewLifecycleOwner(), lista -> {
            adapter.setAlimentos(lista);
        });

        return view;
    }

    private void mostrarDialogoAlimento(AlimentoWithProveedor alimentoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_alimento, null);
        builder.setView(view);

        Spinner spProveedor = view.findViewById(R.id.spProveedor);
        EditText etNombre = view.findViewById(R.id.etNombreAlimento);
        Spinner spTipo = view.findViewById(R.id.spTipoAlimento);
        EditText etUnidades = view.findViewById(R.id.etUnidades);
        EditText etPeso = view.findViewById(R.id.etPeso);

        // Configurar Spinner de Tipos (F1, F2, F3, F4)
        String[] tipos = {"F1", "F2", "F3", "F4"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipo);

        if (alimentoExistente != null) {
            etNombre.setText(alimentoExistente.alimento.getNombre());
            etUnidades.setText(String.valueOf(alimentoExistente.alimento.getUnidades()));
            etPeso.setText(String.valueOf(alimentoExistente.alimento.getPeso()));
            
            for(int i=0; i<tipos.length; i++) {
                if(tipos[i].equals(alimentoExistente.alimento.getTipo())) {
                    spTipo.setSelection(i);
                    break;
                }
            }
        }
        // Cargar proveedores
        executor.execute(() -> {
            List<Proveedor> proveedores = proveedorRep.getAllProveedores();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<Proveedor> adapterProv = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, proveedores);
                    adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProveedor.setAdapter(adapterProv);

                    if (alimentoExistente != null && alimentoExistente.proveedor != null) {
                        for (int i = 0; i < proveedores.size(); i++) {
                            if (proveedores.get(i).getId() == alimentoExistente.proveedor.getId()) {
                                spProveedor.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String tipo = spTipo.getSelectedItem().toString();
            String uniStr = etUnidades.getText().toString().trim();
            String pesoStr = etPeso.getText().toString().trim();
            Proveedor proveedor = (Proveedor) spProveedor.getSelectedItem();

            if (nombre.isEmpty() || uniStr.isEmpty() || pesoStr.isEmpty() || proveedor == null) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int unidades = Integer.parseInt(uniStr);
            int peso = Integer.parseInt(pesoStr);

            executor.execute(() -> {
                if (alimentoExistente == null) {
                    Alimento nuevo = new Alimento(proveedor.getId(), nombre, tipo, unidades, peso);
                    alimentoRep.insert(nuevo);
                } else {
                    Alimento a = alimentoExistente.alimento;
                    a.setIdProveedor(proveedor.getId());
                    a.setNombre(nombre);
                    a.setTipo(tipo);
                    a.setUnidades(unidades);
                    a.setPeso(peso);
                    alimentoRep.update(a);
                }
            });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
