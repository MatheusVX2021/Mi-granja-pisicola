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
}, version = 4, exportSchema = false)
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
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
