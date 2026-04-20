package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cat_gasto")
public class CatGasto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nombre;
    @NonNull
    private String imagen;
    @NonNull
    private String color;

    public CatGasto(){}

    public CatGasto(@NonNull String nombre, @NonNull String imagen, @NonNull String color){
        this.nombre = nombre;
        this.imagen = imagen;
        this.color = color;
    }

    public int getId() {return id;}
    public void setId(int idCatGasto) {this.id = idCatGasto;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
}
