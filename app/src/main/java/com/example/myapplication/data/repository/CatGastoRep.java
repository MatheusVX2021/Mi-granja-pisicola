package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.CatGastoDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.CatGasto;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CatGastoRep {
    private CatGastoDao catGastoDao;
    private ExecutorService executorService;

    public CatGastoRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        catGastoDao = db.catGastoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(CatGasto catGasto) {
        executorService.execute(() -> catGastoDao.insert(catGasto));
    }

    public void update(CatGasto catGasto) {
        executorService.execute(() -> catGastoDao.update(catGasto));
    }

    public void delete(CatGasto catGasto) {
        executorService.execute(() -> catGastoDao.delete(catGasto));
    }

    public List<CatGasto> getAllCategorias() {
        return catGastoDao.getAllCategorias();
    }

    public CatGasto getCategoriaById(int id) {
        return catGastoDao.getCategoriaById(id);
    }
}
