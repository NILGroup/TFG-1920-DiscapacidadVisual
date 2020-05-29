package beacons.server_v1.routes;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.ArrayList;
import java.util.Iterator;

public class Estancia {
    private String tipo;
    private String id;
    private ArrayList<Cuadrante> listaCuadrantes;

    public Estancia(String t, String i) {
        this.tipo = t;
        this.id = i;
        this.listaCuadrantes = new ArrayList();
    }

    public void add(Cuadrante c) {
        this.listaCuadrantes.add(c);
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getTipoString() {
        return this.tipo.toString();
    }

    public String getId() {
        return this.id;
    }

    public boolean perteneceCuadrante(Cuadrante c) {
        return this.listaCuadrantes.contains(c);
    }

    public ArrayList<Cuadrante> getListaCuadrantes() {
        return this.listaCuadrantes;
    }

    public boolean estan(Integer primero, Integer segundo) {
        int i = 0;
        Iterator it = this.listaCuadrantes.iterator();

        while(it.hasNext() && i < 2) {
            Cuadrante c = (Cuadrante)it.next();
            if (c.getID() == primero || c.getID() == segundo) {
                ++i;
            }
        }

        return i == 2;
    }

    public Cuadrante getCuadrante(int indice) {
        Iterator it = this.listaCuadrantes.iterator();

        while(it.hasNext()) {
            Cuadrante c = (Cuadrante)it.next();
            if (c.getID() == indice) {
                return c;
            }
        }

        return null;
    }
}
