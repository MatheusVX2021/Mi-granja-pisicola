package com.example.myapplication.data.model;

public class EstanqueUI {
    private int id; // ID real de la base de datos
    private String imagen; 
    private String nombre;
    private int cantidadPeces;
    private String ultimaAlimentacion;
    private double pesoPromedio;
    private int edadPromedio;
    private double area;

    public EstanqueUI(int id, String imagen, String nombre, int cantidadPeces, String ultimaAlimentacion, double pesoPromedio, int edadPromedio, double area) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.cantidadPeces = cantidadPeces;
        this.ultimaAlimentacion = ultimaAlimentacion;
        this.pesoPromedio = pesoPromedio;
        this.edadPromedio = edadPromedio;
        this.area = area;
    }

    // Getters
    public int getId() { return id; }
    public String getImagen() { return imagen; }
    public String getNombre() { return nombre; }
    public int getCantidadPeces() { return cantidadPeces; }
    public String getUltimaAlimentacion() { return ultimaAlimentacion; }
    public double getPesoPromedio() { return pesoPromedio; }
    public int getEdadPromedio() { return edadPromedio; }
    public double getArea() { return area; }
}