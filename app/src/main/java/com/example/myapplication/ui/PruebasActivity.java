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

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Alimento;
import com.example.myapplication.data.local.entity.CatGasto;
import com.example.myapplication.data.local.entity.Cliente;
import com.example.myapplication.data.local.entity.Especie;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.local.entity.Gasto;
import com.example.myapplication.data.local.entity.Lote;
import com.example.myapplication.data.local.entity.Proveedor;
import com.example.myapplication.data.local.entity.RegAlimentacion;
import com.example.myapplication.data.local.entity.RegReubicacion;
import com.example.myapplication.data.local.entity.Venta;
import com.example.myapplication.data.repository.AlimentoRep;
import com.example.myapplication.data.repository.CatGastoRep;
import com.example.myapplication.data.repository.ClienteRep;
import com.example.myapplication.data.repository.EspecieRep;
import com.example.myapplication.data.repository.EstanqueRep;
import com.example.myapplication.data.repository.GastoRep;
import com.example.myapplication.data.repository.LoteRep;
import com.example.myapplication.data.repository.ProveedorRep;
import com.example.myapplication.data.repository.RegAlimentacionRep;
import com.example.myapplication.data.repository.RegReubicacionRep;
import com.example.myapplication.data.repository.VentaRep;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PruebasActivity extends AppCompatActivity {

    private ImageButton ibHome, ibInsertar, ibEditar, ibEliminar, ibMostrarTodos, ibBuscar, ibLimpiarBD;
    private TextView tvInfo;
    private AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Repositorios
    private AlimentoRep alimentoRep;
    private CatGastoRep catGastoRep;
    private ClienteRep clienteRep;
    private EspecieRep especieRep;
    private EstanqueRep estanqueRep;
    private GastoRep gastoRep;
    private LoteRep loteRep;
    private ProveedorRep proveedorRep;
    private RegAlimentacionRep regAlimentacionRep;
    private RegReubicacionRep regReubicacionRep;
    private VentaRep ventaRep;

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

        db = AppDatabase.getDatabase(this);

        // Inicializar Repositorios
        alimentoRep = new AlimentoRep(getApplication());
        catGastoRep = new CatGastoRep(getApplication());
        clienteRep = new ClienteRep(getApplication());
        especieRep = new EspecieRep(getApplication());
        estanqueRep = new EstanqueRep(getApplication());
        gastoRep = new GastoRep(getApplication());
        loteRep = new LoteRep(getApplication());
        proveedorRep = new ProveedorRep(getApplication());
        regAlimentacionRep = new RegAlimentacionRep(getApplication());
        regReubicacionRep = new RegReubicacionRep(getApplication());
        ventaRep = new VentaRep(getApplication());

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
        ibInsertar.setOnClickListener(v -> mostrarDialogoSeleccionEntidad());
        ibEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
        ibMostrarTodos.setOnClickListener(v -> mostrarDialogoSeleccionMostrar());
        ibLimpiarBD.setOnClickListener(v -> confirmarLimpiarBD());
    }

    private void confirmarLimpiarBD() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar Base de Datos")
                .setMessage("¿Estás seguro de que deseas borrar TODOS los datos? Esta acción reiniciará los IDs a 1.")
                .setPositiveButton("Sí, limpiar", (dialog, which) -> limpiarBaseDeDatos())
                .setNegativeButton("No", null)
                .show();
    }

    private void limpiarBaseDeDatos() {
        executor.execute(() -> {
            try {
                db.clearAllTables();
                // Reiniciar los IDs en SQLite
                db.runInTransaction(() -> {
                    db.getOpenHelper().getWritableDatabase().execSQL("DELETE FROM sqlite_sequence");
                });
                runOnUiThread(() -> {
                    tvInfo.setText("Base de datos limpiada y IDs reiniciados.");
                    Toast.makeText(this, "Base de datos reiniciada", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al limpiar BD", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void mostrarDialogoEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar por ID");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText etEntidad = new EditText(this);
        etEntidad.setHint("Entidad (Ej: Estanque)");
        layout.addView(etEntidad);

        final EditText etId = new EditText(this);
        etId.setHint("ID a eliminar");
        layout.addView(etId);

        builder.setView(layout);
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String entidad = etEntidad.getText().toString().trim();
            String idStr = etId.getText().toString().trim();
            if (!entidad.isEmpty() && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    ejecutarEliminacion(entidad, id);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void ejecutarEliminacion(String entidad, int id) {
        executor.execute(() -> {
            String ent = entidad.toLowerCase();
            switch (ent) {
                case "alimento":
                    Alimento a = alimentoRep.getAlimentoById(id);
                    if (a != null) { alimentoRep.delete(a); mostrarMensaje("Eliminado Alimento " + id); mostrarTablaAlimento(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "catgasto":
                    CatGasto cg = catGastoRep.getCategoriaById(id);
                    if (cg != null) { catGastoRep.delete(cg); mostrarMensaje("Eliminado CatGasto " + id); mostrarTablaCatGasto(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "cliente":
                    Cliente c = clienteRep.getClienteById(id);
                    if (c != null) { clienteRep.delete(c); mostrarMensaje("Eliminado Cliente " + id); mostrarTablaCliente(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "especie":
                    Especie e = especieRep.getEspecieById(id);
                    if (e != null) { especieRep.delete(e); mostrarMensaje("Eliminado Especie " + id); mostrarTablaEspecie(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "estanque":
                    Estanque est = estanqueRep.getEstanqueById(id);
                    if (est != null) { estanqueRep.delete(est); mostrarMensaje("Eliminado Estanque " + id); mostrarTablaEstanque(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "gasto":
                    Gasto g = gastoRep.getGastoById(id);
                    if (g != null) { gastoRep.delete(g); mostrarMensaje("Eliminado Gasto " + id); mostrarTablaGasto(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "lote":
                    Lote l = loteRep.getLoteById(id);
                    if (l != null) { loteRep.delete(l); mostrarMensaje("Eliminado Lote " + id); mostrarTablaLote(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "proveedor":
                    Proveedor p = proveedorRep.getProveedorById(id);
                    if (p != null) { proveedorRep.delete(p); mostrarMensaje("Eliminado Proveedor " + id); mostrarTablaProveedor(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "regalimentacion":
                    RegAlimentacion ra = regAlimentacionRep.getAlimentacionById(id);
                    if (ra != null) { regAlimentacionRep.delete(ra); mostrarMensaje("Eliminado RegAlimentacion " + id); mostrarTablaRegAlimentacion(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "regreubicacion":
                    RegReubicacion rr = regReubicacionRep.getReubicacionById(id);
                    if (rr != null) { regReubicacionRep.delete(rr); mostrarMensaje("Eliminado RegReubicacion " + id); mostrarTablaRegReubicacion(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                case "venta":
                    Venta v = ventaRep.getVentaById(id);
                    if (v != null) { ventaRep.delete(v); mostrarMensaje("Eliminado Venta " + id); mostrarTablaVenta(); }
                    else mostrarMensaje("No se encontró ID");
                    break;
                default:
                    mostrarMensaje("Entidad '" + entidad + "' no válida");
            }
        });
    }

    private void mostrarMensaje(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private void mostrarDialogoSeleccionMostrar() {
        String[] entidades = {
                "Alimento", "CatGasto", "Cliente", "Especie", "Estanque",
                "Gasto", "Lote", "Proveedor", "RegAlimentacion", "RegReubicacion", "Venta"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una entidad para mostrar");
        builder.setItems(entidades, (dialog, which) -> {
            String seleccion = entidades[which];
            manejarSeleccionMostrar(seleccion);
        });
        builder.create().show();
    }

    private void manejarSeleccionMostrar(String seleccion) {
        switch (seleccion) {
            case "Alimento": mostrarTablaAlimento(); break;
            case "CatGasto": mostrarTablaCatGasto(); break;
            case "Cliente": mostrarTablaCliente(); break;
            case "Especie": mostrarTablaEspecie(); break;
            case "Estanque": mostrarTablaEstanque(); break;
            case "Gasto": mostrarTablaGasto(); break;
            case "Lote": mostrarTablaLote(); break;
            case "Proveedor": mostrarTablaProveedor(); break;
            case "RegAlimentacion": mostrarTablaRegAlimentacion(); break;
            case "RegReubicacion": mostrarTablaRegReubicacion(); break;
            case "Venta": mostrarTablaVenta(); break;
        }
    }

    private void mostrarDialogoSeleccionEntidad() {
        String[] entidades = {
                "Alimento", "CatGasto", "Cliente", "Especie", "Estanque",
                "Gasto", "Lote", "Proveedor", "RegAlimentacion", "RegReubicacion", "Venta"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una entidad para insertar");
        builder.setItems(entidades, (dialog, which) -> {
            String seleccion = entidades[which];
            Toast.makeText(this, "seleccionaste " + seleccion, Toast.LENGTH_SHORT).show();
            manejarSeleccionInsertar(seleccion);
        });
        builder.create().show();
    }

    private void manejarSeleccionInsertar(String seleccion) {
        switch (seleccion) {
            case "Alimento": dialogAlimento(); break;
            case "CatGasto": dialogCatGasto(); break;
            case "Cliente": dialogCliente(); break;
            case "Especie": dialogEspecie(); break;
            case "Estanque": dialogEstanque(); break;
            case "Gasto": dialogGasto(); break;
            case "Lote": dialogLote(); break;
            case "Proveedor": dialogProveedor(); break;
            case "RegAlimentacion": dialogRegAlimentacion(); break;
            case "RegReubicacion": dialogRegReubicacion(); break;
            case "Venta": dialogVenta(); break;
        }
    }

    private void dialogAlimento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Alimento");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIdProv = new EditText(this); etIdProv.setHint("ID Proveedor"); layout.addView(etIdProv);
        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etTipo = new EditText(this); etTipo.setHint("Tipo (double)"); layout.addView(etTipo);
        final EditText etUnidades = new EditText(this); etUnidades.setHint("Unidades"); layout.addView(etUnidades);
        final EditText etPeso = new EditText(this); etPeso.setHint("Peso"); layout.addView(etPeso);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Alimento a = new Alimento();
                a.setIdProveedor(Integer.parseInt(etIdProv.getText().toString()));
                a.setNombre(etNombre.getText().toString());
                a.setTipo(Double.parseDouble(etTipo.getText().toString()));
                a.setUnidades(Integer.parseInt(etUnidades.getText().toString()));
                a.setPeso(Integer.parseInt(etPeso.getText().toString()));
                executor.execute(() -> {
                    alimentoRep.insert(a);
                    mostrarTablaAlimento();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogCatGasto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar CatGasto");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etImagen = new EditText(this); etImagen.setHint("Imagen"); layout.addView(etImagen);
        final EditText etColor = new EditText(this); etColor.setHint("Color"); layout.addView(etColor);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            CatGasto cg = new CatGasto(etNombre.getText().toString(), etImagen.getText().toString(), etColor.getText().toString());
            executor.execute(() -> {
                catGastoRep.insert(cg);
                mostrarTablaCatGasto();
            });
        });
        builder.show();
    }

    private void dialogCliente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Cliente");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etDireccion = new EditText(this); etDireccion.setHint("Direccion"); layout.addView(etDireccion);
        final EditText etTelefono = new EditText(this); etTelefono.setHint("Telefono"); layout.addView(etTelefono);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            Cliente c = new Cliente(etNombre.getText().toString(), etDireccion.getText().toString(), etTelefono.getText().toString());
            executor.execute(() -> {
                clienteRep.insert(c);
                mostrarTablaCliente();
            });
        });
        builder.show();
    }

    private void dialogEspecie() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Especie");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etImagen = new EditText(this); etImagen.setHint("Imagen"); layout.addView(etImagen);
        final EditText etTm = new EditText(this); etTm.setHint("TM (meses)"); layout.addView(etTm);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Especie e = new Especie(etNombre.getText().toString(), etImagen.getText().toString(), Integer.parseInt(etTm.getText().toString()));
                executor.execute(() -> {
                    especieRep.insert(e);
                    mostrarTablaEspecie();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogEstanque() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Estanque");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etVolumen = new EditText(this); etVolumen.setHint("Volumen"); layout.addView(etVolumen);
        final EditText etImagen = new EditText(this); etImagen.setHint("Imagen"); layout.addView(etImagen);
        final EditText etEstado = new EditText(this); etEstado.setHint("Estado (true/false)"); layout.addView(etEstado);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Estanque e = new Estanque(etNombre.getText().toString(), Integer.parseInt(etVolumen.getText().toString()), etImagen.getText().toString(), Boolean.parseBoolean(etEstado.getText().toString()));
                executor.execute(() -> {
                    estanqueRep.insert(e);
                    mostrarTablaEstanque();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogGasto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Gasto");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIdCat = new EditText(this); etIdCat.setHint("ID CatGasto"); layout.addView(etIdCat);
        final EditText etMonto = new EditText(this); etMonto.setHint("Monto"); layout.addView(etMonto);
        final EditText etDesc = new EditText(this); etDesc.setHint("Descripcion"); layout.addView(etDesc);
        final EditText etFecha = new EditText(this); etFecha.setHint("Fecha (yyyy-mm-dd)"); layout.addView(etFecha);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Gasto g = new Gasto(Integer.parseInt(etIdCat.getText().toString()), Double.parseDouble(etMonto.getText().toString()), etDesc.getText().toString(), etFecha.getText().toString());
                executor.execute(() -> {
                    gastoRep.insert(g);
                    mostrarTablaGasto();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogLote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Lote");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etIdEspecie = new EditText(this); etIdEspecie.setHint("ID Especie"); layout.addView(etIdEspecie);
        final EditText etIdEstanque = new EditText(this); etIdEstanque.setHint("ID Estanque"); layout.addView(etIdEstanque);
        final EditText etIdProv = new EditText(this); etIdProv.setHint("ID Proveedor"); layout.addView(etIdProv);
        final EditText etCantIni = new EditText(this); etCantIni.setHint("Cant. Inicial"); layout.addView(etCantIni);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Lote l = new Lote(etNombre.getText().toString(), Integer.parseInt(etIdEspecie.getText().toString()), Integer.parseInt(etIdEstanque.getText().toString()), Integer.parseInt(etIdProv.getText().toString()), Integer.parseInt(etCantIni.getText().toString()), Integer.parseInt(etCantIni.getText().toString()));
                executor.execute(() -> {
                    loteRep.insert(l);
                    mostrarTablaLote();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogProveedor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Proveedor");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etNombre = new EditText(this); etNombre.setHint("Nombre"); layout.addView(etNombre);
        final EditText etTipo = new EditText(this); etTipo.setHint("Tipo"); layout.addView(etTipo);
        final EditText etDireccion = new EditText(this); etDireccion.setHint("Direccion"); layout.addView(etDireccion);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            Proveedor p = new Proveedor(etNombre.getText().toString(), etTipo.getText().toString(), etDireccion.getText().toString());
            executor.execute(() -> {
                proveedorRep.insert(p);
                mostrarTablaProveedor();
            });
        });
        builder.show();
    }

    private void dialogRegAlimentacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar RegAlimentacion");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIdEstanque = new EditText(this); etIdEstanque.setHint("ID Estanque"); layout.addView(etIdEstanque);
        final EditText etIdAlimento = new EditText(this); etIdAlimento.setHint("ID Alimento"); layout.addView(etIdAlimento);
        final EditText etKilos = new EditText(this); etKilos.setHint("Kilos"); layout.addView(etKilos);
        final EditText etFecha = new EditText(this); etFecha.setHint("Fecha"); layout.addView(etFecha);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                RegAlimentacion ra = new RegAlimentacion(Integer.parseInt(etIdEstanque.getText().toString()), Integer.parseInt(etIdAlimento.getText().toString()), Integer.parseInt(etKilos.getText().toString()), etFecha.getText().toString());
                executor.execute(() -> {
                    regAlimentacionRep.insert(ra);
                    mostrarTablaRegAlimentacion();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogRegReubicacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar RegReubicacion");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIdLote = new EditText(this); etIdLote.setHint("ID Lote"); layout.addView(etIdLote);
        final EditText etIdOrigen = new EditText(this); etIdOrigen.setHint("ID Estanque Origen"); layout.addView(etIdOrigen);
        final EditText etIdDestino = new EditText(this); etIdDestino.setHint("ID Estanque Destino"); layout.addView(etIdDestino);
        final EditText etUnidades = new EditText(this); etUnidades.setHint("Unidades"); layout.addView(etUnidades);
        final EditText etFecha = new EditText(this); etFecha.setHint("Fecha"); layout.addView(etFecha);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                RegReubicacion rr = new RegReubicacion(Integer.parseInt(etIdLote.getText().toString()), Integer.parseInt(etIdOrigen.getText().toString()), Integer.parseInt(etIdDestino.getText().toString()), Integer.parseInt(etUnidades.getText().toString()), etFecha.getText().toString());
                executor.execute(() -> {
                    regReubicacionRep.insert(rr);
                    mostrarTablaRegReubicacion();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void dialogVenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Venta");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIdLote = new EditText(this); etIdLote.setHint("ID Lote"); layout.addView(etIdLote);
        final EditText etIdCliente = new EditText(this); etIdCliente.setHint("ID Cliente"); layout.addView(etIdCliente);
        final EditText etUnidades = new EditText(this); etUnidades.setHint("Unidades"); layout.addView(etUnidades);
        final EditText etPeso = new EditText(this); etPeso.setHint("Peso"); layout.addView(etPeso);
        final EditText etMonto = new EditText(this); etMonto.setHint("Monto"); layout.addView(etMonto);
        final EditText etFecha = new EditText(this); etFecha.setHint("Fecha"); layout.addView(etFecha);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                Venta v = new Venta(Integer.parseInt(etIdLote.getText().toString()), Integer.parseInt(etIdCliente.getText().toString()), Integer.parseInt(etUnidades.getText().toString()), Integer.parseInt(etPeso.getText().toString()), Double.parseDouble(etMonto.getText().toString()), etFecha.getText().toString());
                executor.execute(() -> {
                    ventaRep.insert(v);
                    mostrarTablaVenta();
                });
            } catch (Exception ex) {
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void mostrarTablaAlimento() {
        executor.execute(() -> {
            List<Alimento> lista = alimentoRep.getAllAlimentos();
            StringBuilder sb = new StringBuilder("TABLA ALIMENTO:\n");
            for (Alimento a : lista) {
                sb.append("ID: ").append(a.getId()).append(" | Nombre: ").append(a.getNombre()).append(" | Peso: ").append(a.getPeso()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaCatGasto() {
        executor.execute(() -> {
            List<CatGasto> lista = catGastoRep.getAllCategorias();
            StringBuilder sb = new StringBuilder("TABLA CATGASTO:\n");
            for (CatGasto cg : lista) {
                sb.append("ID: ").append(cg.getId()).append(" | Nombre: ").append(cg.getNombre()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaCliente() {
        executor.execute(() -> {
            List<Cliente> lista = clienteRep.getAllClientes();
            StringBuilder sb = new StringBuilder("TABLA CLIENTE:\n");
            for (Cliente c : lista) {
                sb.append("ID: ").append(c.getId()).append(" | Nombre: ").append(c.getNombre()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaEspecie() {
        executor.execute(() -> {
            List<Especie> lista = especieRep.getAllEspecies();
            StringBuilder sb = new StringBuilder("TABLA ESPECIE:\n");
            for (Especie e : lista) {
                sb.append("ID: ").append(e.getId()).append(" | Nombre: ").append(e.getNombre()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaEstanque() {
        executor.execute(() -> {
            List<Estanque> lista = estanqueRep.getAllEstanques();
            StringBuilder sb = new StringBuilder("TABLA ESTANQUE:\n");
            for (Estanque e : lista) {
                sb.append("ID: ").append(e.getId()).append(" | Nombre: ").append(e.getNombre()).append(" | Vol: ").append(e.getVolumen()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaGasto() {
        executor.execute(() -> {
            List<Gasto> lista = gastoRep.getAllGastos();
            StringBuilder sb = new StringBuilder("TABLA GASTO:\n");
            for (Gasto g : lista) {
                sb.append("ID: ").append(g.getId()).append(" | Monto: ").append(g.getMonto()).append(" | Fecha: ").append(g.getFecha()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaLote() {
        executor.execute(() -> {
            List<Lote> lista = loteRep.getAllLotes();
            StringBuilder sb = new StringBuilder("TABLA LOTE:\n");
            for (Lote l : lista) {
                sb.append("ID: ").append(l.getId()).append(" | Nombre: ").append(l.getNombre()).append(" | Cant: ").append(l.getCant_act()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaProveedor() {
        executor.execute(() -> {
            List<Proveedor> lista = proveedorRep.getAllProveedores();
            StringBuilder sb = new StringBuilder("TABLA PROVEEDOR:\n");
            for (Proveedor p : lista) {
                sb.append("ID: ").append(p.getId()).append(" | Nombre: ").append(p.getNombre()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaRegAlimentacion() {
        executor.execute(() -> {
            List<RegAlimentacion> lista = regAlimentacionRep.getAllAlimentaciones();
            StringBuilder sb = new StringBuilder("TABLA REG_ALIMENTACION:\n");
            for (RegAlimentacion ra : lista) {
                sb.append("ID: ").append(ra.getId()).append(" | Kilos: ").append(ra.getKilos()).append(" | Fecha: ").append(ra.getFecha()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaRegReubicacion() {
        executor.execute(() -> {
            List<RegReubicacion> lista = regReubicacionRep.getAllReubicaciones();
            StringBuilder sb = new StringBuilder("TABLA REG_REUBICACION:\n");
            for (RegReubicacion rr : lista) {
                sb.append("ID: ").append(rr.getIdRegReubicacion()).append(" | Unidades: ").append(rr.getUnidades()).append(" | Fecha: ").append(rr.getFecha()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }

    private void mostrarTablaVenta() {
        executor.execute(() -> {
            List<Venta> lista = ventaRep.getAllVentas();
            StringBuilder sb = new StringBuilder("TABLA VENTA:\n");
            for (Venta v : lista) {
                sb.append("ID: ").append(v.getId()).append(" | Monto: ").append(v.getMonto()).append(" | Fecha: ").append(v.getFecha()).append("\n");
            }
            runOnUiThread(() -> tvInfo.setText(sb.toString()));
        });
    }
}