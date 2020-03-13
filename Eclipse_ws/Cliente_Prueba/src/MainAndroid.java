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

	String addressServer = "192.168.1.38";//"192.168.1.38"; 

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
		
		//Simulamos la ruta que va del 15 al 19
		
		/*MainAndroid thread = new MainAndroid();
	    
	    
	    //Inicio en el cuadrante 15
		beaconOrigen = "Wsxj";
		destino = "aula x";
		beaconActual = "Wsxj";
		
	    thread.start();
	    
	  //Suponemos que hemos llegado al cuadrante 7
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "CPne";
		
	    MainAndroid thread2 = new MainAndroid();
		thread2.start();
		
		
		//Suponemos que hemos llegado al cuadrante 3
		try {
			TimeUnit.SECONDS.sleep(7);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		beaconActual = "cmSc";
	
		MainAndroid thread3 = new MainAndroid();
		thread3.start();
		
		
		//Suponemos que hemos llegado al cuadrante 17
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Cuadrante 17
		
		beaconActual = "z3bD";
	
		MainAndroid thread5 = new MainAndroid();
		thread5.start();
		
		
		//Suponemos que hemos llegado al cuadrante 19
		try {
			TimeUnit.SECONDS.sleep(13);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beaconActual = "7rd7";
	
		MainAndroid thread6 = new MainAndroid();
		thread6.start();
		*/
		
		
		//Simulamos la ruta que va del 19 al 36
		MainAndroid thread = new MainAndroid();
	    
	    
	    //Inicio en el cuadrante 19
		beaconOrigen = "7rd7";
		destino = "ascensor 1";
		beaconActual = "7rd7";
		
	    thread.start();
	        
	  //Suponemos que hemos llegado al cuadrante 17
  		try {
  			TimeUnit.SECONDS.sleep(10);
  		} catch (InterruptedException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		
  		//Cuadrante 17
  		
  		beaconActual = "z3bD";
  	
  		MainAndroid thread5 = new MainAndroid();
  		thread5.start();
	    
  		
		//Suponemos que hemos llegado al cuadrante 3
		try {
			TimeUnit.SECONDS.sleep(7);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		beaconActual = "cmSc";
	
		MainAndroid thread3 = new MainAndroid();
		thread3.start();
  		
	    
	  //Suponemos que hemos llegado al cuadrante 7
	    try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    beaconActual = "CPne";
		
	    MainAndroid thread2 = new MainAndroid();
		thread2.start();
		
	
		//Suponemos que hemos llegado al cuadrante 15
		try {
			TimeUnit.SECONDS.sleep(13);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beaconActual = "Wsxj";
	
		MainAndroid thread6 = new MainAndroid();
		thread6.start();
		
		
		//Suponemos que hemos llegado al cuadrante 35
		try {
			TimeUnit.SECONDS.sleep(16);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beaconActual = "beacon35";
	
		MainAndroid thread7 = new MainAndroid();
		thread7.start();

		//Suponemos que hemos llegado al cuadrante 36
		try {
			TimeUnit.SECONDS.sleep(19);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beaconActual = "beacon36";
	
		MainAndroid thread8 = new MainAndroid();
		thread8.start();
		
		

	}

}


