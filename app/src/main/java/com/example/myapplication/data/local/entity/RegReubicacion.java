package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reg_reubicacion",
        foreignKeys = {
                @ForeignKey(entity = Estanque.class,
                        parentColumns = "idEstanque",
                        childColumns = "idEstanqueOrigen",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Estanque.class,
                        parentColumns = "idEstanque",
                        childColumns = "idEstanqueDestino",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Lote.class,
                        parentColumns = "idLote",
                        childColumns = "idLote",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("idEstanqueOrigen"),
                @Index("idEstanqueDestino"),
                @Index("idLote")
        })
public class RegReubicacion {
    @PrimaryKey(autoGenerate = true)
    private int idRegReubicacion;
    private int idLote;
    private int idEstanqueOrigen;
    private int idEstanqueDestino;
    private int unidades;
    @NonNull
    private String fecha;

    public RegReubicacion() {}

    public RegReubicacion(int idLote, int idEstanqueOrigen, int idEstanqueDestino, int unidades, @NonNull String fecha) {
        this.idLote = idLote;
        this.idEstanqueOrigen = idEstanqueOrigen;
        this.idEstanqueDestino = idEstanqueDestino;
        this.unidades = unidades;
        this.fecha = fecha;
    }

    public int getIdRegReubicacion() {
        return idRegReubicacion;
    }

    public void setIdRegReubicacion(int idRegReubicacion) {
        this.idRegReubicacion = idRegReubicacion;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getIdEstanqueOrigen() {
        return idEstanqueOrigen;
    }

    public void setIdEstanqueOrigen(int idEstanqueOrigen) {
        this.idEstanqueOrigen = idEstanqueOrigen;
    }

    public int getIdEstanqueDestino() {
        return idEstanqueDestino;
    }

    public void setIdEstanqueDestino(int idEstanqueDestino) {
        this.idEstanqueDestino = idEstanqueDestino;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }
}
