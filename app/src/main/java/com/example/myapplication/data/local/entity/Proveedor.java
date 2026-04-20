package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "proveedor")
public class Proveedor {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nombre;
    private String tipo;
    private String direccion;

    public Proveedor(){}

    public Proveedor(String nombre, String tipo, String direccion){
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
    }

    public int getId() {return id;}
    public void setId(int idProveedor) {this.id = idProveedor;}
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}
}
