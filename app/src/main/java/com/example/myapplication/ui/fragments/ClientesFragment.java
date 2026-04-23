package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.ClientesAdapter;
import com.example.myapplication.data.local.entity.Cliente;
import com.example.myapplication.data.repository.ClienteRep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientesFragment extends Fragment {

    private RecyclerView rvClientes;
    private ClientesAdapter adapter;
    private ClienteRep repository;
    private FloatingActionButton fabAdd;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        rvClientes = view.findViewById(R.id.rvClientes);
        fabAdd = view.findViewById(R.id.fabAddCliente);

        repository = new ClienteRep(requireActivity().getApplication());
        setupRecyclerView();

        fabAdd.setOnClickListener(v -> showClienteDialog(null));

        loadClientes();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new ClientesAdapter(new ClientesAdapter.OnClienteClickListener() {
            @Override
            public void onEditClick(Cliente cliente) {
                showClienteDialog(cliente);
            }

            @Override
            public void onDeleteClick(Cliente cliente) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar Cliente")
                        .setMessage("¿Estás seguro de eliminar a " + cliente.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            executor.execute(() -> {
                                repository.delete(cliente);
                                try { Thread.sleep(100); } catch (InterruptedException e) {}
                                loadClientes();
                            });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onWhatsAppClick(Cliente cliente) {
                if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
                    abrirWhatsApp(cliente.getTelefono());
                } else {
                    Toast.makeText(getContext(), "Sin número de teléfono", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMapsClick(Cliente cliente) {
                String url = (cliente.getDireccion() != null && cliente.getDireccion().startsWith("http"))
                        ? cliente.getDireccion()
                        : "https://www.google.com/maps";

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    if (url.equals("https://www.google.com/maps")) {
                        Toast.makeText(getContext(), "Busca la ubicación, copia el link y cárgalo editando el cliente", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "No se pudo abrir Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rvClientes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClientes.setAdapter(adapter);
    }

    private void loadClientes() {
        executor.execute(() -> {
            List<Cliente> lista = repository.getAllClientes();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setClientes(lista));
            }
        });
    }

    private void showClienteDialog(@Nullable Cliente clienteExistente) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_cliente, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etTelefono = dialogView.findViewById(R.id.etTelefono);
        EditText etUrl = dialogView.findViewById(R.id.etDireccion);

        if (clienteExistente != null) {
            etNombre.setText(clienteExistente.getNombre());
            etTelefono.setText(clienteExistente.getTelefono());
            etUrl.setText(clienteExistente.getDireccion());
        }

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String telefono = etTelefono.getText().toString().trim();
                    String url = etUrl.getText().toString().trim();

                    if (nombre.isEmpty()) {
                        Toast.makeText(getContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    executor.execute(() -> {
                        if (clienteExistente == null) {
                            Cliente nuevo = new Cliente(nombre, url, telefono);
                            repository.insert(nuevo);
                        } else {
                            clienteExistente.setNombre(nombre);
                            clienteExistente.setTelefono(telefono);
                            clienteExistente.setDireccion(url);
                            repository.update(clienteExistente);
                        }
                        try { Thread.sleep(200); } catch (InterruptedException e) {}
                        loadClientes();
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirWhatsApp(String telefono) {
        try {
            String numero = telefono.replaceAll("[^0-9]", "");
            String url = "https://api.whatsapp.com/send?phone=" + numero;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }
}