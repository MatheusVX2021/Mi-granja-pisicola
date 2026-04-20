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
    private int volumen;
    private String imagen;
    private boolean estado;

    public Estanque(){}

    public Estanque(@NonNull String nombre, int volumen, String imagen, boolean estado){
        this.nombre = nombre;
        this.volumen = volumen;
        this.imagen = imagen;
        this.estado = estado;
    }

    public int getId() {return id;}
    public void setId(int idEstanque) {this.id = idEstanque;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public int getVolumen() {return volumen;}
    public void setVolumen(int volumen) {this.volumen = volumen;}
    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}
    public boolean getEstado() {return estado;}
    public void setEstado(boolean estado) {this.estado = estado;}

}
