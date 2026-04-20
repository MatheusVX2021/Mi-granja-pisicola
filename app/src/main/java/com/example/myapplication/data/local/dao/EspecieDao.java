package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Especie;

import java.util.List;

@Dao
public interface EspecieDao {
    @Insert
    void insert(Especie especie);

    @Update
    void update(Especie especie);

    @Delete
    void delete(Especie especie);

    @Query("SELECT * FROM especie ORDER BY nombre ASC")
    List<Especie> getAllEspecies();

    @Query("SELECT * FROM especie WHERE id = :id")
    Especie getEspecieById(int id);
}
