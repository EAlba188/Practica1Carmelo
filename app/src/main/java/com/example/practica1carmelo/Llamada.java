package com.example.practica1carmelo;

import java.util.Comparator;

public class Llamada implements Comparable<Llamada>{

    private String nombre;
    private String año;
    private String mes;
    private String dia;
    private String hora;
    private String min;
    private String num;
    private String seg;

    public Llamada(String nombre,String num, String año, String mes,String dia,String hora,String min,String seg){
        this.año=año;
        this.nombre=nombre;
        this.mes=mes;
        this.dia=dia;
        this.hora=hora;
        this.min=min;
        this.seg=seg;
        this.num=num;
    }

    public int compareTo(Llamada c){
        return this.getNombre().compareTo(c.getNombre());
    }



    public String getNombre() {
        return nombre;
    }

    public String getAño() {
        return año;
    }

    public String getMes() {
        return mes;
    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    public String getMin() {
        return min;
    }

    public String getSeg() {
        return seg;
    }

    public String getNum() {
        return num;
    }
}
