package com.example.myapplication.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.local.dao.AlimentoDao;
import com.example.myapplication.data.local.dao.CatGastoDao;
import com.example.myapplication.data.local.dao.ClienteDao;
import com.example.myapplication.data.local.dao.EspecieDao;
import com.example.myapplication.data.local.dao.EstanqueDao;
import com.example.myapplication.data.local.dao.GastoDao;
import com.example.myapplication.data.local.dao.LoteDao;
import com.example.myapplication.data.local.dao.ProveedorDao;
import com.example.myapplication.data.local.dao.RegAlimentacionDao;
import com.example.myapplication.data.local.dao.RegReubicacionDao;
import com.example.myapplication.data.local.dao.VentaDao;
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

@Database(entities = {
        Alimento.class,
        CatGasto.class,
        Cliente.class,
        Especie.class,
        Estanque.class,
        Gasto.class,
        Lote.class,
        Proveedor.class,
        RegAlimentacion.class,
        RegReubicacion.class,
        Venta.class
}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract AlimentoDao alimentoDao();
    public abstract CatGastoDao catGastoDao();
    public abstract ClienteDao clienteDao();
    public abstract EspecieDao especieDao();
    public abstract EstanqueDao estanqueDao();
    public abstract GastoDao gastoDao();
    public abstract LoteDao loteDao();
    public abstract ProveedorDao proveedorDao();
    public abstract RegAlimentacionDao regAlimentacionDao();
    public abstract RegReubicacionDao regReubicacionDao();
    public abstract VentaDao ventaDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "peces_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@androidx.annotation.NonNull androidx.sqlite.db.SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    insertarDatosPorDefecto(context);
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void insertarDatosPorDefecto(Context context) {
        java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = getDatabase(context);
            
            // 1. Insertar Especie por defecto
            if (db.especieDao().getAllEspeciesSync().isEmpty()) {
                db.especieDao().insertSync(new com.example.myapplication.data.local.entity.Especie("Tilapia Roja", null, 6));
            }

            // 2. Insertar Categoría de Gasto por defecto
            if (db.catGastoDao().getAllCategorias().isEmpty()) {
                db.catGastoDao().insert(new com.example.myapplication.data.local.entity.CatGasto("Alimentación", "", ""));
            }

            // 3. Insertar Estanque por defecto
            if (db.estanqueDao().getAllEstanques().isEmpty()) {
                db.estanqueDao().insert(new com.example.myapplication.data.local.entity.Estanque("Estanque Principal", 100.0, null));
            }

            // Puedes añadir más datos por defecto aquí para otras tablas
        });
    }
}
