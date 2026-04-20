package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "especie", indices = {@Index(value = {"nombre"}, unique = true)})
public class Especie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nombre;
    private String imagen;
    private int tm; // tiempo de maduracion

    public Especie(){}

    @Ignore
    public Especie(@NonNull String nombre, String imagen, int tm){
        this.nombre = nombre;
        this.imagen = imagen;
        this.tm = tm;
    }

    public int getId() {return id;}
    public void setId(int idEspecie) {this.id = idEspecie;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}
    public int getTm() {return tm;}
    public void setTm(int tm) {this.tm = tm;}


}
