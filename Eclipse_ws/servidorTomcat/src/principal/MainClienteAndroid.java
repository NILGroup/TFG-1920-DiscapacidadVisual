package principal;


import java.util.ArrayList;

import routes.Cuadrante;
import routes.Estancia;
import routes.GenerarRuta;
import routes.LectorDestino;
import routes.ListaCuadrantes;
import usuario.Persona;
import xml.Edificio;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
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
	Map<String, Session> sessions = new ConcurrentHashMap<>();

	
	private static ArrayList<Integer> lCuadrantes = new ArrayList<Integer>();
	private static ArrayList<Estancia> aEstancias; //todas las estancias del edificio
	private static ArrayList<Cuadrante> aCuadrantes; //todos los cuadrantes del edificio
	private static Edificio edificio;
	private String beaconOrigen = "no", destino = "no", beaconActual = "no";
	private LectorDestino lectorDest = null;
	private int cuadOrigen, cuadDestino;
	private GenerarRuta gr;
	

	@OnOpen
    public void onOpen(){
        System.out.println("Open Connection ...");
        
        //Cargamos la estructura del edificio
        edificio = new Edificio();
    	aEstancias = edificio.getEstancias();
    	aCuadrantes = edificio.getCuadrantes();
    	
    	//Cargamos los destinos
    	lectorDest = new LectorDestino();
    	System.out.println("El cuad del aula 7:" + lectorDest.buscarDestino("aula 7"));
    }
	
     
    @OnMessage
    public String onMessage(String message){
    	String echoMsg="Error";
    	List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));
    	
    	System.out.println("beaconOrigen: " + splittedMessage.get(0));
    	System.out.println("beaconDestino: " + splittedMessage.get(1));
    	System.out.println("beaconActual: " + splittedMessage.get(2));
    	
    	beaconOrigen = splittedMessage.get(0);
    	destino = splittedMessage.get(1);
    	beaconActual = splittedMessage.get(2);
    	
    	//Calculamos los cuadrantes origen y destino
		cuadOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
		cuadDestino = lectorDest.buscarDestino(destino);
		
		System.out.println("Origen: " + cuadOrigen + " Destino: " + cuadDestino);
		echoMsg = calculaRuta(cuadOrigen, cuadDestino) + "|";
		
		//Gerenamos las instrucciones
		gr = new GenerarRuta(lCuadrantes, aEstancias,aCuadrantes);
    	int cuadActual = ListaCuadrantes.numCuadrante(beaconActual, aCuadrantes);
    	
    	echoMsg = echoMsg + gr.generar(cuadActual, cuadDestino,true) + "|";
    	
    	//Escribir beacon clave
    	if(cuadActual == cuadDestino) {
    		echoMsg = echoMsg + "FINAL";
    	}
    	else {
    		echoMsg = echoMsg + ListaCuadrantes.idBeacon(gr.getCuadranteClave(), aCuadrantes);
    	}
    	
    	  /*switch (splittedMessage.get(0)) {
    	    case "origen:":
    	    	beaconOrigen = splittedMessage.get(1);
    	    	System.out.println("beaconOrigen: " + beaconOrigen);
    	    	echoMsg = splittedMessage.get(1);
    	      break;
    	    case "destino:":
    	    	destino = splittedMessage.get(1);
    	    	System.out.println("Destino: " + destino);
    	    	echoMsg = splittedMessage.get(1);
    	    	break;
    	    case "actual:":
	    	    beaconActual = splittedMessage.get(1);
	    	    System.out.println("beaconActual: " + beaconActual);
		    	echoMsg = splittedMessage.get(1);
		    	break;
    	    case "listaCuad:":
    	    	//Calculamos los cuadrantes origen y destino
	    		cuadOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
	    		cuadDestino = lectorDest.buscarDestino(destino);
	    		
	    		System.out.println("Origen: " + cuadOrigen + " Destino: " + cuadDestino);
	    		echoMsg = "listaCuad:|" + calculaRuta(cuadOrigen, cuadDestino);
	    		break;
    	    case "instruccion:":
    	    	gr = new GenerarRuta(lCuadrantes, aEstancias,aCuadrantes);
    	    	int cuadActual = ListaCuadrantes.numCuadrante(beaconActual, aCuadrantes);
    	    	
    	    	//Pedir al cliente el modo verbose
    	    	/*if(cuadActual == cuadDestino) {
    	    		echoMsg = "instruccionFinal:|" + gr.generar(cuadActual, cuadDestino,true);
    	    	}
    	    	else {
    	    		echoMsg = "instruccion:|" + gr.generar(cuadActual, cuadDestino,true);
    	    	}//cerrar comentario
    	    	echoMsg = "instruccion:|" + gr.generar(cuadActual, cuadDestino,true);
				break;
    	    case "beaconClave:":
    	    	//escribir beacon clave
				echoMsg = "beaconClave:|" + ListaCuadrantes.idBeacon(gr.getCuadranteClave(), aCuadrantes);
    	    	break;
    	  }*/
       
        return echoMsg;
    }
    
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
    
    private String calculaRuta(int ori, int dest) {
    	String lista = "";
    	
    	if (dest != -1) {

			Persona p = new Persona(ori, dest, aCuadrantes);
			lCuadrantes = p.getCamino();
			
			int [][] m = p.getMatrizAdy();
			System.out.println("Matriz ady\n" + m.toString());
			
			System.out.println("Lista Cuadrantes" + lCuadrantes.toString());
			
			for (int i = 0; i < lCuadrantes.size(); i++) {
				lista += " ";
				lista += lCuadrantes.get(i);
			}
			
			System.out.println("Lista Cuadrantes ruta: " + lista);
    	}
    	return lista;
    }
	
//-------------------------------------------------------------------------------------
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int PORT = 2222;
		ServerSocket serverSocket = null;
		LectorDestino lectorDest = new LectorDestino();

		try {
			serverSocket = new ServerSocket(PORT);

		} catch (IOException ioe) {
			System.err.println("Error al conectar con el server");
		}

		edificio = new Edificio();
		aEstancias = edificio.getEstancias();
		aCuadrantes = edificio.getCuadrantes();
		
	

		while (true) {
			try {

				System.out.println("Esperando cliente android... ");

				// Abrimos entradas y salidas de sockets para la conexión con la
				// App. Android
				Socket socket = serverSocket.accept();
				System.out.println("Conexión establecida");
				serverSocket.setReuseAddress(true);

				DataInputStream in = new DataInputStream(
						socket.getInputStream());

				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());

			
				String beaconOrigen = in.readUTF();
				
				//Acceder al cuadrante de ese beacon origen
				//int posOrigen = ListaCuadrantes.numCuadrante(pO,Double.parseDouble(origenZ), aCuadrantes);
				
				int posOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
				
				System.out.println("Beacon origen: " + beaconOrigen);
				System.out.println("Cuadrante Origen: " + posOrigen);

				String destino = in.readUTF();
				int posDestino = lectorDest.buscarDestino(destino);
				
				System.out.println("Cuadrante Destino: " + posDestino);
				
				System.out.println(posDestino);

				String beaconActual = in.readUTF();

				int posAct = ListaCuadrantes.numCuadrante(beaconActual, aCuadrantes);

				System.out.println("Beacon Actual: " + beaconActual);
				System.out.println("Cuadrante Actual: " + posAct);

				if (posDestino != -1) {

					Persona p = new Persona(posOrigen, posDestino, aCuadrantes);

					lCuadrantes = p.getCamino();
					
					int [][] m = p.getMatrizAdy();
					System.out.println("Matriz ady\n" + m.toString());
					
					System.out.println("Lista Cuadrantes" + lCuadrantes.toString());

					String lista = "";
					for (int i = 0; i < lCuadrantes.size(); i++) {
						lista += " ";
						lista += lCuadrantes.get(i);
					}
					out.writeUTF(lista);

					GenerarRuta gr = new GenerarRuta(lCuadrantes, aEstancias,aCuadrantes);
					
					//Pedir al cliente el modo verbose
					String ruta = gr.generar(posAct, posDestino,true);
					out.writeUTF(ruta);
					out.writeInt(gr.getCuadranteClave());
					//escribir beacon clave
					out.writeUTF(ListaCuadrantes.idBeacon(gr.getCuadranteClave(), aCuadrantes));

				} else {
					out.writeInt(2);
					out.writeUTF("Destino inexistente, no ha sido posible calcular la ruta");
					System.out.print("Destino inexistente, no ha sido posible calcular la ruta");

				}

				// Cerramos la conexión
				out.close();
				in.close();
				socket.close();

			} catch (java.lang.NullPointerException npe) {
				npe.printStackTrace();
			} catch (IOException e) {
				System.err.println(" Error en IO \n" + e);
				e.printStackTrace();
			}
		}
	}*/
	//------------------------------------------------------------------
}
