package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.EstanqueDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Estanque;
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
        executorService.execute(() -> estanqueDao.insert(estanque));
    }

    public void update(Estanque estanque) {
        executorService.execute(() -> estanqueDao.update(estanque));
    }

    public void delete(Estanque estanque) {
        executorService.execute(() -> estanqueDao.delete(estanque));
    }

    public List<Estanque> getAllEstanques() {
        return estanqueDao.getAllEstanques();
    }

    public Estanque getEstanqueById(int id) {
        return estanqueDao.getEstanqueById(id);
    }
}
