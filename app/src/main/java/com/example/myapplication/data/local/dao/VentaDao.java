package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Venta;

import java.util.List;

@Dao
public interface VentaDao {
    @Insert
    void insert(Venta venta);

    @Update
    void update(Venta venta);

    @Delete
    void delete(Venta venta);

    @Query("SELECT * FROM venta ORDER BY fecha DESC")
    List<Venta> getAllVentas();

    @Query("SELECT * FROM venta WHERE id = :id")
    Venta getVentaById(int id);
}
