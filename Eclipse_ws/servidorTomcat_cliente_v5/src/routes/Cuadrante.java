package routes;

/**
 * 
 * En esta clase almacenaremos la información referente a los cuadrantes que componen el mapeado del
 * edificio. Cada cuadrante tendrá un identificador ID, un valor que indicará su planta Z, un String 
 * que contendrá el identificador del beacon asociado a dicho cuadrante, un array de String que indicarán
 * el ID del cuadrante con el que esté conectado según el esquema indicado, un valor que indicará la 
 * longitud en metros que ocupa y un ArrayList de String que contendrá la información relevante del cuadrante.
 * 23/03/2020 - Revisado y limpiado
 *
 */
public class Cuadrante {
	
	private int ID;
	private String beacon;
	private float metros;
	private int Z;
	private String posdestino;
	/**	
	 * conectado[0] = norte
	 * conectado[1] = sur
	 * conectado[2] = este
	 * conectado[3] = oeste
	 */
	String[] conectado = new String[4];
	int[] pesos = new int[4];

	String info;
	
	
	public Cuadrante(int id, String beac, int z, String[] conexiones, String posdest, int[] pesosMatriz ,String informacion, float longi){
		
		ID = id;
		info = informacion;
		beacon = beac;
		conectado = conexiones;
		Z = z;
		posdestino = posdest;
		metros = longi;
		pesos = pesosMatriz;
		
	}

	public boolean equals(Object o){
		
		Cuadrante c = (Cuadrante)o;
		
		if(ID==c.getID() && beacon.equals(c.getBeacon())) 
			return true;
		else
			return false;	
		
	}
	
	public Cuadrante clone(){
		
		return new Cuadrante(this.ID, this.beacon, this.Z, this.conectado, this.posdestino, this.pesos, this.info, this.metros);
		
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
	
	public String getPosDestino() {
		return posdestino;
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
	
	public int[] getPesos() {
		return pesos;
	}
		
}
