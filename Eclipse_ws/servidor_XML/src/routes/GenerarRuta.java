package routes;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * En esta clase, tomaremos la lista de cuadrantes de la ruta a recorrer y generaremos la ruta con strings. La idea
 * es comprobar dónde estamos, a dónde queremos movernos y qué hay en medio. Si no se encuentran en la misma estancia
 * quiere decir que estamos al lado de los ascensores/escaleras y el siguiente cuadrante clave está en otro piso.
 * Si están en la misma estancia, comprobamos i hay que girar. Si no, indicamos la mitad del recorrido.
 * 
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class GenerarRuta {
	
	private ArrayList<Integer> lCuadrantes;
	private ArrayList<Estancia> aEstancias;
	private ArrayList<Cuadrante> aCuadrantes;
	private int cuadranteClave;

	public GenerarRuta(ArrayList<Integer> _lCuadrantes, ArrayList<Estancia> _aEstancias, ArrayList<Cuadrante> _aCuadrantes) {
		
		lCuadrantes=_lCuadrantes;
		aEstancias=_aEstancias;
		aCuadrantes = _aCuadrantes;
		
	}
	
	public String generar(int posAct, int posDest){
		
		if (posAct == posDest)
			return "Recorrido finalizado";
		
		Iterator<Integer> it = lCuadrantes.iterator();
		String s = "";
		
		int primerCuadrante=0; //Indice en la lista de cuadrantes
		boolean encontrado = false;
		
		while (it.hasNext() && !encontrado) {
			
			int pos = it.next();
			
			if(pos == posAct)
				encontrado = true;
			else
				primerCuadrante++;
			
		}
		
		int ultimoCuadrante = primerCuadrante + 1; //Indice en la lista de cuadrantes
		
		//Cogemos los cuadrantes de la lista general
		Cuadrante c1 , c2;
		c1 = aCuadrantes.get(lCuadrantes.get(primerCuadrante));
		c2 = aCuadrantes.get(lCuadrantes.get(ultimoCuadrante));
		
		//Hay que poner aquí las condiciones teniendo en cuenta que hay 3 plantas!!
		if (c1.getZ() > c2.getZ()) 
			return "Baja a la planta inferior.";
		else if(c2.getZ() > c1.getZ())
			return "Sube a la planta superior.";
		else {
			
			Estancia est = null;
			if(c1.getZ() == 1) {
				est = aEstancias.get(0);
			}
			else if(c1.getZ() == 0) {
				est = aEstancias.get(1);
			}
			else {
				//segunda planta
			}
			
			String direccionPrincipal = c1.getDireccion(c2);
			String direccion = direccionPrincipal;
			String dirDeLaQueVengo = direccionPrincipal;
			String direccionSig = direccionPrincipal;
			Cuadrante cuadAnterior = null;
			if(primerCuadrante > 0) { //Ya estamos en ruta
				cuadAnterior = aCuadrantes.get(lCuadrantes.get(primerCuadrante-1));
				dirDeLaQueVengo = cuadAnterior.getDireccion(c1);
			}
			
			int cont = 1; //Contador de cuadrantes. Al 8 paramos
			float metros = c1.getMetros();
			
			while(direccion == direccionPrincipal && lCuadrantes.get(ultimoCuadrante) != posDest
					&& cont < 8) {
				
				ultimoCuadrante++;
				Cuadrante c3 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
				direccionSig = c2.getDireccion(c3);
				if(direccionSig == direccionPrincipal) {
					c2 = c3;
					cont++;
					direccion = direccionSig;
					metros += c2.getMetros();
				}
				else {
					break;
				}
				
			} //Al salir, tenemos en c2 el cuadrante clave
			
			//Comprobación de planta
			//if (c2.getZ() == c1.getZ())  {
				
				if(direccion.equals(dirDeLaQueVengo)) {
					s = "Continua recto " + Float.toString(metros) +" metros.";
					direccion = direccionSig;
					s += generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
				}
				else {
					direccion = direccionSig;
					s = generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
					s += "Continua recto " + Float.toString(metros) +" metros.";
					
				}
				cuadranteClave = c2.getID();
		}
		return s;
	}
	
	public String generaInstruccion(int cont, String direccionPrincipal, String direccion, String dirDeLaQueVengo) {
		String s = "";
		
		if (!direccion.equals(direccionPrincipal) || !direccion.equals(dirDeLaQueVengo) ) { //Giramos
			
			s += ("Gira a la ");
			
			if (direccionPrincipal == "norte" || dirDeLaQueVengo == "norte") {
				
				if (direccion == "este")
					s += ("derecha.");
				else if (direccion == "oeste")
					s += ("izquierda.");
				
			}else if (direccionPrincipal == "sur" || dirDeLaQueVengo == "sur") {
				
				if (direccion == "oeste")
					s += ("derecha.");
				else if (direccion == "este")
					s += ("izquierda.");
				
			}else if (direccionPrincipal == "este" || dirDeLaQueVengo == "este") {
				
				if (direccion == "sur")
					s += ("derecha.");
				else if (direccion == "norte")
					s += ("izquierda.");
				
			}else if (direccionPrincipal == "oeste" || dirDeLaQueVengo == "oeste") {
				
				if (direccion == "norte")
					s += ("derecha.");
				else if (direccion == "sur")
					s += ("izquierda.");
				
			}
			
		} else if (cont == 8) {//Medio del pasillo
		
			s += ("Espera a la siguiente indicación.");
		
		} else {
			
			s += ("Has llegado a tu destino.");
			
		}
		
		return s;
	}

	public int getCuadranteClave() {
		return cuadranteClave;
	}

}
