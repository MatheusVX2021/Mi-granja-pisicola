package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Lote;

import java.util.List;

@Dao
public interface LoteDao {
    @Insert
    void insert(Lote lote);

    @Update
    void update(Lote lote);

    @Delete
    void delete(Lote lote);

    @Query("SELECT * FROM lote ORDER BY nombre ASC")
    List<Lote> getAllLotes();

    @Query("SELECT l.*, e.nombre as nombreEspecie, e.imagen as imagenEspecie, est.nombre as nombreEstanque, p.nombre as nombreProveedor " +
           "FROM lote l " +
           "INNER JOIN especie e ON l.idEspecie = e.id " +
           "INNER JOIN estanque est ON l.idEstanque = est.id " +
           "INNER JOIN proveedor p ON l.idProveedor = p.id " +
           "ORDER BY l.nombre ASC")
    List<com.example.myapplication.data.model.LoteUI> getAllLotesUI();

    @Query("SELECT * FROM lote WHERE id = :id")
    Lote getLoteById(int id);

    @Query("SELECT * FROM lote WHERE idEstanque = :idEstanque")
    List<Lote> getLotesByEstanque(int idEstanque);
}
