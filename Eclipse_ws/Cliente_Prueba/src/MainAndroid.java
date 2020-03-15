import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;



public class MainAndroid extends Thread {

	String addressServer = "192.168.1.37";//"192.168.1.38"; 

    int PORT = 2222;
    Socket socket = null;
    String msg = "Hola servidor\n";
    
    static String beaconOrigen = "Wsxj";
	
    static String destino = "aula x";
	
    static String beaconActual = "Wsxj";
	
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

               out = new DataOutputStream(socket.getOutputStream());
               in = new DataInputStream(socket.getInputStream());
               out.flush();

               out.writeUTF(beaconOrigen);
  			     			   
  			   out.writeUTF(destino);
  			 
  			   out.writeUTF(beaconActual);
  			 
  			   
  			   listaCuadrantes=in.readUTF();
  			   ruta  = in.readUTF();
  			   cuadranteClave  = in.readInt();
  			   
  			   System.out.println("-------------------------------------------");
  			   System.out.println("Estoy en el beacon: " + beaconActual);
  			   System.out.println("Lista de cuadrantes de la ruta: " + listaCuadrantes);
  			   System.out.println("Ruta: " + ruta);
  			   System.out.println("Cuadrante clave: " + cuadranteClave);
  			   System.out.println("-------------------------------------------");
  			   
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
		MainAndroid thread = new MainAndroid();
	    
	    
	    //Inicio en el cuadrante 24, destino 54
		beaconOrigen = "beacon24";
		destino = "cuad 54";
		beaconActual = "beacon24";
		
	    thread.start();
		
	  //Suponemos que hemos llegado al cuadrante 32
	    try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon32";
		
	    MainAndroid thread2 = new MainAndroid();
		thread2.start();
		
		//Suponemos que hemos llegado al cuadrante 34
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon34";
		
	    MainAndroid thread3 = new MainAndroid();
		thread3.start();
		
		
		//Suponemos que hemos llegado al cuadrante 35
	    try {
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon35";
		
	    MainAndroid thread4 = new MainAndroid();
		thread4.start();
		
		//Suponemos que hemos llegado al cuadrante 36
	    try {
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon36";
		
	    MainAndroid thread5 = new MainAndroid();
		thread5.start();
		
		//Suponemos que hemos llegado al cuadrante 67
	    try {
			TimeUnit.SECONDS.sleep(9);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon67";
		
	    MainAndroid thread8 = new MainAndroid();
		thread8.start();
		
		//Suponemos que hemos llegado al cuadrante 68
	    try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon68";
		
	    MainAndroid thread6 = new MainAndroid();
		thread6.start();
		
		
		//Suponemos que hemos llegado al cuadrante 55
	    try {
			TimeUnit.SECONDS.sleep(12);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon55";
		
	    MainAndroid thread7 = new MainAndroid();
		thread7.start();
		
		//Suponemos que hemos llegado al cuadrante 54
	    try {
			TimeUnit.SECONDS.sleep(12);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "beacon54";
		
	    MainAndroid thread9 = new MainAndroid();
		thread9.start();
		
		

	}

}


