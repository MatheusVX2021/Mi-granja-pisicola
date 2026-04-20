package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Estanque;

import java.util.List;

@Dao
public interface EstanqueDao {
    @Insert
    void insert(Estanque estanque);

    @Update
    void update(Estanque estanque);

    @Delete
    void delete(Estanque estanque);

    @Query("SELECT * FROM estanque ORDER BY nombre ASC")
    List<Estanque> getAllEstanques();

    @Query("SELECT * FROM estanque WHERE idEstanque = :id")
    Estanque getEstanqueById(int id);
}
