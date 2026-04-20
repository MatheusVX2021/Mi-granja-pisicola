package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alimento")
public class Alimento {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idProveedor;
    @NonNull
    private String nombre;
    private double tipo;
    private int unidades;
    private int peso;

    public Alimento(){}

    public Alimento(int idProveedor, @NonNull String nombre, double tipo, int unidades, int peso){
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.tipo = tipo;
        this.unidades = unidades;
        this.peso = peso;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getIdProveedor() {return idProveedor;}
    public void setIdProveedor(int idProveedor) {this.idProveedor = idProveedor;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public double getTipo() {return tipo;}
    public void setTipo(double tipo) {this.tipo = tipo;}
    public int getUnidades() {return unidades;}
    public void setUnidades(int unidades) {this.unidades = unidades;}
    public int getPeso() {return peso;}
    public void setPeso(int peso) {this.peso = peso;}
}
