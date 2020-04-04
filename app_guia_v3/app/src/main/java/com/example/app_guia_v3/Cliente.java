package com.example.app_guia_v3;

import android.os.Handler;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Cliente {

    public static final String TAG = "Client_socket";

    String addressServer ="192.168.1.38"; //"147.96.96.209";//"172.20.10.6";//"192.168.1.43";//"192.168.1.34"; //"147.96.102.38";// cambiar a tu IP

    private int PORT = 2222;
    private Socket socket = null;

    private String dest, b_mas_cerca, origen;
    //String listaCuadrantes, ruta, beaconClave;
    private int cuadranteClave;
    private boolean verbose;

    final String [] results = new  String[3];

    public Cliente(String destino, String beaconMasCercano, String ori, boolean verb){
        dest = destino;
        b_mas_cerca = beaconMasCercano;
        origen = ori;
        verbose = verb;
    }

    protected String [] socketConnect() {
        Log.i(TAG, " ***********En socketConnect: \n");

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream in = null;
                DataOutputStream out = null;

                try {
                    Log.i(TAG, "!!!!!! Antes de llamar a Socket: \n");
                    socket = new Socket();
                    SocketAddress adr = new InetSocketAddress(addressServer, PORT);
                    socket.connect(adr, 1500);

                    //System.out.println("Ha conectado");

                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    out.flush();

                    //Mandamos al servidor el beacon origen
                    out.writeUTF(origen);

                    //Mandamos al servidor el destino
                    out.writeUTF(dest);

                    //Mandamos al servidor nuestro beacon más cercano actual
                    out.writeUTF(b_mas_cerca);

                    //Mandamos el modo verbose a true o false;
                    out.writeBoolean(verbose);

                    //Leemos del servidor la ruta de cuadrantes que tenemos que hacer
                    String listaCuadrantes = in.readUTF();

                    //Leemos del servidor la instrucción
                    String ruta = in.readUTF();

                    //Leemos del servidor el cuadrante clave
                    cuadranteClave = in.readInt();

                    //Leemos del servidor el beacon clave
                    String beaconClave = in.readUTF();
                    Log.i(TAG, "ruta: " + ruta + " \n");
                    Log.i(TAG, "beacon clave: " + beaconClave + " \n");

                    results[0] = listaCuadrantes;
                    results[1] = ruta;
                    results[2] = beaconClave;

                    Log.i(TAG, "ruta: en el thread... " + results[1] + " \n");

                    in.close();
                    out.close();
                    socket.close();

                } catch (SocketTimeoutException e) {
                    //System.err.println(" Error al conectar: \n" + e);
                    Log.i(TAG, " Error al conectar: \n" + e);

                } catch (UnknownHostException e) {
                    //System.err.println(" Servidor inaccesible \n" + e);
                    //System.exit(1);
                    Log.i(TAG, " Servidor inaccesible \n" + e);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    Log.i(TAG, " IOExc \n" + e);
                }

            }
        });
        thread.start();
        Log.i(TAG, " ***********En socketConnect al final: \n");
        Log.i(TAG, "ruta: fuera del thread... " + results[1] + " \n");



        try{thread.join();}
        catch (InterruptedException e) {
        // TODO Auto-generated catch block
        //e.printStackTrace();
        Log.i(TAG, " IOExc \n" + e);
    }
        return results;
    }

    public int getCuadranteClave() {
        return cuadranteClave;
    }
    /*public String getListaCuadrantes() {
        return listaCuadrantes;
    }
    public String getRuta(){
        return ruta;
    }
    public String getBeaconClave(){
        return beaconClave;
    }
    public int cuadranteClave() {
        return cuadranteClave;
    }*/
}
