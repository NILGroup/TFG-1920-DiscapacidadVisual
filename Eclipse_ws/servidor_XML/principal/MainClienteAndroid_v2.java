package principal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class MainClienteAndroid_v2 {

	private static ArrayList<Integer> lCuadrantes = new ArrayList<Integer>();
	private static ArrayList<Estancia> aEstancias; //todas las estancias del edificio
	private static ArrayList<Cuadrante> aCuadrantes; //todos los cuadrantes del edificio
	private static Edificio edificio;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int PORT = 2222;
		ServerSocket serverSocket = null;
		LectorDestino lectorDest = new LectorDestino();

		try {
			serverSocket = new ServerSocket(PORT);

		} catch (IOException ioe) {
			System.err.println("Error al conectar con el server");
		}
		//Leemos la estructura del edificio
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

				//Leemos el beaconOrigen
				String beaconOrigen = in.readUTF();
				
				//Acceder al cuadrante de ese beacon origen
				int posOrigen = ListaCuadrantes.numCuadrante(beaconOrigen, aCuadrantes);
				
				System.out.println("Beacon origen: " + beaconOrigen);
				System.out.println("Cuadrante Origen: " + posOrigen);

				String destino = in.readUTF();
				int posDestino = lectorDest.buscarDestino(destino);
				
				System.out.println("Cuadrante Destino: " + posDestino);
				
				System.out.println(posDestino);

				//Ya no se lee beacon actual
				
				if (posDestino != -1) {

					Persona p = new Persona(posOrigen, posDestino, aCuadrantes);

					lCuadrantes = p.getCamino();
					
					int [][] m = p.getMatrizAdy();
					
					System.out.println("Lista Cuadrantes" + lCuadrantes.toString());

					String lista = "";
					for (int i = 0; i < lCuadrantes.size(); i++) {
						lista += " ";
						lista += lCuadrantes.get(i);
					}
					//Mandamos al cliente la ruta de cuadrantes que debe seguir
					out.writeUTF(lista);

					GenerarRuta gr = new GenerarRuta(lCuadrantes, aEstancias,aCuadrantes);
					
					//Pedir al cliente el modo verbose
					//Gereramos las instrucciones completas desde origen al destino
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
	}


}
