package com.example.myapplication.ui.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.CatGasto;
import com.example.myapplication.ui.adapter.CatGastoAdapter;
import com.example.myapplication.ui.adapter.ColorAdapter;
import com.example.myapplication.ui.adapter.IconAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CatGastoActivity extends AppCompatActivity {

    private RecyclerView rvCatGasto;
    private FloatingActionButton fabAdd;
    private CatGastoAdapter adapter;
    private AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Listas de ejemplo para iconos y colores
    private final List<String> listaIconos = Arrays.asList("ic_pez", "ic_pescado", "ic_alimentar", "ic_estanque", "ic_finanza", "ic_inventario");
    private final List<String> listaColores = Arrays.asList("#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9", "#DCEDC8", "#F0F4C3", "#FFF9C4", "#FFECB3", "#FFE0B2", "#FFCCBC");

    private String iconoSeleccionado = "ic_pez";
    private String colorSeleccionado = "#B2DFDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_gasto);
        
        db = AppDatabase.getDatabase(this);
        
        rvCatGasto = findViewById(R.id.rvCatGasto);
        fabAdd = findViewById(R.id.fabAddCatGasto);

        rvCatGasto.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CatGastoAdapter(new ArrayList<>(), new CatGastoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CatGasto cat) {
                mostrarDialogoCategoria(cat);
            }

            @Override
            public void onDeleteClick(CatGasto cat) {
                executor.execute(() -> {
                    db.catGastoDao().delete(cat);
                    cargarCategorias();
                });
            }
        });
        rvCatGasto.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> mostrarDialogoCategoria(null));

        cargarCategorias();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarCategorias() {
        executor.execute(() -> {
            List<CatGasto> lista = db.catGastoDao().getAllCategorias();
            runOnUiThread(() -> adapter.setLista(lista));
        });
    }

    private void mostrarDialogoCategoria(CatGasto catExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_cat_gasto, null);
        builder.setView(view);

        EditText etNombre = view.findViewById(R.id.etNombreCat);
        RecyclerView rvIconos = view.findViewById(R.id.rvIconos);
        RecyclerView rvColores = view.findViewById(R.id.rvColores);

        if (catExistente != null) {
            etNombre.setText(catExistente.getNombre());
            iconoSeleccionado = catExistente.getImagen();
            colorSeleccionado = catExistente.getColor();
        } else {
            iconoSeleccionado = "ic_pez";
            colorSeleccionado = "#B2DFDB";
        }

        // Configurar RV de Iconos
        rvIconos.setLayoutManager(new GridLayoutManager(this, 4));
        IconAdapter iconAdapter = new IconAdapter(listaIconos, iconName -> iconoSeleccionado = iconName);
        rvIconos.setAdapter(iconAdapter);
        iconAdapter.setSelectedIcon(iconoSeleccionado);
        
        // Configurar RV de Colores
        rvColores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(listaColores, colorHex -> colorSeleccionado = colorHex);
        rvColores.setAdapter(colorAdapter);
        colorAdapter.setSelectedColor(colorSeleccionado);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                if (catExistente == null) {
                    db.catGastoDao().insert(new CatGasto(nombre, iconoSeleccionado, colorSeleccionado));
                } else {
                    catExistente.setNombre(nombre);
                    catExistente.setImagen(iconoSeleccionado);
                    catExistente.setColor(colorSeleccionado);
                    db.catGastoDao().update(catExistente);
                }
                cargarCategorias();
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}