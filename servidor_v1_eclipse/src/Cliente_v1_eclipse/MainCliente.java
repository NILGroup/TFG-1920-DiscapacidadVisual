package Cliente_v1_eclipse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

public class MainCliente {

	
	static String addressServer ="192.168.1.38"; //"147.96.96.209";//"172.20.10.6";//"192.168.1.43";//"192.168.1.34"; //"147.96.102.38";//

    static int PORT = 2222;
    static Socket socket = null;
    static String msg = "Hola servidor\n";
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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

            out.writeUTF(msg);
            

            String msgServ  = in.readUTF();
            
            System.out.println(msgServ);

            in.close();
            out.close();
            socket.close();

        } catch (SocketTimeoutException e) {
            System.err.println(" Error al conectar: \n" + e);
            //Log.i(TAG, " Error al conectar: \n" + e);

        } catch (UnknownHostException e) {
            System.err.println(" Servidor inaccesible \n" + e);
            System.exit(1);
            //Log.i(TAG, " Servidor inaccesible \n" + e);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.i(TAG, " IOExc \n" + e);
        }

	}

}
