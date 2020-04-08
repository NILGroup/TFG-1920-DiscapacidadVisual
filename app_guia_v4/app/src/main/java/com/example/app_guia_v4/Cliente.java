package com.example.app_guia_v4;

import android.util.Log;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;


public class Cliente {

    private WebSocketClient webSocketClient;


    private String dest, b_mas_cerca, origen;
    String listaCuadrantes, ruta, beaconClave = "NO", hayGiro;
    private boolean verbose;
    final String [] results = new  String[4];

    public Cliente(String destino, String beaconMasCercano, String ori, boolean verb){
        dest = destino;
        b_mas_cerca = beaconMasCercano;
        origen = ori;
        verbose = verb;
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
                webSocketClient.send(origen + "|" + dest + "|" + b_mas_cerca + "|" + String.valueOf(verbose));
            }
            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket", "Message received");
                List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));

                listaCuadrantes = splittedMessage.get(0);
                results[0] = listaCuadrantes;

                ruta = splittedMessage.get(1);
                results[1] = ruta;

                hayGiro = splittedMessage.get(2);
                results[2] = hayGiro;

                beaconClave = splittedMessage.get(3);
                results[3] = beaconClave;

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
                if(beaconClave.equals("NO")) {
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

    public String toString(boolean verb) {
        if (verb) return "true";
        else return "false";
    }

}
