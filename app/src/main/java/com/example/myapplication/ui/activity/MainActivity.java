package com.example.myapplication.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Manejar el Splash Screen de Android 12+
        SplashScreen.installSplashScreen(this);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // No padding bottom because of NavView
            return insets;
        });

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navView, navController);
        }

        // Configurar Toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Ocultar título por defecto para usar nuestro TextView
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_ver_cat_gastos) {
            startActivity(new android.content.Intent(this, CatGastoActivity.class));
            return true;
        } else if (id == R.id.action_ver_especies) {
            startActivity(new android.content.Intent(this, EspecieListActivity.class));
            return true;
        } else if (id == R.id.action_abrir_registros) {
            startActivity(new android.content.Intent(this, RegistrosActivity.class));
            return true;
        } else if (id == R.id.action_delete_db) {
            mostrarDialogoConfirmacionEliminar();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoConfirmacionEliminar() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar base de datos")
                .setMessage("¿Seguro que quieres eliminar? Si lo haces no hay marcha atrás.")
                .setPositiveButton("OK", (dialog, which) -> mostrarDialogoSeguridad())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoSeguridad() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación de seguridad")
                .setMessage("Solo para asegurarnos que no presionaste borrar por accidente, ¿estás seguro?")
                .setPositiveButton("SÍ, ELIMINAR TODO", (dialog, which) -> eliminarBaseDeDatos())
                .setNegativeButton("No, cancelar", null)
                .show();
    }

    private void eliminarBaseDeDatos() {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            db.clearAllTables();
            
            // Reiniciar IDs (sqlite_sequence)
            db.runInTransaction(() -> {
                db.getOpenHelper().getWritableDatabase().execSQL("DELETE FROM sqlite_sequence");
            });

            runOnUiThread(() -> {
                android.widget.Toast.makeText(this, "Base de datos eliminada por completo", android.widget.Toast.LENGTH_LONG).show();
                // Reiniciar la actividad para refrescar fragmentos
                finish();
                startActivity(getIntent());
            });
        });
    }
}