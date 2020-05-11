package routes;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * En esta clase, tomaremos la lista de cuadrantes de la ruta a recorrer y generaremos la ruta con strings. 
 * La idea es comprobar dónde estamos, a dónde queremos movernos y qué hay en medio. Si no se encuentran 
 * en la misma estancia quiere decir que estamos al lado de los ascensores/escaleras y el siguiente 
 * cuadrante clave está en otro piso. Si están en la misma estancia, comprobamos i hay que girar.
 * Si no, indicamos el siguiente punto clave.
 * 
 * 23/03/2020 - Revisado y limpiado
 *
 */
public class GenerarRuta {
	
	private ArrayList<Integer> lCuadrantes;
	private ArrayList<Planta> aPlantas;
	private ArrayList<Cuadrante> aCuadrantes;
	private int cuadranteClave;

	public GenerarRuta(ArrayList<Integer> _lCuadrantes, ArrayList<Planta> _aPlantas, ArrayList<Cuadrante> _aCuadrantes) {
		
		lCuadrantes=_lCuadrantes;
		aPlantas=_aPlantas;
		aCuadrantes = _aCuadrantes;
		
	}
	
	public String[] generar(int posAct, int posDest){
		
		Iterator<Integer> it = lCuadrantes.iterator();
		String s = "";
		String [] res = new String[3]; 	//res[0] = instruccion; res[1] = si hay giro o no, 
										//res[2] = info adicional del cuadrante siguiente al actual
		res[1] = "no";
		res[2] = "no";
		
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
		
		
		if (posAct == posDest) { //Si hemos llegado al destino
			if(primerCuadrante > 0) {//No estábamos de inicio en el destino
				String rutaFinal = "";
				//miramos el cuadrante anterior para ver de qué direccion venimos
				Cuadrante cuadAnt = aCuadrantes.get(lCuadrantes.get(primerCuadrante-1));
				
				String dirAnt = cuadAnt.getDireccion(c1);
				String dirDest = c1.getPosDestino();
				
				rutaFinal = "Su destino está " + indicaDirFinal(dirAnt, dirDest);
				res[0] = rutaFinal;
				return res;
			}
			else {
				res[0] = "Ya te encuentras en el destino";
				return res;
			}
		}
		
		c2 = aCuadrantes.get(lCuadrantes.get(ultimoCuadrante));
		
		//Completamos la informacion adicional
		res[2] = "Información adicional: " + c2.getInfo();
		
		//Condiciones si hay un cambio de planta
		Cuadrante cuadAnt = c1.clone();
		if(primerCuadrante > 0) {
			cuadAnt = aCuadrantes.get(lCuadrantes.get(primerCuadrante-1));
		}
		//Suponemos que no va a iniciar la ruta en el ascensor. En ese caso te devuelve que el ascensor está delante
		if (c1.getZ() != c2.getZ()) {
			cuadranteClave = c2.getID();
			
			String subeBaja = "Sube";
			
			if (c1.getZ() > c2.getZ()) {
				subeBaja = "Baja";
			}
			String dirAnt = cuadAnt.getDireccion(c1);
			String dirDest = c1.getPosDestino();
			
			res[0] = "El ascensor está "+ indicaDirFinal(dirAnt,dirDest) + ". " + subeBaja + " a la planta " + Integer.toString(c2.getZ()) + ".";
				return res;
		}
		
		else {//El siguiente cuadrante está en la misma planta
			Planta est = null;
			
			est = aPlantas.get(c1.getZ());
			
			String direccionPrincipal = c1.getDireccion(c2);
			String direccion = direccionPrincipal;
			String dirDeLaQueVengo = direccionPrincipal;
			String direccionSig = direccionPrincipal;
			Cuadrante cuadAnterior = null;
			if(primerCuadrante > 0) { //Ya estamos en ruta
				cuadAnterior = aCuadrantes.get(lCuadrantes.get(primerCuadrante-1));
				if(cuadAnterior.getZ() != c1.getZ()) { //Acabamos de subir o bajar una planta
					
					cuadranteClave = c2.getID();
					String dirAnt = c1.getDireccion(c2);
					String dirDest = c1.getPosDestino();
					String dir = indicaDirFinal(dirAnt,dirDest);
					if(dir.equals("delante")) {
						res[0] ="Continua recto " + Float.toString(c1.getMetros()) +" metros para salir de la zona de ascensores.";
						return res;
					}
					else {
						res[0] = "Gira " + dir + " y avanza " + Float.toString(c1.getMetros()) + " metros. Espera la siguiente indicación.";
						//res[1] = "si";
						if(dir.equals("a la derecha")){
							res[1]="der";
						}
						else {
							res[1]="iz";
						}
						return res;
					}
				}
				dirDeLaQueVengo = cuadAnterior.getDireccion(c1);
			}
			
			
			float metros = c1.getMetros();
			int contHastaCambioDir = 1;
			//int contSaltoCuadrante = 0; //Contador de cuadrantes que avanzamos sin instrucción
			//int maxCuadSaltados = 2; //Máximo número de cuadrantes que avanzamos sin instrucción
			
			cuadranteClave = c2.getID(); //El cuadrante clave siempre es el siguiente
										//Para modificarlo basta con poner esta instruccion detrás del bucle siguiente
			
			while(direccion == direccionPrincipal && lCuadrantes.get(ultimoCuadrante) != posDest
					&& contHastaCambioDir < 8) {
					ultimoCuadrante++;
					Cuadrante c3 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
					if(c3 == null) {break;} //El siguiente cuadrante es de otra planta
					direccionSig = c2.getDireccion(c3);
					if(direccionSig == direccionPrincipal) {
						c2 = c3;
						//contSaltoCuadrante++;
						direccion = direccionSig;
						metros += c2.getMetros();
											
						}
					else {//hay que girar
						break;
					}
					/*
					 if(contSaltoCuadrante == maxCuadSaltados){ 
					 break;}*/
				contHastaCambioDir++;
			}
			
			String dir = indicaDirFinal(dirDeLaQueVengo, direccionPrincipal);
			if(dirDeLaQueVengo.equals(direccionPrincipal)) {//Aún no hay que girar
				s = "Continua recto " + Float.toString(metros) +" metros. ";
				dir = indicaDirFinal(dirDeLaQueVengo, direccionSig);
				if(dir.equals("delante")) {
					//s += "espera la siguiente instrucción"; se elimina de las instrucciones
				}
				else {//Hay que hacer algún giro
					s += "Luego gira " + dir + ".";
				}
			}
			
			else {
				s = s += "Gira " + dir + ".";
				s += "Luego continua recto " + Float.toString(metros) +" metros.";
				//res[1]="si";
				if(dir.equals("a la derecha")){
					res[1]="der";
				}
				else {
					res[1]="iz";
				}
			}
		}
		
		res[0] = s;
		return res;
	}
	

	public String indicaDirFinal(String dirAnt, String dirDest) {
		String dirFinal = "delante";
		
		if(dirDest.equals("sur")) {
			if(dirAnt.equals("oeste")) {
				dirFinal = "a la izquierda";
			}
			else if(dirAnt.equals("este")) {
				dirFinal = "a la derecha";
			}
		}
		else if(dirDest.equals("norte")) {
			if(dirAnt.equals("oeste")) {
				dirFinal = "a la derecha";
			}
			else if(dirAnt.equals("este")) {
				dirFinal = "a la izquierda";
			}
		}
		else if(dirDest.equals("este")) {
			if(dirAnt.equals("sur")) {
				dirFinal = "a la izquierda";
			}
			else if(dirAnt.equals("norte")) {
				dirFinal = "a la derecha";
			}
		}
		else if(dirDest.equals("oeste")) {
			if(dirAnt.equals("norte")) {
				dirFinal = "a la izquierda";
			}
			else if(dirAnt.equals("sur")) {
				dirFinal = "a la derecha";
			}
		}
		return dirFinal;
	}
	

	public int getCuadranteClave() {
		return cuadranteClave;
	}

}
