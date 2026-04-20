package com.example.myapplication.data.repository;

import android.app.Application;
import com.example.myapplication.data.local.dao.ClienteDao;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Cliente;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClienteRep {
    private ClienteDao clienteDao;
    private ExecutorService executorService;

    public ClienteRep(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        clienteDao = db.clienteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Cliente cliente) {
        executorService.execute(() -> clienteDao.insert(cliente));
    }

    public void update(Cliente cliente) {
        executorService.execute(() -> clienteDao.update(cliente));
    }

    public void delete(Cliente cliente) {
        executorService.execute(() -> clienteDao.delete(cliente));
    }

    public List<Cliente> getAllClientes() {
        return clienteDao.getAllClientes();
    }

    public Cliente getClienteById(int id) {
        return clienteDao.getClienteById(id);
    }
}
