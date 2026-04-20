package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.ProveedorDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Proveedor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProveedorRep {
    private ProveedorDao proveedorDao;
    private ExecutorService executorService;

    public ProveedorRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        proveedorDao = db.proveedorDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Proveedor proveedor) {
        executorService.execute(() -> proveedorDao.insert(proveedor));
    }

    public void update(Proveedor proveedor) {
        executorService.execute(() -> proveedorDao.update(proveedor));
    }

    public void delete(Proveedor proveedor) {
        executorService.execute(() -> proveedorDao.delete(proveedor));
    }

    public List<Proveedor> getAllProveedores() {
        return proveedorDao.getAllProveedores();
    }

    public Proveedor getProveedorById(int id) {
        return proveedorDao.getProveedorById(id);
    }
}
