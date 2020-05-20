package routes;

import java.util.ArrayList;


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
		
	private ArrayList<Cuadrante> lista;
	private int[][] matrizAdy;
	@SuppressWarnings("unused")
	private int[][] matrizOriginal;
		
	public ListaCuadrantes(ArrayList<Cuadrante> aCuadrantes){

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
				if (norte >= 0) {
					matrizAdy[i][norte] = lista.get(i).getPesos()[0];
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int sur = Integer.parseInt(conectado[1]);
				if (sur >= 0) {
					matrizAdy[i][sur] = lista.get(i).getPesos()[1];
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int este = Integer.parseInt(conectado[2]);
				if (este >= 0) {
					matrizAdy[i][este] = lista.get(i).getPesos()[2];
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			try {
				
				int oeste = Integer.parseInt(conectado[3]);
				if (oeste >= 0) {
					matrizAdy[i][oeste] = lista.get(i).getPesos()[3];
				}
									
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static int numCuadrante(String beaconActual, ArrayList<Cuadrante> aCuadrantes) {
		
		for (int i= 0; i<aCuadrantes.size(); i++) {
			
			Cuadrante c = aCuadrantes.get(i);
			
			if (c.getBeacon().equals(beaconActual))
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
		
	} 
	
}
