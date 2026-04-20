package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.AlimentoDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Alimento;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlimentoRep {
    private AlimentoDao alimentoDao;
    private ExecutorService executorService;

    public AlimentoRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        alimentoDao = db.alimentoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Alimento alimento) {
        executorService.execute(() -> alimentoDao.insert(alimento));
    }

    public void update(Alimento alimento) {
        executorService.execute(() -> alimentoDao.update(alimento));
    }

    public void delete(Alimento alimento) {
        executorService.execute(() -> alimentoDao.delete(alimento));
    }

    public List<Alimento> getAllAlimentos() {
        return alimentoDao.getAllAlimentos();
    }

    public Alimento getAlimentoById(int id) {
        return alimentoDao.getAlimentoById(id);
    }
}
