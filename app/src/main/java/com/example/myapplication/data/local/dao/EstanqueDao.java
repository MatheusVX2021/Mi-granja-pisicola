package com.example.myapplication.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.model.EstanqueUI;

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

    @Query("SELECT * FROM estanque WHERE id = :id")
    Estanque getEstanqueById(int id);

    @Query("SELECT e.id, e.imagen, e.nombre, e.area, " +
            "IFNULL(SUM(l.cant_act), 0) as cantidadPeces, " +
            "IFNULL(AVG(l.peso_promedio), 0.0) as pesoPromedio, " +
            "IFNULL(AVG(CAST((julianday('now') - julianday(l.fecha_entrada)) / 30.44 AS INTEGER)), 0) as edadPromedio, " +
            "(SELECT MAX(fecha) FROM reg_alimentacion WHERE idEstanque = e.id) as ultimaAlimentacion " +
            "FROM estanque e " +
            "LEFT JOIN lote l ON e.id = l.idEstanque " +
            "GROUP BY e.id ORDER BY e.nombre ASC")
    List<EstanqueUI> getEstanquesUI();
}
