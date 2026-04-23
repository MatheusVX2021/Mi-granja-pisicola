package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Alimento;
import com.example.myapplication.data.local.entity.AlimentoWithProveedor;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

@Dao
public interface AlimentoDao {
    @Insert
    void insert(Alimento alimento);

    @Update
    void update(Alimento alimento);

    @Delete
    void delete(Alimento alimento);

    @Transaction
    @Query("SELECT * FROM alimento ORDER BY nombre ASC")
    LiveData<List<AlimentoWithProveedor>> getAllAlimentosWithProveedor();

    @Query("SELECT * FROM alimento WHERE id = :id")
    Alimento getAlimentoById(int id);

    @Transaction
    @Query("SELECT * FROM alimento ORDER BY nombre ASC")
    List<AlimentoWithProveedor> getAllAlimentosStatic();

    @Query("SELECT * FROM alimento WHERE nombre = :nombre LIMIT 1")
    Alimento getAlimentoByNombre(String nombre);
}
