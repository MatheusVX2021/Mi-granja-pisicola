package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "venta",
        foreignKeys = {
                @ForeignKey(entity = Lote.class,
                        parentColumns = "id",
                        childColumns = "idLote",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Cliente.class,
                        parentColumns = "id",
                        childColumns = "idCliente",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("idLote"),
                @Index("idCliente")
        })
public class Venta {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idLote;
    private int idCliente;
    private int unidades; // cantidad de peces vendidos
    private int peso;
    private double monto;
    @NonNull
    private String fecha;

    public Venta() {}

    public Venta(int idLote, int idCliente, int unidades, int peso, double monto, @NonNull String fecha) {
        this.idLote = idLote;
        this.idCliente = idCliente;
        this.unidades = unidades;
        this.peso = peso;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }
}
