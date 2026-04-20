package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Proveedor;

import java.util.List;

@Dao
public interface ProveedorDao {
    @Insert
    void insert(Proveedor proveedor);

    @Update
    void update(Proveedor proveedor);

    @Delete
    void delete(Proveedor proveedor);

    @Query("SELECT * FROM proveedor ORDER BY nombre ASC")
    List<Proveedor> getAllProveedores();

    @Query("SELECT * FROM proveedor WHERE id = :id")
    Proveedor getProveedorById(int id);
}
