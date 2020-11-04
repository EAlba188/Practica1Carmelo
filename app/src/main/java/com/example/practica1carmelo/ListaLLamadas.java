package com.example.practica1carmelo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ListaLLamadas {
    private ArrayList<Llamada> lista;


    public ListaLLamadas(){
        this.lista=new ArrayList<>();
    }

    public void añadirLlamada(String nombre,String num, String año, String mes,String dia,String hora,String min,String seg){
        Llamada nueva;
        nueva = new Llamada(nombre,num,año,mes,dia,hora,min,seg);
        lista.add(nueva);
    }

    public void sorting(){
        Collections.sort(lista);
    }


    public ArrayList<Llamada> getLista() {
        return lista;
    }






}
