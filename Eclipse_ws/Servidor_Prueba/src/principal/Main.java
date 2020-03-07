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
import routes.Posicion;
import usuario.Persona;
import xml.Edificio;

/**
 * 
 * Método principal del servidor. En este método se establecerá conexión y se
 * esperará a recibir la petición de la aplicación móvil. Los datos de los xml
 * se cargarán nada más lanzar el servidor. Cuando se reciba la petición, se
 * leerán los datos en la variable "in" de tipo DataInputStream por este orden:
 * -Origen -Destino -Ángulo de rotación -Posición actual
 * 
 * Obtendremos el camino a recorrer a través de la clase Persona, generando a
 * continuación la ruta que será enviada al dispositivo móvil.
 * 
 * 26/05/2014 - Revisado y limpiado
 * 
 */
public class Main {

	private static ArrayList<Integer> lCuadrantes = new ArrayList<Integer>();
	private static ArrayList<Estancia> aEstancias;
	private static ArrayList<Cuadrante> aCuadrantes;
	private static Edificio edificio;

	public static void main(String[] args) {
		
		
		
		/*int PORT = 2222;
        ServerSocket serverSocket = null;
        

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException var25) {
            System.err.println("Error al conectar con el server");
        }

        while(true) {
            while(true) {
                try {
                    System.out.println("Esperando cliente ... ");
                    Socket socket = serverSocket.accept();
                    System.out.println("Conexión establecida");
                    serverSocket.setReuseAddress(true);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    
                    String msgCliente = in.readUTF();
                    System.out.println("Dice el cliente: " + msgCliente);
                    
                    out.writeUTF("Hola yo soy el servidor");

                    out.close();
                    in.close();
                    socket.close();
                    
                } catch (NullPointerException var26) {
                    var26.printStackTrace();
                } catch (IOException var27) {
                    System.err.println(" Error en IO \n" + var27);
                    var27.printStackTrace();
                }
            }
        }*/
		
		
		
		
		
	
		
		
		
		//*************************************************************************

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
		
		
		// PARA DEBUG
		// for (int i = 1; i > 16; i--)
		/*
		 * lCuadrantes.add(0); lCuadrantes.add(1); lCuadrantes.add(2);
		 * lCuadrantes.add(16); lCuadrantes.add(18);
		 */

		while (true) {
			try {

				System.out.println("Esperando cliente ... ");

				// Abrimos entradas y salidas de sockets para la conexión con la
				// App. Android
				Socket socket = serverSocket.accept();
				System.out.println("Conexión establecida");
				serverSocket.setReuseAddress(true);

				DataInputStream in = new DataInputStream(
						socket.getInputStream());

				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());

				/** FUNCIONA **/
				String origenX = in.readUTF();
				String origenY = in.readUTF();
				String origenZ = in.readUTF();
				Posicion pO = new Posicion(Double.parseDouble(origenX),Double.parseDouble(origenY));

				int posOrigen = ListaCuadrantes.numCuadrante(pO,Double.parseDouble(origenZ), aCuadrantes);
				
				
				// String origen = in.readUTF();
				// int posOrigen = Integer.parseInt(origen);
				System.out.println("origenX: " + origenX);
				System.out.println("origenY: " + origenY);
				System.out.println("origenZ: " + origenZ);
				System.out.println("PosOrigen: " + posOrigen);

				String destino = in.readUTF();
				int posDestino = lectorDest.buscarDestino(destino);
				
				System.out.println("PosDestino: " + posDestino);
				//System.out.println(posDestino);

				//String angOrientacion = in.readUTF();
				//float angOr = Float.parseFloat(angOrientacion);

				/** FUNCIONA **/
				String actualX = in.readUTF();
				String actualY = in.readUTF();
				String actualZ = in.readUTF();

				Posicion pA = new Posicion(Double.parseDouble(actualX),Double.parseDouble(actualY));

				int posAct = ListaCuadrantes.numCuadrante(pA,Double.parseDouble(actualZ), aCuadrantes);


				// String actual =in.readUTF();
				// int posAct = Integer.parseInt(actual);
				System.out.println("actualX: " + actualX);
				System.out.println("actualY: " + actualY);
				System.out.println("actualZ: " + actualZ);
				System.out.println("PosActual: " + posAct);

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
					String ruta = gr.generar(posAct, posDestino);
					out.writeUTF(ruta);
					out.writeInt(gr.getCuadranteClave());

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