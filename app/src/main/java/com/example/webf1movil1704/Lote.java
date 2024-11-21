package com.example.webf1movil1704;

public class Lote {
    private int id;
    private String ubicacion;
    private double areaSembrada;

    public Lote(int id, String ubicacion, double areaSembrada) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.areaSembrada = areaSembrada;
    }

    public int getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public double getAreaSembrada() {
        return areaSembrada;
    }
}
