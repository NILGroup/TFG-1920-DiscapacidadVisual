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
		
		Iterator<Estancia> itEst = aEstancias.iterator();
		boolean estan = false;
		Estancia est = null;
		
		while (itEst.hasNext() && !estan) {
			
			est = itEst.next();
			estan = est.estan(lCuadrantes.get(primerCuadrante), lCuadrantes.get(ultimoCuadrante));
			
		}
		
		//Cogemos los cuadrantes de la lista general
		Cuadrante c1 , c2;
		c1 = aCuadrantes.get(lCuadrantes.get(primerCuadrante));
		c2 = aCuadrantes.get(lCuadrantes.get(ultimoCuadrante));
		
		//Si están en la misma estancia
		if (estan) {
			
			c1 = est.getCuadrante(lCuadrantes.get(primerCuadrante));
			c2 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
			
			String direccionPrincipal = c1.getDireccion(c2);
			String direccion = direccionPrincipal;
			int cont = 1; //Contador de cuadrantes. Al 8 paramos
			
			while(direccion == direccionPrincipal && lCuadrantes.get(ultimoCuadrante) != posDest
					&& cont < 8) {
				
				ultimoCuadrante++;
				Cuadrante c3 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
				if (c3 == null) {//Diferente estancia
					direccion = c2.getDireccion(lCuadrantes.get(ultimoCuadrante));
					break;
				}
				direccion = c2.getDireccion(c3);
				c2 = c3;
				cont++;
				
			} //Al salir, tenemos en c2 el cuadrante clave
			
			//Comprobación de planta
			if (c2.getZ() == c1.getZ())  {
			
				String s = "Sigue recto hasta ";
				s += (c2.getObjeto());
				s += (". ");
				cuadranteClave = c2.getID();
			
				if (direccion != direccionPrincipal) { //Giramos
				
				s += ("Gira a la ");
				
				if (direccionPrincipal == "norte") {
					
					if (direccion == "este")
						s += ("derecha.");
					else if (direccion == "oeste")
						s += ("izquierda.");
					
				}else if (direccionPrincipal == "sur") {
					
					if (direccion == "oeste")
						s += ("derecha.");
					else if (direccion == "este")
						s += ("izquierda.");
					
				}else if (direccionPrincipal == "este") {
					
					if (direccion == "sur")
						s += ("derecha.");
					else if (direccion == "norte")
						s += ("izquierda.");
					
				}else if (direccionPrincipal == "oeste") {
					
					if (direccion == "norte")
						s += ("derecha.");
					else if (direccion == "sur")
						s += ("izquierda.");
					
				}
				
			} else if (cont == 8) {//Medio del pasillo
			
				s += ("Sigue recto hasta la siguiente indicación.");
			
			} else {
				
				s += ("Has llegado a tu destino.");
				
			}
			
			return s;
			
		} 
		} else if (c1.getZ() > c2.getZ()) 
			return "Baja a la planta inferior.";
		else if(c2.getZ() > c1.getZ())
			return "Sube a la planta superior.";
		else
			return "Cambio de estancia";
		return "NO SE LA RUTA";
	}

	public int getCuadranteClave() {
		return cuadranteClave;
	}

}
