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
	private float metros;
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
	//ArrayList<String> objetos;
	String info;
	
	
	public Cuadrante(int id, String beac, int z, String[] conexiones, /*ArrayList<String> objs,*/ String informacion, float longi){
		
		ID = id;
		info = informacion;
		beacon = beac;
		conectado = conexiones;
		//objetos = objs;
		Z = z;
		metros = longi;
		
	}

	public boolean equals(Object o){
		
		Cuadrante c = (Cuadrante)o;
		
		if(ID==c.getID() && beacon.equals(c.getBeacon())) 
			return true;
		else
			return false;	
		
	}
	
	public Cuadrante clone(){
		
		return new Cuadrante(this.ID, this.beacon, this.Z, this.conectado, /*this.objetos,*/ this.info, this.metros);
		
	}
	
	public int getID() {
		return ID;
	}
	public String getBeacon() {
		return beacon;
	}

	public void setID(int id) {
		ID = id;
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
	
	public float getMetros() {
		return metros;
	}
	
	public String getInfo() {
		return info;
	}
		
}
