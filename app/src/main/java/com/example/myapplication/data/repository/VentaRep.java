package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.VentaDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Venta;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VentaRep {
    private VentaDao ventaDao;
    private ExecutorService executorService;

    public VentaRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        ventaDao = db.ventaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Venta venta) {
        executorService.execute(() -> ventaDao.insert(venta));
    }

    public void update(Venta venta) {
        executorService.execute(() -> ventaDao.update(venta));
    }

    public void delete(Venta venta) {
        executorService.execute(() -> ventaDao.delete(venta));
    }

    public List<Venta> getAllVentas() {
        return ventaDao.getAllVentas();
    }

    public Venta getVentaById(int id) {
        return ventaDao.getVentaById(id);
    }
}
