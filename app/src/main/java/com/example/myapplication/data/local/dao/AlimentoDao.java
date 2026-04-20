package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Alimento;

import java.util.List;

@Dao
public interface AlimentoDao {
    @Insert
    void insert(Alimento alimento);

    @Update
    void update(Alimento alimento);

    @Delete
    void delete(Alimento alimento);

    @Query("SELECT * FROM alimento ORDER BY nombre ASC")
    List<Alimento> getAllAlimentos();

    @Query("SELECT * FROM alimento WHERE id = :id")
    Alimento getAlimentoById(int id);
}
