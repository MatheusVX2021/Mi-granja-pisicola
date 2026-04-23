package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Venta;
import com.example.myapplication.data.model.VentaUI;

import java.util.List;

@Dao
public interface VentaDao {
    @Insert
    void insert(Venta venta);

    @Update
    void update(Venta venta);

    @Delete
    void delete(Venta venta);

    @Query("SELECT v.*, l.nombre as nombreLote, c.nombre as nombreCliente " +
            "FROM venta v " +
            "JOIN lote l ON v.idLote = l.id " +
            "JOIN cliente c ON v.idCliente = c.id " +
            "ORDER BY v.fecha DESC")
    List<VentaUI> getAllVentasUI();

    @Query("SELECT * FROM venta WHERE id = :id")
    Venta getVentaById(int id);
}
