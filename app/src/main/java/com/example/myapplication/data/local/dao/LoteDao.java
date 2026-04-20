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

    @Query("SELECT * FROM lote WHERE id = :id")
    Lote getLoteById(int id);

    @Query("SELECT * FROM lote WHERE idEstanque = :idEstanque")
    List<Lote> getLotesByEstanque(int idEstanque);
}
