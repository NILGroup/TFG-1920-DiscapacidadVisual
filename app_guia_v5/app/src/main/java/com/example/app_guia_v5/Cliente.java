package com.example.app_guia_v5;

import android.util.Log;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;


public class Cliente {

    private WebSocketClient webSocketClient;


    private String dest, ori;
    String infoRecibida = "no";
    //String listaCuadrantes, intrucciones, beaconsClave = "NO", hayGiros;
    final String [] results = new  String[5];

    public Cliente(String destino, String origen){
        dest = destino;
        ori = origen;
    }

    protected String[] createWebSocketClient() {
        final URI uri;

        try {
            // Connect to local host
            uri = new URI("ws://192.168.1.38:8080/servidorTomcat/websocketendpoint");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        Log.i("WebSocket", "Antes de webSocket");


        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send(ori + "|" + dest);
            }
            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket", "Message received");
                List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));

                /*listaCuadrantes = splittedMessage.get(0);
                results[0] = listaCuadrantes;

                beaconsClave = splittedMessage.get(1);
                ruta = splittedMessage.get(1);
                results[1] = ruta;

                hayGiro = splittedMessage.get(2);
                results[2] = hayGiro;

                beaconClave = splittedMessage.get(3);
                results[3] = beaconClave;*/

                results[0] = splittedMessage.get(0); //Lista de cuadrantes

                results[1] = splittedMessage.get(1); //Lista de beacons

                results[2] = splittedMessage.get(2); //Lista de instrucciones

                results[3] = splittedMessage.get(3); //Lista de giros

                results[4] = splittedMessage.get(4); //Lista de info adicional

                synchronized(webSocketClient) {//Ya tenemos toda la información
                    webSocketClient.notifyAll();
                }
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();

        synchronized (webSocketClient){
            try {
                if(infoRecibida.equals("no")) {
                    Log.i("WebSocket", "Vamos a esperar a los datos");
                    webSocketClient.wait();
                }
                webSocketClient.close();
            } catch (InterruptedException e) {
                //when the object is interrupted
            }
        }
        return results;
    }

}
