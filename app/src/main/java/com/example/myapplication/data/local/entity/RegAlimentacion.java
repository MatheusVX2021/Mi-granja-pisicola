package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reg_alimentacion",
        foreignKeys = {
                @ForeignKey(entity = Estanque.class,
                        parentColumns = "id",
                        childColumns = "idEstanque",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Alimento.class,
                        parentColumns = "id",
                        childColumns = "idAlimento",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("idEstanque"),
                @Index("idAlimento")
        })
public class RegAlimentacion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idEstanque;
    private int idAlimento;
    private int kilos;
    @NonNull
    private String fecha;

    public RegAlimentacion() {}

    public RegAlimentacion(int idEstanque, int idAlimento, int kilos, @NonNull String fecha) {
        this.idEstanque = idEstanque;
        this.idAlimento = idAlimento;
        this.kilos = kilos;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstanque() {
        return idEstanque;
    }

    public void setIdEstanque(int idEstanque) {
        this.idEstanque = idEstanque;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public int getKilos() {
        return kilos;
    }

    public void setKilos(int kilos) {
        this.kilos = kilos;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }
}
