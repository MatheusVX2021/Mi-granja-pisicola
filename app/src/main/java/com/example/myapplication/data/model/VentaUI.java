package com.example.myapplication.data.model;

public class VentaUI {
    private int id;
    private int idLote;
    private String nombreLote;
    private int idCliente;
    private String nombreCliente;
    private int unidades;
    private int peso;
    private double monto;
    private String fecha;

    public VentaUI() {}

    public VentaUI(int id, int idLote, String nombreLote, int idCliente, String nombreCliente, int unidades, int peso, double monto, String fecha) {
        this.id = id;
        this.idLote = idLote;
        this.nombreLote = nombreLote;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.unidades = unidades;
        this.peso = peso;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters y Setters necesarios para Room
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public String getNombreLote() { return nombreLote; }
    public void setNombreLote(String nombreLote) { this.nombreLote = nombreLote; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }

    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
