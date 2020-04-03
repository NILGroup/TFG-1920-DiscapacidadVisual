package com.example.app_guia_v4;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.TextView;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

public class Cliente {

    private WebSocketClient webSocketClient;


    private String dest, b_mas_cerca, origen;
    String listaCuadrantes, ruta, rutaFinal, beaconClave = null;
    private int cuadranteClave;
    private boolean verbose;
    final String [] results = new  String[4];

    public Cliente(String destino, String beaconMasCercano, String ori, boolean verb){
        dest = destino;
        b_mas_cerca = beaconMasCercano;
        origen = ori;
        verbose = verb;
    }

    protected void esperaDatos(){
        //while(beaconClave == null){}
        try{Thread.sleep(5000);}
        catch(Exception e){
            Log.i("WebSocket", "Problemas en sleep de esperaDatos");
        }
        Log.i("WebSocket", "Salimos de esperaDatos");
    }

    protected String[] createWebSocketClient() {
        URI uri;

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
                /*webSocketClient.send("origen:|"+origen);
                webSocketClient.send("destino:|"+dest);
                webSocketClient.send("actual:|"+b_mas_cerca);
                webSocketClient.send("listaCuad:|");*/
                webSocketClient.send(origen + "|" + dest + "|" + b_mas_cerca);
            }
            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket", "Message received");
                List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));

                listaCuadrantes = splittedMessage.get(0);
                results[0] = listaCuadrantes;

                ruta = splittedMessage.get(1);
                results[1] = ruta;

                beaconClave = splittedMessage.get(2);
                results[3] = beaconClave;

                /*switch (splittedMessage.get(0)) {
                    case "origen:": //ha recibido origen y envio el destino
                        //webSocketClient.send("destino:|"+dest);
                        break;

                    case "destino:": //ha recibido el destino y envio el beacon actual
                        //webSocketClient.send("actual:|"+b_mas_cerca);
                        break;

                    case "actual:": //ha recibido el beacon actual y solicito la lista de cuadrantes
                        //webSocketClient.send("listaCuad:|");
                        break;

                    case "listaCuad:": //me envia la lista de cuadrantes
                        listaCuadrantes= splittedMessage.get(1);
                        results[0] = listaCuadrantes;
                        Log.i("WebSocket", "ListaCuad: " + results[0]);
                        //solicito la instruccion
                        webSocketClient.send("instruccion:|");
                        break;

                    case "instruccion:": //me envia la instr
                        ruta = splittedMessage.get(1);
                        results[1] = ruta;
                        Log.i("WebSocket", "ruta: " + ruta + " \n");
                        //solicito el beacon clave
                        webSocketClient.send("beaconClave:|");
                        break;
                    case "instruccionFinal:": //me envia la instr final
                        rutaFinal = splittedMessage.get(1);
                        results[2] = rutaFinal;
                        Log.i("WebSocket", "rutaFinal: " + rutaFinal + " \n");
                        //solicito el beacon clave
                        webSocketClient.send("beaconClave:|");
                        break;

                    case "beaconClave:": //me envia el beacon clave
                        beaconClave= splittedMessage.get(1);
                        results[3] = beaconClave;
                        Log.i("WebSocket", "beacon clave: " + beaconClave + " \n");
                        break;
                }*/

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

        esperaDatos();

        webSocketClient.close();

        return results;
    }

}
