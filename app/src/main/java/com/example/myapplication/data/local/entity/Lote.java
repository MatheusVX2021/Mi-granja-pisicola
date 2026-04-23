package com.example.myapplication.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "lote",
        foreignKeys = {
                @ForeignKey(entity = Especie.class,
                        parentColumns = "id",
                        childColumns = "idEspecie",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Estanque.class,
                        parentColumns = "id",
                        childColumns = "idEstanque",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Proveedor.class,
                        parentColumns = "id",
                        childColumns = "idProveedor",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"nombre"}, unique = true),
                @Index("idEspecie"),
                @Index("idEstanque"),
                @Index("idProveedor")
        })
public class Lote {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nombre;
    @NonNull
    private Integer idEspecie;
    @NonNull
    private Integer idEstanque;
    private Integer idProveedor;
    private int cant_ini;
    private int cant_act;
    private int cant_sac;
    private int cant_ven;
    private double peso_promedio;
    private int edad;
    @NonNull
    private String fecha_entrada;

    public Lote() {}

    public Lote(@NonNull String nombre, @NonNull Integer idEspecie, @NonNull Integer idEstanque, Integer idProveedor, int cant_ini, int cant_act, double peso_promedio, @NonNull String fecha_entrada) {
        this.nombre = nombre;
        this.idEspecie = idEspecie;
        this.idEstanque = idEstanque;
        this.idProveedor = idProveedor;
        this.cant_ini = cant_ini;
        this.cant_act = cant_act;
        this.peso_promedio = peso_promedio;
        this.fecha_entrada = fecha_entrada;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso_promedio() {
        return peso_promedio;
    }

    public void setPeso_promedio(double peso_promedio) {
        this.peso_promedio = peso_promedio;
    }

    @NonNull
    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(@NonNull String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public Integer getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(@NonNull Integer idEspecie) {
        this.idEspecie = idEspecie;
    }

    @NonNull
    public Integer getIdEstanque() {
        return idEstanque;
    }

    public void setIdEstanque(@NonNull Integer idEstanque) {
        this.idEstanque = idEstanque;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getCant_ini() {
        return cant_ini;
    }

    public void setCant_ini(int cant_ini) {
        this.cant_ini = cant_ini;
    }

    public int getCant_act() {
        return cant_act;
    }

    public void setCant_act(int cant_act) {
        this.cant_act = cant_act;
    }

    public int getCant_sac() {
        return cant_sac;
    }

    public void setCant_sac(int cant_sac) {
        this.cant_sac = cant_sac;
    }

    public int getCant_ven() {
        return cant_ven;
    }

    public void setCant_ven(int cant_ven) {
        this.cant_ven = cant_ven;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre != null ? nombre : "";
    }
}
