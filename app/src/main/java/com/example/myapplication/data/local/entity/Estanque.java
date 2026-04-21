package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "estanque", indices = {@Index(value = {"nombre"}, unique = true)})
public class Estanque {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nombre;
    private double area;
    private String imagen;

    public Estanque(){}

    public Estanque(@NonNull String nombre, double area, String imagen){
        this.nombre = nombre;
        this.area = area;
        this.imagen = imagen;
    }

    public int getId() {return id;}
    public void setId(int idEstanque) {this.id = idEstanque;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public double getArea() {return area;}
    public void setArea(double area) {this.area = area;}
    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}

}
