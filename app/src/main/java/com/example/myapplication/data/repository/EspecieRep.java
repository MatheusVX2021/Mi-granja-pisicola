package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.EspecieDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Especie;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EspecieRep {
    private EspecieDao especieDao;
    private ExecutorService executorService;

    public EspecieRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        especieDao = db.especieDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Especie especie) {
        executorService.execute(() -> especieDao.insert(especie));
    }

    public void update(Especie especie) {
        executorService.execute(() -> especieDao.update(especie));
    }

    public void delete(Especie especie) {
        executorService.execute(() -> especieDao.delete(especie));
    }

    public List<Especie> getAllEspecies() {
        return especieDao.getAllEspecies();
    }

    public Especie getEspecieById(int id) {
        return especieDao.getEspecieById(id);
    }
}
