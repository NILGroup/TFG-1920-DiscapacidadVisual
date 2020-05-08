package principal;


import java.util.ArrayList;

import routes.Cuadrante;
import routes.Planta;
import routes.GenerarRuta;
import routes.LectorDestino;
import routes.ListaCuadrantes;
import usuario.Persona;
import xml.Edificio;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;


/**
 * 
 * En esta clase nos encargamos de la conexión directa con el cliente. Constituye el servidor de 
 * la aplicación, queda a la espera de clientes y cuando uno llega le atiende leyendo el origen de
 * su ruta, el destino al que quiere ir y su beacon más cercano. A partir de esa información genera 
 * la siguiente instrucción del cliente y vuelve a quedar a la espera de que el cliente vuelva a 
 * conectar una vez haya actualizado su posición actual.
 * 
 * 23/03/2020 - Revisado y limpiado
 *
 */

@ServerEndpoint("/websocketendpoint")
public class MainClienteAndroid {
	
	//Mapa con las sesiones que hay
	//Map<String, Session> sessions = new ConcurrentHashMap<>();

	
	private static ArrayList<Integer> lCuadrantes = new ArrayList<Integer>();
	private static ArrayList<Planta> aPlantas; //todas las estancias del edificio
	private static ArrayList<Cuadrante> aCuadrantes; //todos los cuadrantes del edificio
	private static Edificio edificio;
	private String beaconOrigen = "no", destino = "no";
	private LectorDestino lectorDest = null;
	private int cuadOrigen, cuadDestino;
	private GenerarRuta gr;
	
	

	@OnOpen
    public void onOpen(){
        System.out.println("Open Connection ...");
        
        //Cargamos la estructura del edificio
        edificio = new Edificio();
    	aPlantas = edificio.getPlantas();
    	aCuadrantes = edificio.getCuadrantes();
    	
    	//Cargamos los destinos
    	lectorDest = new LectorDestino();
    	
    	
    	System.out.println(aCuadrantes.get(0).getBeacon());
    	System.out.println(aCuadrantes.get(7).getPosDestino());
    	System.out.println(aCuadrantes.get(38).getPosDestino());
    	//System.out.println(lectorDest.buscarDestino("ascensor_cafe_trasera_p1").getDireccion());
    	System.out.println("Datos cargados ...");
    }
	
     
    @OnMessage
    public String onMessage(String message){
    	String echoMsg="Error";
    	List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));
    	
    	System.out.println("beaconOrigen: " + splittedMessage.get(0));
    	System.out.println("Destino: " + splittedMessage.get(1));
    	
    	
    	beaconOrigen = splittedMessage.get(0);
    	destino = splittedMessage.get(1);
    	
    	
    	//Calculamos los cuadrantes origen y destino
		cuadOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
		cuadDestino = lectorDest.buscarDestino(destino);
		
		System.out.println("Origen: " + cuadOrigen + " Destino: " + cuadDestino);
		
		//Mandamos la lista de cuadrantes:
		String [] cuadRutaStr = calculaRuta(cuadOrigen, cuadDestino);  
		//echoMsg = cuadRutaStr[0] + "|";
		
		//Mandamos la lista de beacons:
		echoMsg = cuadRutaStr[1] + " FINAL" +"|";
		
		//Gerenamos las instrucciones
		gr = new GenerarRuta(lCuadrantes, aPlantas,aCuadrantes);
    	
    	String [] auxGenerar = new String[2];
    	String instrucciones = "";
    	String hayGiro = "";
    	String infoAdicional = "";
    	
    	List<String> cuadRutaLista = Arrays.asList(cuadRutaStr[0].split(Pattern.quote(" ")));
    	
    	//Se recorre la lista de cuadrantes para obtener las instrucciones y si hay giro o no
    	for (String cuadActual : cuadRutaLista) {
    		auxGenerar = gr.generar(Integer.parseInt(cuadActual), cuadDestino);
	    	if(Integer.parseInt(cuadActual) == cuadDestino) {
	    		instrucciones += auxGenerar[0];
	    		hayGiro += auxGenerar[1];
	    		infoAdicional += auxGenerar[2];
        	}
        	else {
        		instrucciones += auxGenerar[0] + "@";
        		hayGiro += auxGenerar[1] + "@";
        		infoAdicional += auxGenerar[2] + "@";
        	}
    	}
    	
    	echoMsg += instrucciones + "|" + hayGiro + "|" + infoAdicional;
    	
        return echoMsg;
    }
    
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
    
    private String[] calculaRuta(int ori, int dest) {
    	
    	String [] resRuta = new String[2]; 	//resRuta[0] = listaCuadrantes
    										//resRuta[1] = listaBeacons
    	resRuta[0] = "";
    	resRuta[1] = "";
    	
    	if (dest != -1) {

			Persona p = new Persona(ori, dest, aCuadrantes);
			lCuadrantes = p.getCamino();
			
			//int [][] m = p.getMatrizAdy();
			//System.out.println("Matriz ady\n" + m.toString());
			
			//System.out.println("Lista Cuadrantes" + lCuadrantes.toString());
			
			for (int i = 0; i < lCuadrantes.size(); i++) {
				if(i!=0) {
					resRuta[0] += " ";
					resRuta[1] += " ";
				}
				resRuta[0] += lCuadrantes.get(i);
				resRuta[1] += ListaCuadrantes.idBeacon(lCuadrantes.get(i), aCuadrantes);
			}
			
			System.out.println("Lista Cuadrantes ruta: " + resRuta[0]);
			System.out.println("Lista Beacons ruta: " + resRuta[1]);
    	}
    	return resRuta;
    }
	
}
