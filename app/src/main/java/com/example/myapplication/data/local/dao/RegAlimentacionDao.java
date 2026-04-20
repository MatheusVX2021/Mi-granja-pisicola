package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
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
}
