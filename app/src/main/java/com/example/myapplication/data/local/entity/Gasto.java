package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gasto")
public class Gasto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idCatGasto;
    @NonNull
    private double monto;
    private String descripcion;
    @NonNull
    private String fecha;

    public Gasto(){}

    public Gasto(int idCatGasto, @NonNull double monto, String descripcion, @NonNull String fecha){
        this.idCatGasto = idCatGasto;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getIdCatGasto() {return idCatGasto;}
    public void setIdCatGasto(int idCatGasto) {this.idCatGasto = idCatGasto;}
    public double getMonto() {return monto;}
    public void setMonto(double monto) {this.monto = monto;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public String getFecha() {return fecha;}
    public void setFecha(@NonNull String fecha) {this.fecha = fecha;}
}
