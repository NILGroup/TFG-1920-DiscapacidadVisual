package routes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import xml.Edificio;

/**
 * 
 * En esta clase almacenaremos la lista completa de cuadrantes (de todos los xml). Esta lista incluirá 
 * la unión entre éstos así como el cálculo de la ruta entre dos cuadrantes dados.
 * 
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class ListaCuadrantes {

	public static final int MAX = 9999;
	private static final int NO_PARENT = -1;

	private static int[] prev;
		
	private ArrayList<Cuadrante> lista;
	private int[][] matrizAdy;
	@SuppressWarnings("unused")
	private int[][] matrizOriginal;
		
	public ListaCuadrantes(ArrayList<Cuadrante> aCuadrantes){

		Edificio xml = new Edificio();
		lista = aCuadrantes;
		iniciarLista();
		
	}
	
	public void iniciarLista(){
		
		unirCuadrantes(lista.size());
		matrizOriginal = matrizAdy.clone();
		
	}
	

	private void unirCuadrantes(int size){
		
		matrizAdy = new int[size][size];

		for (int i = 0; i<size; i++) {
			for (int j = 0; j<size; j++) {
				matrizAdy[i][j] = MAX;
			}
		}
		
		for (int i=0; i<size;i++) {
			
			String[] conectado = lista.get(i).getConectado();
			
			try {
			
				int norte = Integer.parseInt(conectado[0]);
				if (norte >= 0)
				matrizAdy[i][norte] = 1;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int sur = Integer.parseInt(conectado[1]);
				if (sur >= 0)
				matrizAdy[i][sur] = 1;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int este = Integer.parseInt(conectado[2]);
				if (este >= 0)
				matrizAdy[i][este] = 1;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int oeste = Integer.parseInt(conectado[3]);
				if (oeste >= 0)
				matrizAdy[i][oeste] = 1;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/*public int numCuadrante(Posicion p) {
		
		for (int i= 0; i<lista.size(); i++) {
			
			Cuadrante c = lista.get(i);
			
			if (c.pertenece(p))
				return c.getID();
		}
		
		return -1;
	}*/
	
	
	/*public static int numCuadrante(Posicion p, double pZ, ArrayList<Cuadrante> aCuadrantes) {
		
		for (int i= 0; i<aCuadrantes.size(); i++) {
			
			Cuadrante c = aCuadrantes.get(i);
			
			if (c.pertenece(p,pZ))
				return c.getID();
		}
		
		return -1;
	}*/
	
	/*public int numCuadrante(double pX, double pY) {
		
		Posicion p = new Posicion(pX, pY);
		return numCuadrante(p);
		
	}*/
	
	public static int numCuadrante(String beaconActual, ArrayList<Cuadrante> aCuadrantes) {
		
		for (int i= 0; i<aCuadrantes.size(); i++) {
			
			Cuadrante c = aCuadrantes.get(i);
			
			if (c.getBeacon().equals(beaconActual))//en principio no hace falta comprobar la z
				return c.getID();
		}
		
		return -1;
	}
	
	public static String idBeacon(int cuadrante, ArrayList<Cuadrante> aCuadrantes) {
		Cuadrante c = aCuadrantes.get(cuadrante);
		return c.getBeacon();
	}
	
	
	public Cuadrante getCuadrante(int id) {
		
		for (int i= 0; i<lista.size(); i++) {
			
			if (lista.get(i).getID()==id)
				return lista.get(i);
			
		}
		
		return null;
	}
	
	

	public ArrayList<Cuadrante> getLista() {
		return lista;
	}

	public void setLista(ArrayList<Cuadrante> lista) {
		this.lista = lista;
	}

	public int[][] getMatrizAdyacencia() {
		return matrizAdy;
	}

	public void setMatrizAdyacencia(int[][] matrizAdyacencia) {
		this.matrizAdy = matrizAdyacencia;
	}

	/**
	 * Método de búsqueda en anchura para el cálculo de la ruta
	 * @param ini Cuadrante inicial
	 * @param fin Cuadrante final
	 * @return ArrayList de enteros con el recorrido
	 */
	public ArrayList<Integer> bfs(int ini, int fin) {
		
		int pasos = 0, actual;
		boolean visitado[ ] = new boolean[ MAX ];
		Arrays.fill( visitado , false );
		ArrayList<Integer> camino = new ArrayList<Integer>();
		prev = new int[ MAX ];

		prev[ ini ] = -1;

		Queue<Integer> Q = new LinkedList<Integer>();
		Q.add(ini);
		
		while (!Q.isEmpty()) {
		
			actual = Q.remove();
			pasos++;

			if(actual == fin) break;						//si se llego al destino

			visitado[actual] = true;

			for (int i = 0 ; i < lista.size() ; ++i) {//vemos adyacentes a nodo actual
				
				int v = matrizAdy[actual][i];
				
				//if (v != 0 && !visitado[i]) { //no visitado agregamos a cola
				if (v != MAX && !visitado[i]) { //no visitado agregamos a cola
					
					prev[i] = actual; //para ver recorrido de nodo inicio a fin
					Q.add( i );
					
				}
			}
		}

		for (int i=0;i<prev.length;i++)
			 camino.add(prev[i]);
		
		return camino;
	}
	
	public ArrayList<Integer> caminoConDijkstra(int ini, int fin) {
		
		Dijkstra d = new Dijkstra();
		ArrayList<Integer> camino = new ArrayList<Integer>();
		int [] parents = d.dijkstra(matrizAdy, ini);
		
		daCamino(fin, parents, camino);
		return camino;
	}
	
	public static void daCamino(int dest, 
			int[] parents, ArrayList<Integer> camino) 
	{ 
		
		// Base case : Source node has 
		// been processed 
		if (dest == NO_PARENT) 
		{ 
		return; 
		} 
		
		daCamino(parents[dest], parents, camino); 
		camino.add(dest);
		//System.out.print(dest + " "); 
	} 
	
}
