package com.example.myapplication.data.model;

public class RegAlimentacionUI {
    private int id;
    private String nombreEstanque;
    private String nombreAlimento;
    private int kilos;
    private String fecha;

    public RegAlimentacionUI(int id, String nombreEstanque, String nombreAlimento, int kilos, String fecha) {
        this.id = id;
        this.nombreEstanque = nombreEstanque;
        this.nombreAlimento = nombreAlimento;
        this.kilos = kilos;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public String getNombreEstanque() { return nombreEstanque; }
    public String getNombreAlimento() { return nombreAlimento; }
    public int getKilos() { return kilos; }
    public String getFecha() { return fecha; }
}
