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
	private ArrayList<Planta> aEstancias;
	private ArrayList<Cuadrante> aCuadrantes;
	private int cuadranteClave;

	public GenerarRuta(ArrayList<Integer> _lCuadrantes, ArrayList<Planta> _aEstancias, ArrayList<Cuadrante> _aCuadrantes) {
		
		lCuadrantes=_lCuadrantes;
		aEstancias=_aEstancias;
		aCuadrantes = _aCuadrantes;
		
	}
	
	public String[] generar(int posAct, int posDest){
		
		
		
		Iterator<Integer> it = lCuadrantes.iterator();
		String s = "";
		String [] res = new String[3]; //res[0] = ruta; res[1] = si hay giro o no;
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
			if(primerCuadrante > 0) {
				String rutaFinal = "";
				Cuadrante cuadAnt = aCuadrantes.get(lCuadrantes.get(primerCuadrante-1));
				if((posDest >= 0 && posDest <= 7) || (posDest >= 21 && posDest <= 26) || 
						(posDest >= 12 && posDest <= 16) || posDest == 30 || posDest == 36 
						|| posDest == 35) { //El destino es un aula
					
					if(c1.getDireccion(lCuadrantes.get(primerCuadrante-1)).equals("este") || posDest == 30
							|| (c1.getDireccion(lCuadrantes.get(primerCuadrante-1)).equals("oeste") && 
							(posDest >= 12 && posDest <= 16) ) || 
							(posDest == 36 && (cuadAnt.getID() == 32 || cuadAnt.getID() == 35))
							|| (posDest == 35 && cuadAnt.getID() == 34)) { //La direccion en la que vas es oeste
						rutaFinal = "Su destino está a la izquierda. El recorrido ha finalizado.";
					}
					else { //Vas en direcion este
						rutaFinal = "Su destino está a la derecha. El recorrido ha finalizado.";
					}
				}
				else { //El destino no es un aula
					rutaFinal = "Su destino está delante. El recorrido ha finalizado.";
				}
				res[0] = rutaFinal;
				//return rutaFinal;
				return res;
			}
			else {
				res[0] = "Ya te encuentras en el destino";
				//return "Ya te encuentras en el destino";
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
		//Suponemos que no va a iniciar la ruta en el ascensor
		if (c1.getZ() > c2.getZ()) {
			cuadranteClave = c2.getID();
			if(cuadAnt.getID() == 2 || cuadAnt.getID() == 22) {
				res[0] = "Los ascensores están a tu izquierda. Baja a la planta cero.";
				return res;
			}
				//return "Los ascensores están a tu izquierda. Baja a la planta cero.";
			else if(cuadAnt.getID() == 32 || cuadAnt.getID() == 17) {
				res[0] = "Los ascensores están a tu derecha. Baja a la planta cero.";
				return res;
				//return "Los ascensores están a tu derecha. Baja a la planta cero.";
			}
			else {
				res[0] = "El ascensor está delante. Baja a la planta cero.";
				return res;
				//return "El ascensor está delante. Baja a la planta cero.";
			}
		}
		else if(c2.getZ() > c1.getZ()) {
			cuadranteClave = c2.getID();
			if(cuadAnt.getID() == 2 || cuadAnt.getID() == 22) {
				res[0] = "Los ascensores están a tu izquierda. Sube a la primera planta.";
				return res;
				//return "Los ascensores están a tu izquierda. Sube a la primera planta.";
			}
			else if(cuadAnt.getID() == 32 || cuadAnt.getID() == 17) {
				res[0] = "Los ascensores están a tu derecha. Sube a la primera planta.";
				return res;
				//return "Los ascensores están a tu derecha. Sube a la primera planta.";
			}
			else {
				res[0] = "El ascensor está delante. Sube a la primera planta.";
				return res;
				//return "El ascensor está delante. Sube a la primera planta.";
			}
		}
		else {
			
			Planta est = null;
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
				if(cuadAnterior.getZ() != c1.getZ()) { //Acabamos de subir o bajar una planta
					if(c1.getID() == 29 || c1.getID() == 10) {
						cuadranteClave = c2.getID();
						res[0] ="Continua recto " + Float.toString(c1.getMetros()) +" metros para salir de la zona de ascensores.";
						//return "Continua recto " + Float.toString(c1.getMetros()) +" metros para salir de la zona de ascensores.";
						return res;
					}
					else if(c1.getID() == 31) {
						cuadranteClave = c2.getID();
						if(c2.getID() == 32) {
							res[0] = "Gira a la izquierda y avanza " + Float.toString(c1.getMetros()) + " metros. Espera la siguiente indicación.";
							res[1] = "si";
							return res;
							//return "Gira a la izquierda y avanza " + Float.toString(c1.getMetros()) + " metros. Espera la siguiente indicación.";
						}
						else {
							res[0] = "Gira a la derecha y avanza " + Float.toString(c1.getMetros()) + "metros. Espera la siguiente indicación.";
							res[1] = "si";
							return res;
							//return "Gira a la derecha y avanza " + Float.toString(c1.getMetros()) + "metros. Espera la siguiente indicación.";
						}
					}
				}
				dirDeLaQueVengo = cuadAnterior.getDireccion(c1);
			}
			
			int cont = 1; //Contador de cuadrantes que avanzamos sin instrucción
			float metros = c1.getMetros();
			int contHastaCambioDir = 1;
			//Cuadrante aux = null;
			
			cuadranteClave = c2.getID(); //El cuadrante clave siempre es el siguiente
			
			while(direccion == direccionPrincipal && lCuadrantes.get(ultimoCuadrante) != posDest
					&& /*cont < 2*/ contHastaCambioDir < 8) {
				//if(cont < 2) {
					ultimoCuadrante++;
					Cuadrante c3 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
					if(c3 == null) {break;} //El siguiente cuadrante es de otra planta
					direccionSig = c2.getDireccion(c3);
					if(direccionSig == direccionPrincipal) {
						c2 = c3;
						cont++;
						direccion = direccionSig;
						metros += c2.getMetros();
						//aux = c2.clone();					
						}
					else {//hay que girar
						break;
					}
				//}
				/*else {
					ultimoCuadrante++;
					Cuadrante c3 = est.getCuadrante(lCuadrantes.get(ultimoCuadrante));
					if(c3 == null) {break;} //El siguiente cuadrante es de otra planta
					String direccionAux = aux.getDireccion(c3);
					if(direccionAux == direccionPrincipal) {
						aux = c3;
						metros += aux.getMetros();
					}
					else {break;}
				}*/
				contHastaCambioDir++;
			} //Al salir, tenemos en c2 el cuadrante clave (ya no, el cuadranteClave es siempre el siguiente)
			
				//String[] resAux = new String[2];
				if(direccion.equals(dirDeLaQueVengo)) {
					s = "Continua recto " + Float.toString(metros) +" metros, luego ";
					direccion = direccionSig;	
					s += generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
					
					//resAux = generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
					
				}
				else {
					if(!dirDeLaQueVengo.equals(direccionSig)) {
						direccion = direccionSig;
					}
					//resAux = generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
					s = generaInstruccion(cont, direccionPrincipal, direccion, dirDeLaQueVengo);
					//s = resAux[0];
					s += " luego continua recto " + Float.toString(metros) +" metros.";
					res[1]="si";
				}
				//cuadranteClave = c2.getID(); SE PONE ANTES PARA QUE VAYA UNO POR UNO!
				//res[1] = resAux[1];
				//Añadimos información sobre el siguiente cuadrante si está el modo verbose activado
				//if(verbose) s += "Información adicional: " + c2.getInfo();
				//res[2] = "Información adicional: " + c2.getInfo();
		}
		
		res[0] = s;
		return res;
		//return s;
	}
	
	public String generaInstruccion(int cont, String direccionPrincipal, String direccion,
			String dirDeLaQueVengo) {
		String s = "";
		//String [] res = new String[2];
		//res[1] = "no";
		
		if (!direccion.equals(direccionPrincipal)) { //Giramos
			//res[1] = "si";
			s += ("gira a la ");
			
			if (direccionPrincipal == "norte" ) {
				
				if (direccion == "este")
					s += ("derecha,");
				else if (direccion == "oeste")
					s += ("izquierda,");
				
			}else if (direccionPrincipal == "sur") {
				
				if (direccion == "oeste")
					s += ("derecha,");
				else if (direccion == "este")
					s += ("izquierda.,");
				
			}else if (direccionPrincipal == "este" ) {
				
				if (direccion == "sur")
					s += ("derecha,");
				else if (direccion == "norte")
					s += ("izquierda,");
				
			}else if (direccionPrincipal == "oeste" ) {
				
				if (direccion == "norte")
					s += ("derecha,");
				else if (direccion == "sur")
					s += ("izquierda,");
				
			}
			
		}else if (!direccion.equals(dirDeLaQueVengo) ) { //Giramos
			//res[1]= "si";
			s += ("gira a la ");
			
			if (dirDeLaQueVengo == "norte") {
				
				if (direccion == "este")
					s += ("derecha,");
				else if (direccion == "oeste")
					s += ("izquierda,");
				
			}else if (dirDeLaQueVengo == "sur") {
				
				if (direccion == "oeste")
					s += ("derecha,");
				else if (direccion == "este")
					s += ("izquierda,");
				
			}else if (dirDeLaQueVengo == "este") {
				
				if (direccion == "sur")
					s += ("derecha,");
				else if (direccion == "norte")
					s += ("izquierda,");
				
			}else if (dirDeLaQueVengo == "oeste") {
				
				if (direccion == "norte")
					s += ("derecha,");
				else if (direccion == "sur")
					s += ("izquierda,");
			}
			
		} else {//No cambio de dirección
			
			/*if(verbose) {
				s += ("espera a la siguiente indicación.");
			}else {
				
				if (cont == 2) {//Medio del pasillo
					
					s += ("espera a la siguiente indicación.");
				
				} else {
					
					s += ("habrás llegado a tu destino.");
				}
			
			}*/
			s += ("espera a la siguiente indicación.");
		}
		//res[0] = s;
		//return res;
		return s;
		
		/*else if (cont == 2) {//Medio del pasillo
		
			s += ("espera a la siguiente indicación.");
		
		} else {
			
			s += ("habrás llegado a tu destino.");
			
		}*/
		

	}

	public int getCuadranteClave() {
		return cuadranteClave;
	}

}
