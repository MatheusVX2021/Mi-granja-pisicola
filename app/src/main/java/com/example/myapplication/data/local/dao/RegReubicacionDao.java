package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
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
}
