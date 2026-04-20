package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.CatGasto;

import java.util.List;

@Dao
public interface CatGastoDao {
    @Insert
    void insert(CatGasto catGasto);

    @Update
    void update(CatGasto catGasto);

    @Delete
    void delete(CatGasto catGasto);

    @Query("SELECT * FROM cat_gasto ORDER BY nombre ASC")
    List<CatGasto> getAllCategorias();

    @Query("SELECT * FROM cat_gasto WHERE id = :id")
    CatGasto getCategoriaById(int id);
}
