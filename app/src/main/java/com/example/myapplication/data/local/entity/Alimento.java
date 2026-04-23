package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "alimento", indices = {@Index(value = {"nombre"}, unique = true)})
public class Alimento {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idProveedor;
    @NonNull
    private String nombre;
    private String tipo;
    private int unidades;
    private int peso;

    public Alimento(){}

    public Alimento(int idProveedor, @NonNull String nombre, String tipo, int unidades, int peso){
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
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public int getUnidades() {return unidades;}
    public void setUnidades(int unidades) {this.unidades = unidades;}
    public int getPeso() {return peso;}
    public void setPeso(int peso) {this.peso = peso;}
}
