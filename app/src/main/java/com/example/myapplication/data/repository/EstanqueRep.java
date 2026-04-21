package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.EstanqueDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.model.EstanqueUI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstanqueRep {
    private EstanqueDao estanqueDao;
    private ExecutorService executorService;

    public EstanqueRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        estanqueDao = db.estanqueDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Estanque estanque) {
        insert(estanque, null);
    }

    public void insert(Estanque estanque, Runnable onComplete) {
        executorService.execute(() -> {
            estanqueDao.insert(estanque);
            if (onComplete != null) onComplete.run();
        });
    }

    public void update(Estanque estanque) {
        update(estanque, null);
    }

    public void update(Estanque estanque, Runnable onComplete) {
        executorService.execute(() -> {
            estanqueDao.update(estanque);
            if (onComplete != null) onComplete.run();
        });
    }

    public void delete(Estanque estanque) {
        delete(estanque, null);
    }

    public void delete(Estanque estanque, Runnable onComplete) {
        executorService.execute(() -> {
            estanqueDao.delete(estanque);
            if (onComplete != null) onComplete.run();
        });
    }

    public List<EstanqueUI> getEstanquesUI() {
        return estanqueDao.getEstanquesUI();
    }

    public List<Estanque> getAllEstanques() {
        return estanqueDao.getAllEstanques();
    }

    public Estanque getEstanqueById(int id) {
        return estanqueDao.getEstanqueById(id);
    }
}
