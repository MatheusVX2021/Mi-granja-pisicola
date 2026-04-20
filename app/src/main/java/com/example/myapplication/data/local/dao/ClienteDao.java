package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Cliente;

import java.util.List;

@Dao
public interface ClienteDao {
    @Insert
    void insert(Cliente cliente);

    @Update
    void update(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Query("SELECT * FROM cliente ORDER BY nombre ASC")
    List<Cliente> getAllClientes();

    @Query("SELECT * FROM cliente WHERE id = :id")
    Cliente getClienteById(int id);
}
