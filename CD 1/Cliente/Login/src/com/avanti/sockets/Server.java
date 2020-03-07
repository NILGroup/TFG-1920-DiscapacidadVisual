package com.avanti.sockets;

import java.lang.Runnable;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * <p>Title: Server.java </p>
 *
 * <p>Description: Clase que recibe la petici�n del servidor. </p>
 *
 *
 * @author Enrique Lopez Manas
 * @version 1.0
 * @see Client.java
 */

public class Server implements Runnable{
    public static final String SERVERIP = "127.0.0.1";
    public static final int SERVERPORT = 4444;
         
   // @Override
    public void run() {
         try {
              InetAddress serverAddr = InetAddress.getByName(SERVERIP);
              DatagramSocket socket = new DatagramSocket(SERVERPORT,serverAddr);
              byte[] buf = new byte[17];
              DatagramPacket packet = new DatagramPacket(buf, buf.length);
              socket.receive(packet);
             } catch (Exception e) {
            
         }
    }
}