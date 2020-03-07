
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Main_thread extends Thread{
	
	String addressServer ="192.168.1.38"; 

    int PORT = 2222;
    Socket socket = null;
    String msg = "Hola servidor\n";
    
    static String origenX = "1";
    static String origenY = "15";
    static String origenZ = "1";
	
    static String destino = "aula 13";
	
    static String posActualX = "1";
    static String posActualY = "15";
    static String posActualZ = "1";
	
	private String ruta;
	private String listaCuadrantes;
	private int cuadranteClave;
	
	
	   public void run() {
           DataInputStream in = null;
           DataOutputStream out = null;

           try {
               
               socket = new Socket();
               SocketAddress adr = new InetSocketAddress(addressServer, PORT);
               socket.connect(adr, 1500);

               //System.out.println("Ha conectado");

               out = new DataOutputStream(socket.getOutputStream());
               in = new DataInputStream(socket.getInputStream());
               out.flush();

               //out.writeUTF(msg);
               
               
               out.writeUTF(origenX);
  			   out.writeUTF(origenY);
  			   out.writeUTF(origenZ);
  			   
  			   out.writeUTF(destino);
  			 
  			   out.writeUTF(posActualX);
  			   out.writeUTF(posActualY);
  			   out.writeUTF(posActualZ);

               //String msgServ  = in.readUTF();
               //System.out.println("El servidor dice: " + msgServ);
  			   
  			   
  			   listaCuadrantes=in.readUTF();
  			   ruta  = in.readUTF();
  			   cuadranteClave  = in.readInt();
  			   
  			   System.out.println("Lista de cuadrantes de la ruta: " + listaCuadrantes);
  			   System.out.println("Ruta: " + ruta);
  			   System.out.println("Cuadrante clave: " + cuadranteClave);

               in.close();
               out.close();
               socket.close();

           } catch (SocketTimeoutException e) {
               System.err.println(" Error al conectar: \n" + e);
           
           } catch (UnknownHostException e) {
               System.err.println(" Servidor inaccesible \n" + e);
               System.exit(1);
               
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }


       }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    /*Main_thread thread = new Main_thread();
	    
	    
	    //Inicio en el cuadrante 0. Aula 6
	    origenX = "1";
		origenY = "15";
		origenZ = "1";
		
		destino = "aula 13";
		
		posActualX = "1";
		posActualY = "15";
		posActualZ = "1";
	    
	    thread.start();
	    
	  //Suponemos que hemos llegado al cuadrante 10
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    posActualX = "32";
		posActualY = "15";
		posActualZ = "1";
		
		Main_thread thread2 = new Main_thread();
		thread2.start();
		
		//Suponemos que hemos llegado al cuadrante 15, aula 13
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		posActualX = "47";
		posActualY = "15";
		posActualZ = "1";
	
		Main_thread thread3 = new Main_thread();
		thread3.start();*/
		
		//Vamos del cuadrante 15 al 19, no va bien
		
		/*Main_thread thread = new Main_thread();
	    
	    
	    //Inicio en el cuadrante 15. Aula 13
	    origenX = "47";
		origenY = "15";
		origenZ = "1";
		
		destino = "aula x"; //está asi en destinos.json
		
		posActualX = "47";
		posActualY = "15";
		posActualZ = "1";
	    
	    thread.start();
	    
	  //Suponemos que hemos llegado al cuadrante 10
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    //posActualX = "32";
		//posActualY = "15";
		//posActualZ = "1";
		
	    //cuadrante 1
	    posActualX = "5";
	  	posActualY = "15";
	    posActualZ = "1";
	  		
	    
		Main_thread thread2 = new Main_thread();
		thread2.start();
		
		//Suponemos que hemos llegado al cuadrante 9
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		posActualX = "28";
		posActualY = "15";
		posActualZ = "1";
	
		Main_thread thread3 = new Main_thread();
		thread3.start();*/
		
		
		
		
		
		//Vamos del 0 al 19
		
		Main_thread thread = new Main_thread();
	    
	    
	    //Inicio en el cuadrante 15. Aula 6
	    origenX = "47";
		origenY = "15";
		origenZ = "1";
		
		destino = "aula x";
		
		posActualX = "47";
		posActualY = "15";
		posActualZ = "1";
	    
	    thread.start();
	    
	  //Suponemos que hemos llegado al cuadrante 7
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    posActualX = "22";
		posActualY = "15";
		posActualZ = "1";
		
		Main_thread thread2 = new Main_thread();
		thread2.start();
		
		//Suponemos que hemos llegado al cuadrante 17
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		posActualX = "10";
		posActualY = "17";
		posActualZ = "1";
	
		Main_thread thread3 = new Main_thread();
		thread3.start();
		
	    
	}



}
