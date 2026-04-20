package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class PruebasActivity extends AppCompatActivity {

    private ImageButton ibHome, ibInsertar, ibEditar, ibEliminar, ibMostrarTodos, ibBuscar, ibLimpiarBD;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pruebas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        ibHome = findViewById(R.id.ibHome);
        ibInsertar = findViewById(R.id.ibInsertar);
        ibEditar = findViewById(R.id.ibEditar);
        ibEliminar = findViewById(R.id.ibEliminar);
        ibMostrarTodos = findViewById(R.id.ibMostrarTodos);
        ibBuscar = findViewById(R.id.ibBuscar);
        ibLimpiarBD = findViewById(R.id.ibLimpiarBD);
        tvInfo = findViewById(R.id.tvInfo);

        // Programar botón Home
        ibHome.setOnClickListener(v -> {
            Intent intent = new Intent(PruebasActivity.this, MainActivity.class);
            startActivity(intent);
            // No llamamos a finish() para que la actividad Pruebas permanezca en la pila
        });

        // Aquí puedes programar el resto de los botones
        ibInsertar.setOnClickListener(v -> {
            String[] entidades = {
                    "Alimento", "CatGasto", "Cliente", "Especie", "Estanque",
                    "Gasto", "Lote", "Proveedor", "RegAlimentacion", "RegReubicacion", "Venta"
            };

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PruebasActivity.this);
            builder.setTitle("Seleccione una entidad para insertar");
            builder.setItems(entidades, (dialog, which) -> {
                String seleccion = entidades[which];
                android.widget.Toast.makeText(PruebasActivity.this, "seleccionaste " + seleccion, android.widget.Toast.LENGTH_SHORT).show();
            });
            builder.create().show();
        });
    }
}