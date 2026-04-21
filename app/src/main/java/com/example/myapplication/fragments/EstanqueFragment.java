package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.EstanqueAdapter;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.model.EstanqueUI;
import com.example.myapplication.data.repository.EstanqueRep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstanqueFragment extends Fragment implements EstanqueAdapter.OnEstanqueActionListener {

    private RecyclerView rvEstanques;
    private EstanqueAdapter adapter;
    private List<EstanqueUI> estanqueList = new ArrayList<>();
    private FloatingActionButton fabAddEstanque;
    private EstanqueRep estanqueRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private EstanqueUI estanqueSeleccionadoParaImagen;

    private final ActivityResultLauncher<String[]> openDocument = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null && estanqueSeleccionadoParaImagen != null) {
                    try {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                        
                        actualizarImagenEstanque(estanqueSeleccionadoParaImagen.getId(), uri.toString());
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al obtener permisos de imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estanque, container, false);
        
        rvEstanques = view.findViewById(R.id.rvEstanques);
        rvEstanques.setLayoutManager(new LinearLayoutManager(getContext()));
        
        fabAddEstanque = view.findViewById(R.id.fabAddEstanque);

        estanqueRep = new EstanqueRep(getActivity().getApplication());
        adapter = new EstanqueAdapter(estanqueList, this);
        rvEstanques.setAdapter(adapter);

        fabAddEstanque.setOnClickListener(v -> mostrarDialogoNuevoEstanque());
        
        return view;
    }

    private void mostrarDialogoNuevoEstanque() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nuevo Estanque");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_estanque, null);
        EditText etNombre = view.findViewById(R.id.etNombreEstanque);
        EditText etArea = view.findViewById(R.id.etAreaEstanque);

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String areaStr = etArea.getText().toString().trim();

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(areaStr)) {
                try {
                    double area = Double.parseDouble(areaStr);
                    Estanque nuevo = new Estanque(nombre, area, null);
                    estanqueRep.insert(nuevo, this::cargarDatosReales);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Área inválida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ambos campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onEdit(EstanqueUI estanqueUI, String nuevoNombre, double nuevaArea) {
        executor.execute(() -> {
            Estanque estanque = estanqueRep.getEstanqueById(estanqueUI.getId());
            if (estanque != null) {
                estanque.setNombre(nuevoNombre);
                estanque.setArea(nuevaArea);
                estanqueRep.update(estanque, this::cargarDatosReales);
            }
        });
    }

    @Override
    public void onDelete(EstanqueUI estanqueUI) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Estanque")
                .setMessage("¿Estás seguro de eliminar " + estanqueUI.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    executor.execute(() -> {
                        Estanque estanque = estanqueRep.getEstanqueById(estanqueUI.getId());
                        if (estanque != null) {
                            estanqueRep.delete(estanque, this::cargarDatosReales);
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onAddImage(EstanqueUI estanque) {
        estanqueSeleccionadoParaImagen = estanque;
        openDocument.launch(new String[]{"image/*"});
    }

    @Override
    public void onRemoveImage(EstanqueUI estanque) {
        actualizarImagenEstanque(estanque.getId(), null);
    }

    private void actualizarImagenEstanque(int id, String uri) {
        executor.execute(() -> {
            Estanque estanque = estanqueRep.getEstanqueById(id);
            if (estanque != null) {
                estanque.setImagen(uri);
                estanqueRep.update(estanque, this::cargarDatosReales);
            }
        });
    }

    @Override
    public void onAddLote(EstanqueUI estanque) {
        Toast.makeText(getContext(), "Añadiendo lote a " + estanque.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlimentar(EstanqueUI estanque) {
        Toast.makeText(getContext(), "Alimentando " + estanque.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosReales();
    }

    private void cargarDatosReales() {
        executor.execute(() -> {
            List<EstanqueUI> lista = estanqueRep.getEstanquesUI();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    estanqueList.clear();
                    estanqueList.addAll(lista);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }
}