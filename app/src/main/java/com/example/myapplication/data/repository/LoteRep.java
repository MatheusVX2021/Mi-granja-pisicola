package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.LoteDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Lote;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoteRep {
    private LoteDao loteDao;
    private ExecutorService executorService;

    public LoteRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        loteDao = db.loteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Lote lote) {
        executorService.execute(() -> loteDao.insert(lote));
    }

    public void update(Lote lote) {
        executorService.execute(() -> loteDao.update(lote));
    }

    public void delete(Lote lote) {
        executorService.execute(() -> loteDao.delete(lote));
    }

    public List<Lote> getAllLotes() {
        return loteDao.getAllLotes();
    }

    public Lote getLoteById(int id) {
        return loteDao.getLoteById(id);
    }

    public List<Lote> getLotesByEstanque(int idEstanque) {
        return loteDao.getLotesByEstanque(idEstanque);
    }
}
