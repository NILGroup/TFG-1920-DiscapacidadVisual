package com.example.app_guia_v3.routes;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * Esta clase servir� para almacenar estancias como pasillos, aulas y similares.
 * Contendr� el tipo, un identificador y una lista de los cuadrantes que
 * lo componen.
 *
 * 26/05/2014 - Revisado y limpiado.
 *
 */

public class Estancia {

    private String tipo;
    private String id;
    private ArrayList<Cuadrante> listaCuadrantes;

    /**
     * Constructora de de la clase estancia. La lista de cuadrantes se crear� vac�a.
     *
     * @param t: tipo de estancia (mirar clase enumerada Tipo)
     * @param i: identificador �nico para la estancia
     *
     * Ejemplo: Estancia(Tipo.AULA, "Aula 12")
     */
    public Estancia(String t, String i) {

        tipo = t;
        id = i;
        listaCuadrantes = new ArrayList<Cuadrante>();

    }

    public void add(Cuadrante c) {

        listaCuadrantes.add(c);

    }

    public String getTipo() {

        return tipo;

    }

    public String getTipoString() {

        return tipo.toString();

    }

    public String getId() {

        return id;

    }

    public boolean perteneceCuadrante(Cuadrante c) {

        return listaCuadrantes.contains(c);

    }

    public ArrayList<Cuadrante> getListaCuadrantes() {

        return listaCuadrantes;

    }

    public boolean estan(Integer primero, Integer segundo) {

        int i = 0;

        Iterator<Cuadrante> it = listaCuadrantes.iterator();

        while(it.hasNext() && i < 2) {

            Cuadrante c = it.next();
            if (c.getID() == primero || c.getID() == segundo)
                i++;

        }

        return i == 2;

    }

    public Cuadrante getCuadrante(int indice) {

        Iterator<Cuadrante> it = listaCuadrantes.iterator();

        while(it.hasNext()) {

            Cuadrante c = it.next();
            if (c.getID() == indice)
                return c;

        }

        return null;


    }

}
