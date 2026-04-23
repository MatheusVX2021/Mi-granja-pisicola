package com.example.myapplication.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.utils.FileUtils;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.EspecieAdapter;
import com.example.myapplication.data.local.entity.Especie;
import com.example.myapplication.data.repository.EspecieRep;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EspecieListActivity extends AppCompatActivity implements EspecieAdapter.OnEspecieClickListener {

    private RecyclerView rvEspecies;
    private EspecieAdapter adapter;
    private EspecieRep especieRep;
    private List<Especie> listaEspecies = new ArrayList<>();
    private Especie especieSeleccionadaParaImagen;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<String[]> openDocument = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null && especieSeleccionadaParaImagen != null) {
                    executorService.execute(() -> {
                        String localUri = FileUtils.saveImageToInternalStorage(this, uri);
                        if (localUri != null) {
                            especieSeleccionadaParaImagen.setImagen(localUri);
                            especieRep.update(especieSeleccionadaParaImagen, this::cargarEspecies);
                        }
                    });
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especie_list);

        especieRep = new EspecieRep(getApplication());
        rvEspecies = findViewById(R.id.rvEspecies);
        rvEspecies.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new EspecieAdapter(listaEspecies, this);
        rvEspecies.setAdapter(adapter);

        // Configurar botón volver personalizado
        View btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        FloatingActionButton fab = findViewById(R.id.fabAddEspecie);
        fab.setOnClickListener(v -> mostrarDialogoEspecie(null));

        cargarEspecies();
    }

    private void cargarEspecies() {
        executorService.execute(() -> {
            List<Especie> especies = especieRep.getAllEspecies();
            runOnUiThread(() -> adapter.setEspecies(especies));
        });
    }

    private void mostrarDialogoEspecie(Especie especieExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_especie, null);
        EditText etNombre = view.findViewById(R.id.etNombreEspecie);
        EditText etMaduracion = view.findViewById(R.id.etTm);

        if (especieExistente != null) {
            etNombre.setText(especieExistente.getNombre());
            etMaduracion.setText(String.valueOf(especieExistente.getTm()));
        }

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String maduracionStr = etMaduracion.getText().toString().trim();

            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            int tm = maduracionStr.isEmpty() ? 0 : Integer.parseInt(maduracionStr);

            if (especieExistente != null) {
                especieExistente.setNombre(nombre);
                especieExistente.setTm(tm);
                especieRep.update(especieExistente, this::cargarEspecies);
            } else {
                Especie nuevaEspecie = new Especie(nombre, null, tm);
                especieRep.insert(nuevaEspecie, this::cargarEspecies);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onEditClick(Especie especie) {
        mostrarDialogoEspecie(especie);
    }

    @Override
    public void onDeleteClick(Especie especie) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Especie")
                .setMessage("¿Estás seguro de eliminar " + especie.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    especieRep.delete(especie, this::cargarEspecies);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onImageClick(Especie especie) {
        especieSeleccionadaParaImagen = especie;
        openDocument.launch(new String[]{"image/*"});
    }
}
