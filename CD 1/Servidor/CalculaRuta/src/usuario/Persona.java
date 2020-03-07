package usuario;

import java.util.ArrayList;
import routes.Cuadrante;
import routes.ListaCuadrantes;

/**
 * Clase que se encarga de generar la ruta a seguir por el usuario. Inicialmente se calcula la ruta en 
 * CUADRANTES, como un camino mínimo.
 * Obtenida la ruta de cuadrantes, se genera la ruta teniendo en cuenta las direcciones que comunican los
 * cuadrantes y la distancia que se debe recorrer, de forma que se generar instrucciones por tramos.
 * 
 * @author Mariana
 *
 *26/05/2014 - Revisado y limpiado
 */

public class Persona {
	
	private ArrayList<Integer> camino;
	private ArrayList<ArrayList<String>> instrucciones = new ArrayList<ArrayList<String>>();
	private ListaCuadrantes l;
	
	public Persona(int actual,int destino,ArrayList<Cuadrante> aCuadrantes){
		
		l = new ListaCuadrantes(aCuadrantes);
		//Ruta de distancia mínima de cuadrantes
		camino = l.bfs(actual,destino);
		
	}
	
	public String convertirDir(int dir){
		switch(dir){
		
			case 1: return "Norte";
			case 2: return "Noreste";
			case 3: return "Este";
			case 4: return "Sureste";
			case 5: return "Sur";
			case 6: return "Suroeste";
			case 7: return "Oeste";
			case 8: return "Noroeste";
			default: return "No hay conexión";
		}
	}
	
	public ArrayList<Integer> getCamino() {
		return camino;
	}

	public void setCamino(ArrayList<Integer> camino) {
		this.camino = camino;
	}
	
	public ArrayList<ArrayList<String>> getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(ArrayList<ArrayList<String>> instrucciones) {
		this.instrucciones = instrucciones;
	}
	
	
}