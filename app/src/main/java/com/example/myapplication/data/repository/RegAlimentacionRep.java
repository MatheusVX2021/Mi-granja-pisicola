package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.RegAlimentacionDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.RegAlimentacion;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegAlimentacionRep {
    private RegAlimentacionDao regAlimentacionDao;
    private ExecutorService executorService;

    public RegAlimentacionRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        regAlimentacionDao = db.regAlimentacionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(RegAlimentacion regAlimentacion) {
        executorService.execute(() -> regAlimentacionDao.insert(regAlimentacion));
    }

    public void update(RegAlimentacion regAlimentacion) {
        executorService.execute(() -> regAlimentacionDao.update(regAlimentacion));
    }

    public void delete(RegAlimentacion regAlimentacion) {
        executorService.execute(() -> regAlimentacionDao.delete(regAlimentacion));
    }

    public List<RegAlimentacion> getAllAlimentaciones() {
        return regAlimentacionDao.getAllAlimentaciones();
    }

    public List<com.example.myapplication.data.model.RegAlimentacionUI> getAllAlimentacionesUI() {
        return regAlimentacionDao.getAllAlimentacionesUI();
    }

    public RegAlimentacion getAlimentacionById(int id) {
        return regAlimentacionDao.getAlimentacionById(id);
    }
}
