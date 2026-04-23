package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.RegAlimentacion;

import java.util.List;

@Dao
public interface RegAlimentacionDao {
    @Insert
    void insert(RegAlimentacion regAlimentacion);

    @Update
    void update(RegAlimentacion regAlimentacion);

    @Delete
    void delete(RegAlimentacion regAlimentacion);

    @Query("SELECT * FROM reg_alimentacion ORDER BY fecha DESC")
    List<RegAlimentacion> getAllAlimentaciones();

    @Query("SELECT * FROM reg_alimentacion WHERE id = :id")
    RegAlimentacion getAlimentacionById(int id);

    @Transaction
    @Query("SELECT r.id, e.nombre as nombreEstanque, a.nombre as nombreAlimento, r.kilos, r.fecha " +
           "FROM reg_alimentacion r " +
           "INNER JOIN estanque e ON r.idEstanque = e.id " +
           "INNER JOIN alimento a ON r.idAlimento = a.id " +
           "ORDER BY r.fecha DESC")
    List<com.example.myapplication.data.model.RegAlimentacionUI> getAllAlimentacionesUI();
}
