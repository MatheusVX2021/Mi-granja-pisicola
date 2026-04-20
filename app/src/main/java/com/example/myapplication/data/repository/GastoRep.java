package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.GastoDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Gasto;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GastoRep {
    private GastoDao gastoDao;
    private ExecutorService executorService;

    public GastoRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gastoDao = db.gastoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Gasto gasto) {
        executorService.execute(() -> gastoDao.insert(gasto));
    }

    public void update(Gasto gasto) {
        executorService.execute(() -> gastoDao.update(gasto));
    }

    public void delete(Gasto gasto) {
        executorService.execute(() -> gastoDao.delete(gasto));
    }

    public List<Gasto> getAllGastos() {
        return gastoDao.getAllGastos();
    }

    public Gasto getGastoById(int id) {
        return gastoDao.getGastoById(id);
    }
}
