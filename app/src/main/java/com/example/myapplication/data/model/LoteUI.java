package com.example.myapplication.data.model;

public class LoteUI {
    private int id;
    private String nombre;
    private Integer idEspecie;
    private String nombreEspecie;
    private String imagenEspecie;
    private Integer idEstanque;
    private String nombreEstanque;
    private Integer idProveedor;
    private String nombreProveedor;
    private int cant_ini;
    private int edad;
    private int cant_act;
    private int cant_sac;
    private int cant_ven;
    private double peso_promedio;
    private String fecha_entrada;

    public LoteUI(int id, String nombre, Integer idEspecie, String nombreEspecie, String imagenEspecie,
                  Integer idEstanque, String nombreEstanque, Integer idProveedor, String nombreProveedor,
                  int cant_ini, int edad, int cant_act, int cant_sac, int cant_ven, double peso_promedio, String fecha_entrada) {
        this.id = id;
        this.nombre = nombre;
        this.idEspecie = idEspecie;
        this.nombreEspecie = nombreEspecie;
        this.imagenEspecie = imagenEspecie;
        this.idEstanque = idEstanque;
        this.nombreEstanque = nombreEstanque;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.cant_ini = cant_ini;
        this.edad = edad;
        this.cant_act = cant_act;
        this.cant_sac = cant_sac;
        this.cant_ven = cant_ven;
        this.peso_promedio = peso_promedio;
        this.fecha_entrada = fecha_entrada;
    }

    public int getEdad() { return edad; }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getIdEspecie() { return idEspecie; }
    public String getNombreEspecie() { return nombreEspecie; }
    public String getImagenEspecie() { return imagenEspecie; }
    public Integer getIdEstanque() { return idEstanque; }
    public String getNombreEstanque() { return nombreEstanque; }
    public Integer getIdProveedor() { return idProveedor; }
    public String getNombreProveedor() { return nombreProveedor; }
    public int getCant_ini() { return cant_ini; }
    public int getCant_act() { return cant_act; }
    public int getCant_sac() { return cant_sac; }
    public int getCant_ven() { return cant_ven; }
    public double getPeso_promedio() { return peso_promedio; }
    public String getFecha_entrada() { return fecha_entrada; }
}
