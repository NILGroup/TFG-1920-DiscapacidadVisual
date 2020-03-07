package com.avanti.test;

import java.util.ArrayList;

import com.avanti.routes.Arista;

import com.avanti.routes.ListaCuadrantes;
import com.avanti.routes.Posicion;

import junit.framework.TestCase;

public class TestListaCuadrantes extends TestCase{
	
	private ListaCuadrantes lista;

	public TestListaCuadrantes(String s){
		super(s);
		lista = new ListaCuadrantes();
	}
	
	public void testCaminoMasCorto(){
		ArrayList<Integer> sol = new ArrayList<Integer>();
		sol.add(19);
		sol.add(17);
		sol.add(0);
		sol.add(1);
		sol.add(2);
		assertEquals(sol, lista.rutaMasCorta(2));
	}
	
	public void testListaAdyacentes(){
		ArrayList<Integer> sol = new ArrayList<Integer>();
		sol.add(17);
		sol.add(18);
		assertEquals(sol, lista.listaAdyacentes(19));
	}
	
	public void testNumCuadrante(){
		Posicion p = new Posicion(5,3);
		//Posicion p2 = new Posicion(4,4);
		int sol = 1;
		int sol2 = -1;
		assertEquals(sol, lista.numCuadrante(p));
		assertEquals(sol2, lista.numCuadrante(4,4));
	}
	
	public void testDirArista(){
		int origen = 17;
		int destino = 19;
		assertEquals(Arista.DOWN_RIGHT, lista.getDirArista(origen, destino));
	}
	
/*	
	public void testBloquearCuadrante(){
		Fire f = new Fire(2,5,3,1);
		int[][] sol = lista.getMatrizAdyacencia().clone();
		lista.bloquearCuadrante(f);
		int[][] matriz = lista.getMatrizAdyacencia();
		//int[][] sol = new int[lista.getMatrizAdyacencia()[0].length][lista.getMatrizAdyacencia()[0].length];
		for (int i= 0; i<lista.getMatrizAdyacencia().length; i++){
			sol[i][1] = lista.MAX;
			sol[1][i] = lista.MAX;
		}
		for (int i= 0; i<lista.getMatrizAdyacencia().length; i++){
			assertEquals(sol[i], matriz[i]);
		}
	}

	public void testDesbloquearCuadrante(){
		int[][] sol = lista.getMatrizAdyacencia().clone();
		Fire f = new Fire(2,5,3,1);
		lista.bloquearCuadrante(f);
		lista.desbloquearCuadrante(f);
		int[][] matriz = lista.getMatrizAdyacencia();
		for (int i= 0; i<lista.getMatrizAdyacencia().length; i++){
			assertEquals(sol[i], matriz[i]);
		}
	}
*/
}
