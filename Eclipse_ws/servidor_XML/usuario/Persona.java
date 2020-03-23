package usuario;

import java.util.ArrayList;
import routes.Cuadrante;
import routes.ListaCuadrantes;

/**
 * Clase que se encarga de generar la ruta a seguir por el usuario. Inicialmente se calcula la ruta en 
 * CUADRANTES, como un camino mínimo utilizando el algoritmo de Dijkstra.
 * Obtenida la ruta de cuadrantes, se genera la ruta teniendo en cuenta las direcciones que comunican los
 * cuadrantes y la distancia que se debe recorrer, de forma que se generar instrucciones por tramos.
 * 
 *
 *26/05/2014 - Revisado y limpiado
 */

public class Persona {
	
	private ArrayList<Integer> camino;
	private ArrayList<ArrayList<String>> instrucciones = new ArrayList<ArrayList<String>>();
	private ListaCuadrantes l;
	
	private int[][] matrizAdy;
	
	public Persona(int actual,int destino,ArrayList<Cuadrante> aCuadrantes){
		
		l = new ListaCuadrantes(aCuadrantes);
		//Ruta de distancia mínima de cuadrantes
		camino = l.caminoConDijkstra(actual, destino);
		matrizAdy = l.getMatrizAdyacencia();
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
	
	public int[][] getMatrizAdy(){
		return this.matrizAdy;
	}
	
}