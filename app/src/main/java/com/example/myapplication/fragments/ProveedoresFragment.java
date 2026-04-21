package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.example.myapplication.adapter.ProveedoresAdapter;
import com.example.myapplication.data.local.entity.Proveedor;
import com.example.myapplication.data.repository.ProveedorRep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProveedoresFragment extends Fragment {

    private RecyclerView rvProveedores;
    private ProveedoresAdapter adapter;
    private ProveedorRep repository;
    private FloatingActionButton fabAdd;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proveedores, container, false);

        rvProveedores = view.findViewById(R.id.rvProveedores);
        fabAdd = view.findViewById(R.id.fabAddProveedor);

        repository = new ProveedorRep(requireActivity().getApplication());
        setupRecyclerView();

        fabAdd.setOnClickListener(v -> showProveedorDialog(null));

        loadProveedores();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new ProveedoresAdapter(new ProveedoresAdapter.OnProveedorClickListener() {
            @Override
            public void onEditClick(Proveedor proveedor) {
                showProveedorDialog(proveedor);
            }

            @Override
            public void onDeleteClick(Proveedor proveedor) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar Proveedor")
                        .setMessage("¿Estás seguro de eliminar a " + proveedor.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            executor.execute(() -> {
                                repository.delete(proveedor);
                                try { Thread.sleep(100); } catch (InterruptedException e) {}
                                loadProveedores();
                            });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onWhatsAppClick(Proveedor proveedor) {
                if (proveedor.getCelular() != null && !proveedor.getCelular().isEmpty()) {
                    abrirWhatsApp(proveedor.getCelular());
                } else {
                    Toast.makeText(getContext(), "Sin número de celular", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMapsClick(Proveedor proveedor) {
                String url = (proveedor.getDireccion() != null && proveedor.getDireccion().startsWith("http")) 
                        ? proveedor.getDireccion() 
                        : "https://www.google.com/maps";
                
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    if (url.equals("https://www.google.com/maps")) {
                        Toast.makeText(getContext(), "Busca la ubicación, copia el link y cárgalo editando el proveedor", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "No se pudo abrir Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rvProveedores.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProveedores.setAdapter(adapter);
    }

    private void loadProveedores() {
        executor.execute(() -> {
            List<Proveedor> lista = repository.getAllProveedores();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setProveedores(lista));
            }
        });
    }

    private void showProveedorDialog(@Nullable Proveedor proveedorExistente) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_proveedor, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreProveedor);
        Spinner spTipo = dialogView.findViewById(R.id.spTipoProveedor);
        EditText etCelular = dialogView.findViewById(R.id.etCelularProveedor);
        EditText etUrl = dialogView.findViewById(R.id.etUrlProveedor);

        String[] tipos = {"Huevos", "Alimento"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterSpinner);

        if (proveedorExistente != null) {
            etNombre.setText(proveedorExistente.getNombre());
            etCelular.setText(proveedorExistente.getCelular());
            etUrl.setText(proveedorExistente.getDireccion());
            if (proveedorExistente.getTipo().equalsIgnoreCase("Alimento")) {
                spTipo.setSelection(1);
            } else {
                spTipo.setSelection(0);
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(proveedorExistente == null ? "Nuevo Proveedor" : "Editar Proveedor")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String tipo = spTipo.getSelectedItem().toString();
                    String celular = etCelular.getText().toString().trim();
                    String url = etUrl.getText().toString().trim();

                    if (nombre.isEmpty()) {
                        Toast.makeText(getContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    executor.execute(() -> {
                        if (proveedorExistente == null) {
                            Proveedor nuevo = new Proveedor(nombre, tipo, url, celular);
                            repository.insert(nuevo);
                        } else {
                            proveedorExistente.setNombre(nombre);
                            proveedorExistente.setTipo(tipo);
                            proveedorExistente.setCelular(celular);
                            proveedorExistente.setDireccion(url);
                            repository.update(proveedorExistente);
                        }
                        try { Thread.sleep(200); } catch (InterruptedException e) {}
                        loadProveedores();
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirWhatsApp(String celular) {
        try {
            String numero = celular.replaceAll("[^0-9]", "");
            String url = "https://api.whatsapp.com/send?phone=" + numero;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }
}