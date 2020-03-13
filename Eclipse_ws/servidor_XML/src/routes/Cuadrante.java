package routes;

import java.util.ArrayList;

/**
 * 
 * En esta clase almacenaremos la información referente a los cuadrantes que componen el mapeado del
 * edificio. Cada cuadrante tendrá un identificador ID, un valor que indicará su planta Z, un String 
 * que contendrá el identificador del beacon asociado a dicho cuadrante, un array de String
 * que indicarán el ID del cuadrante con el que esté conectado según el esquema indicado y dos ArrayList
 * de String, uno que contendrá los objetos identificativos del cuadrante y otro que contendrála información
 * del cuadrante.
 * 
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class Cuadrante {
	
	private int ID;
	private String beacon;
	//private Posicion posNW;
	//private Posicion posSE;
	private int Z;
	/**	
	 * conectado[0] = norte
	 * conectado[1] = sur
	 * conectado[2] = este
	 * conectado[3] = oeste
	 */
	String[] conectado = new String[4];
	ArrayList<String> objetos;
	ArrayList<String> info;
	
	
	public Cuadrante(int id, String beac,/*Posicion nw, Posicion se,*/ int z, String[] conexiones, ArrayList<String> objs, ArrayList<String> informacion){
		
		ID = id;
		info=informacion;
		beacon=beac;
		//posNW = nw;
		//posSE = se;
		conectado = conexiones;
		objetos = objs;
		Z = z;
		
	}

	public boolean equals(Object o){
		
		Cuadrante c = (Cuadrante)o;
		
		//if ((this.ID==c.getID()) && (posNW.equals(c.getPosNW())) && (posSE.equals(c.getPosSE())))
			//return true;
		if(ID==c.getID() && beacon.equals(c.getBeacon())) 
			return true;
		else
			return false;	
		
	}
	
	public Cuadrante clone(){
		
		return new Cuadrante(this.ID, this.beacon,/*this.posNW, this.posSE,*/this.Z, this.conectado, this.objetos, this.info);
		
	}
	
	/**
	 * Compureba si una posición forma parte del cuadrante.
	 * @param p Posición
	 * @return True si la posición p está dentro del cuadrante.
	 */
	/*public boolean pertenece(Posicion p) {
		
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
		
	}*/
	
	/*public boolean pertenece(double pX, double pY) {
		
		Posicion p = new Posicion(pX, pY);
		
		return pertenece(p);
		
	}*/
	
	public int getID() {
		return ID;
	}
	public String getBeacon() {
		return beacon;
	}

	public void setID(int id) {
		ID = id;
	}

	/*public Posicion getPosNW() {
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
	}*/

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
