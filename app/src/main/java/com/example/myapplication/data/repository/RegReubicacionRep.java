package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.RegReubicacionDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.RegReubicacion;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegReubicacionRep {
    private RegReubicacionDao regReubicacionDao;
    private ExecutorService executorService;

    public RegReubicacionRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        regReubicacionDao = db.regReubicacionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(RegReubicacion regReubicacion) {
        executorService.execute(() -> regReubicacionDao.insert(regReubicacion));
    }

    public void update(RegReubicacion regReubicacion) {
        executorService.execute(() -> regReubicacionDao.update(regReubicacion));
    }

    public void delete(RegReubicacion regReubicacion) {
        executorService.execute(() -> regReubicacionDao.delete(regReubicacion));
    }

    public List<RegReubicacion> getAllReubicaciones() {
        return regReubicacionDao.getAllReubicaciones();
    }

    public RegReubicacion getReubicacionById(int id) {
        return regReubicacionDao.getReubicacionById(id);
    }
}
