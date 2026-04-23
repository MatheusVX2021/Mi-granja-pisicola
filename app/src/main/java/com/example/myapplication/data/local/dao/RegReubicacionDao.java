package com.example.myapplication.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.RegReubicacion;

import java.util.List;

@Dao
public interface RegReubicacionDao {
    @Insert
    void insert(RegReubicacion regReubicacion);

    @Update
    void update(RegReubicacion regReubicacion);

    @Delete
    void delete(RegReubicacion regReubicacion);

    @Query("SELECT * FROM reg_reubicacion ORDER BY fecha DESC")
    List<RegReubicacion> getAllReubicaciones();

    @Query("SELECT * FROM reg_reubicacion WHERE idRegReubicacion = :id")
    RegReubicacion getReubicacionById(int id);

    @Transaction
    @Query("SELECT r.*, l.nombre as nombreLote, e1.nombre as nombreEstanqueOrigen, e2.nombre as nombreEstanqueDestino " +
           "FROM reg_reubicacion r " +
           "INNER JOIN lote l ON r.idLote = l.id " +
           "INNER JOIN estanque e1 ON r.idEstanqueOrigen = e1.id " +
           "INNER JOIN estanque e2 ON r.idEstanqueDestino = e2.id " +
           "ORDER BY r.fecha DESC")
    LiveData<List<com.example.myapplication.data.model.RegReubicacionUI>> getAllRegistrosUI();
}
