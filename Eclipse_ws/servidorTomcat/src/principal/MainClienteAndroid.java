package principal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
 * En esta clase nos encargamos de la conexi�n directa con el cliente. Constituye el servidor de 
 * la aplicaci�n, queda a la espera de clientes y cuando uno llega le atiende leyendo el origen de
 * su ruta, el destino al que quiere ir y su beacon m�s cercano. A partir de esa informaci�n genera 
 * la siguiente instrucci�n del cliente y vuelve a quedar a la espera de que el cliente vuelva a 
 * conectar una vez haya actualizado su posici�n actual.
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
	private String beaconOrigen = "no", destino = "no";
	private LectorDestino lectorDest = null;
	
	

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
    	
    	  switch (splittedMessage.get(0)) {
    	    case "origen:":
    	    	beaconOrigen = splittedMessage.get(1);
    	    	echoMsg = splittedMessage.get(1) + " de parte del servidor1";
    	      break;
    	    case "destino:":
    	    	destino = splittedMessage.get(1);
    	    	echoMsg = splittedMessage.get(1) + " de parte del servidor2";
    	    	if(!beaconOrigen.equals("no") && !destino.equals("no")) {//Ya hay origen y destino:
    	    		
    	    		//Calculamos los cuadrantes origen y destino
    	    		int cuadOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
    	    		int cuadDestino = lectorDest.buscarDestino(destino);
    	    		
    	    		System.out.println("Origen: " + cuadOrigen + " Destino: " + cuadDestino);
    	    		System.out.println("El cuad del aula 7, otra vez:" + lectorDest.buscarDestino("aula 7"));
    	    		echoMsg = calculaRuta(cuadOrigen, cuadDestino);
    	    		
    	    		beaconOrigen="no"; destino="no";
    	    	}
    	      break;
    	  }
       
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
			//out.writeUTF(lista);
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

				// Abrimos entradas y salidas de sockets para la conexi�n con la
				// App. Android
				Socket socket = serverSocket.accept();
				System.out.println("Conexi�n establecida");
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

				// Cerramos la conexi�n
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
