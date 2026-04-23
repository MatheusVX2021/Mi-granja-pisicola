package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Gasto;
import com.example.myapplication.data.local.entity.GastoWithCategory;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

@Dao
public interface GastoDao {
    @Insert
    void insert(Gasto gasto);

    @Update
    void update(Gasto gasto);

    @Delete
    void delete(Gasto gasto);

    @Transaction
    @Query("SELECT * FROM gasto ORDER BY fecha DESC")
    LiveData<List<GastoWithCategory>> getAllGastosWithCategory();

    @Query("SELECT * FROM gasto ORDER BY fecha DESC")
    List<Gasto> getAllGastos();

    @Query("SELECT * FROM gasto WHERE id = :id")
    Gasto getGastoById(int id);
}
