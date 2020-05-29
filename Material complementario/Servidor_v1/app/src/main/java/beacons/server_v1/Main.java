package beacons.server_v1;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import beacons.server_v1.routes.Estancia;
//import routes.GenerarRuta;
//import routes.LectorDestino;
//import routes.ListaCuadrantes;
//import routes.Posicion;
//import usuario.Persona;
import beacons.server_v1.xml.Edificio;
import beacons.server_v1.xml.CargaXML;
import beacons.server_v1.routes.Cuadrante;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ArrayList<Integer> lCuadrantes = new ArrayList();
    //private static ArrayList<Estancia> aEstancias;
    //private static ArrayList<Cuadrante> aCuadrantes;
    private static Edificio edificio;

    public Main() {
    }

    public static void main(String[] args) {

        CargaXML carga;
        SAXBuilder builder = new SAXBuilder();
	    ArrayList<Estancia> aEstancias = new ArrayList<Estancia>();
	    ArrayList<Cuadrante> aCuadrantes= new ArrayList<Cuadrante>();

	    try
        {
            File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\edificio.xml");
            Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
            Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
            List lista_plantas = rootNode.getChildren( "planta" );//Se obtiene la lista de hijos de la raiz 'edificio'

            for ( int indexPlanta = 0; indexPlanta < lista_plantas.size(); indexPlanta++ )//Se recorre la lista de 'plantas'
            {
                Element planta = (Element) lista_plantas.get(indexPlanta);//Se obtiene el elemento 'planta'
                String nombre = planta.getAttributeValue("nombre");//Se obtiene el atribullo 'idp'


                String archivo = planta.getChildText("archivo");
                if (!archivo.equals("nada")){
                    carga= new CargaXML(archivo);
                    System.out.println("Archivo "+ archivo );
                    aEstancias.addAll(carga.getEstancias());
                    aCuadrantes.addAll(carga.getCuadrantes());
                }
                for(int i=0; i < aCuadrantes.size(); i++) {
                	System.out.println("Cuadrante " + aCuadrantes.get(i).getID());
                	System.out.println("Conectado " + aCuadrantes.get(i).conectado[0]);
                	System.out.println("Beacon immediate " + aCuadrantes.get(i).beacons[0]);
                }
            }
        }catch ( IOException io ) {
            System.out.println( io.getMessage() );
        }catch ( JDOMException jdomex ) {
            System.out.println( jdomex.getMessage() );
        }




        /*int PORT = 22;
        ServerSocket serverSocket = null;
        LectorDestino lectorDest = new LectorDestino();

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException var25) {
            System.err.println("Error al conectar con el server");
        }

        edificio = new Edificio();
        aEstancias = edificio.getEstancias();
        aCuadrantes = edificio.getCuadrantes();

        while(true) {
            while(true) {
                try {
                    System.out.println("Esperando cliente ... ");
                    Socket socket = serverSocket.accept();
                    System.out.println("Conexión establecida");
                    serverSocket.setReuseAddress(true);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String origenX = in.readUTF();
                    String origenY = in.readUTF();
                    String origenZ = in.readUTF();
                    Posicion pO = new Posicion(Double.parseDouble(origenX), Double.parseDouble(origenY));
                    int posOrigen = ListaCuadrantes.numCuadrante(pO, Double.parseDouble(origenZ), aCuadrantes);
                    System.out.println("origenX");
                    System.out.println(origenX);
                    System.out.println("origenY");
                    System.out.println(origenY);
                    System.out.println("origenZ");
                    System.out.println(origenZ);
                    System.out.println(posOrigen);
                    String destino = in.readUTF();
                    int posDestino = lectorDest.buscarDestino(destino);
                    String angOrientacion = in.readUTF();
                    float angOr = Float.parseFloat(angOrientacion);
                    String actualX = in.readUTF();
                    String actualY = in.readUTF();
                    String actualZ = in.readUTF();
                    Posicion pA = new Posicion(Double.parseDouble(actualX), Double.parseDouble(actualY));
                    int posAct = ListaCuadrantes.numCuadrante(pA, Double.parseDouble(actualZ), aCuadrantes);
                    System.out.println("actualX");
                    System.out.println(actualX);
                    System.out.println("actualY");
                    System.out.println(actualY);
                    System.out.println("actualZ");
                    System.out.println(actualZ);
                    System.out.println(posAct);
                    if (posDestino == -1) {
                        out.writeInt(2);
                        out.writeUTF("Destino inexistente, no ha sido posible calcular la ruta");
                        System.out.print("Destino inexistente, no ha sido posible calcular la ruta");
                    } else {
                        Persona p = new Persona(posOrigen, posDestino, aCuadrantes);
                        lCuadrantes = p.getCamino();
                        String lista = "";

                        for(int i = 0; i < lCuadrantes.size(); ++i) {
                            lista = lista + " ";
                            lista = lista + lCuadrantes.get(i);
                        }

                        out.writeUTF(lista);
                        GenerarRuta gr = new GenerarRuta(lCuadrantes, aEstancias, aCuadrantes);
                        String ruta = gr.generar(posAct, posDestino);
                        out.writeUTF(ruta);
                        out.writeInt(gr.getCuadranteClave());
                    }

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
    }
}