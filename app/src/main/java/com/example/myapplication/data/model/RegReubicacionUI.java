package com.example.myapplication.data.model;

public class RegReubicacionUI {
    private int idRegReubicacion;
    private int idLote;
    private String nombreLote;
    private int idEstanqueOrigen;
    private String nombreEstanqueOrigen;
    private int idEstanqueDestino;
    private String nombreEstanqueDestino;
    private int unidades;
    private String fecha;

    public RegReubicacionUI(int idRegReubicacion, int idLote, String nombreLote, 
                            int idEstanqueOrigen, String nombreEstanqueOrigen, 
                            int idEstanqueDestino, String nombreEstanqueDestino, 
                            int unidades, String fecha) {
        this.idRegReubicacion = idRegReubicacion;
        this.idLote = idLote;
        this.nombreLote = nombreLote;
        this.idEstanqueOrigen = idEstanqueOrigen;
        this.nombreEstanqueOrigen = nombreEstanqueOrigen;
        this.idEstanqueDestino = idEstanqueDestino;
        this.nombreEstanqueDestino = nombreEstanqueDestino;
        this.unidades = unidades;
        this.fecha = fecha;
    }

    public int getIdRegReubicacion() { return idRegReubicacion; }
    public int getIdLote() { return idLote; }
    public String getNombreLote() { return nombreLote; }
    public int getIdEstanqueOrigen() { return idEstanqueOrigen; }
    public String getNombreEstanqueOrigen() { return nombreEstanqueOrigen; }
    public int getIdEstanqueDestino() { return idEstanqueDestino; }
    public String getNombreEstanqueDestino() { return nombreEstanqueDestino; }
    public int getUnidades() { return unidades; }
    public String getFecha() { return fecha; }
}
