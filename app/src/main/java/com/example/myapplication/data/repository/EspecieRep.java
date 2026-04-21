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
        insert(especie, null);
    }

    public void insert(Especie especie, Runnable onComplete) {
        executorService.execute(() -> {
            especieDao.insert(especie);
            if (onComplete != null) onComplete.run();
        });
    }

    public void update(Especie especie) {
        update(especie, null);
    }

    public void update(Especie especie, Runnable onComplete) {
        executorService.execute(() -> {
            especieDao.update(especie);
            if (onComplete != null) onComplete.run();
        });
    }

    public void delete(Especie especie) {
        delete(especie, null);
    }

    public void delete(Especie especie, Runnable onComplete) {
        executorService.execute(() -> {
            especieDao.delete(especie);
            if (onComplete != null) onComplete.run();
        });
    }

    public List<Especie> getAllEspecies() {
        return especieDao.getAllEspecies();
    }

    public Especie getEspecieById(int id) {
        return especieDao.getEspecieById(id);
    }
}
