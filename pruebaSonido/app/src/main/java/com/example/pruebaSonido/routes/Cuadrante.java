package com.example.pruebaSonido.routes;

import android.util.Log;

import com.kontakt.sdk.android.common.Proximity;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * En esta clase almacenaremos la informaci�n referente a los cuadrantes que componen el mapeado del
 * edificio. Cada cuadrante tendr� un identificador ID, un valor que indicar� su planta Z, dos atributos
 * de tipo Posicion que indicar�n las esquinas opuestas del cuadrante PosNW y posSE, un array de String
 * que indicar�n el ID del cuadrante con el que est� conectado seg�n el esquema indicado y un ArrayList
 * de String que contendr�n los objetos identificativos del cuadrante.
 *
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class Cuadrante {

    private int ID;
    private Posicion posNW;
    private Posicion posSE;
    private int Z;
    /**
     * conectado[0] = norte
     * conectado[1] = sur
     * conectado[2] = este
     * conectado[3] = oeste
     */
    String[] conectado = new String[4];
    ArrayList<String> objetos;

    String[] beacons = new String[3];
    public static final String TAG = "CUADRANTE";

    public Cuadrante(int id, Posicion nw, Posicion se, int z, String[] conexiones, String[] beacons2,
                     ArrayList<String> objs){

        ID = id;
        posNW = nw;
        posSE = se;
        conectado = conexiones;
        beacons = beacons2;
        objetos = objs;
        Z = z;

    }

    public boolean equals(Object o){

        Cuadrante c = (Cuadrante)o;

        if ((this.ID==c.getID()) && (posNW.equals(c.getPosNW())) && (posSE.equals(c.getPosSE())))
            return true;
        else
            return false;

    }

    public Cuadrante clone(){

        return new Cuadrante(this.ID, this.posNW, this.posSE,this.Z, this.conectado,this.beacons, this.objetos);

    }

    /**
     * Compureba si una posici�n forma parte del cuadrante.
     * @param p Posici�n
     * @return True si la posici�n p est� dentro del cuadrante.
     */
    public boolean pertenece(Posicion p) {

        double xIzq = posNW.getPX();
        double xDcha = posSE.getPX();
        double ySup = posNW.getPY();
        double yInf = posSE.getPY();

        if ((xIzq<=p.getPX())&&(p.getPX()<xDcha)&&(ySup<=p.getPY())&&(p.getPY()<yInf))
            return true;
        else
            return false;

    }

    public boolean pertenece(Posicion p, double pZ) {

        double xIzq = posNW.getPX();
        double xDcha = posSE.getPX();
        double ySup = posNW.getPY();
        double yInf = posSE.getPY();
        int miZ = (int) pZ;
        //  return true;
        //if ((xIzq<=p.getPX())&&(p.getPX()<xDcha)&&(ySup<=p.getPY())&&(p.getPY()<yInf) && (Z == pZ) )
        if ((xIzq<=p.getPX())&&(p.getPX()<xDcha)&&(ySup>=p.getPY())&&(p.getPY()>yInf) && (Z == miZ) )
            return true;
        else
            return false;

    }

    public boolean pertenece(List<IEddystoneDevice> eddystones) {
        Log.e(TAG, "El cuadrante "+ ID +" tiene: "+ beacons[0] + beacons[1] + beacons[2] + "\n");

        for(int i=0; i < eddystones.size();i++) {
            if (eddystones.get(i).getProximity() == Proximity.IMMEDIATE && beacons[0] != "nada" &&
            eddystones.get(i).getUniqueId() != beacons[0]){return false;}
            else if (eddystones.get(i).getProximity() == Proximity.NEAR && beacons[1] != "nada" &&
                    eddystones.get(i).getUniqueId() != beacons[1]){return false;}
            else if (eddystones.get(i).getProximity() == Proximity.FAR && beacons[2] != "nada" &&
                    eddystones.get(i).getUniqueId() != beacons[2]){return false;}
        }
        return true;
    }

	/*public boolean pertenece(double pX, double pY) {

		Posicion p = new Posicion(pX, pY);

		return pertenece(p);

	}*/

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public Posicion getPosNW() {
        return posNW;
    }

    public void setPosNW(Posicion posNW) {
        this.posNW = posNW;
    }

    public Posicion getPosSE() {
        return posSE;
    }

    public void setPosSE(Posicion posSE) {
        this.posSE = posSE;
    }

    public String getDireccion(Cuadrante c2) {

        for (int i = 0; i < 4; i++) {

            if (Integer.parseInt(conectado[i]) == c2.getID()) {

                switch(i) {
                    case 0:
                        return "norte";
                    case 1:
                        return "sur";
                    case 2:
                        return "este";
                    default:
                        return "oeste";
                }

            }

        }

        return "No conectado";

    }

    public String getObjeto() {
        return objetos.get(0);
    }

    public String getDireccion(Integer indice) {

        for (int i = 0; i < 4; i++) {

            if (Integer.parseInt(conectado[i]) == indice) {

                switch(i) {
                    case 0:
                        return "norte";
                    case 1:
                        return "sur";
                    case 2:
                        return "este";
                    default:
                        return "oeste";
                }

            }

        }

        return "No conectado";

    }

    public String[] getConectado() {
        return conectado;
    }

    public int getZ() {
        return Z;
    }

    public void setZ(int z) {
        Z = z;
    }

}
