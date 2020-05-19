package com.example.app_guia_v5;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;


public class Cliente  {

    private WebSocketClient webSocketClient;


    private String dest, ori;
    private String infoRecibida = "no";
    final String [] results = new  String[5];
    private String uri_serv;

    public Cliente(String destino, String origen, String uri){
        dest = destino;
        ori = origen;
        uri_serv = uri;
    }

    protected String[] createWebSocketClient() {
        final URI uri;

        try {
            // Connect to local host
            //uri = new URI("ws://192.168.1.38:8080/servidorTomcat_cliente_v5/websocketendpoint");
            uri = new URI(uri_serv);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        Log.i("WebSocket", "Antes de webSocket");


        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting " + ori + "|" + dest);
                webSocketClient.send(ori + "|" + dest);
            }
            @Override
            public void onTextReceived(String message) {
                Log.i("WebSocket", "Message received");
                List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));

                results[0] = splittedMessage.get(0); //Lista de beacons

                results[1] = splittedMessage.get(1); //Lista de instrucciones

                results[2] = splittedMessage.get(2); //Lista de giros

                results[3] = splittedMessage.get(3); //Lista de info adicional

                synchronized(webSocketClient) {//Ya tenemos toda la información
                    infoRecibida = "si";
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
                Log.i("WebSocket", "Excepcion ");
                synchronized(webSocketClient) {
                    webSocketClient.notifyAll();
                }
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
            }
        };
        //webSocketClient.setConnectTimeout(10000);
        webSocketClient.setConnectTimeout(5000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();

        synchronized (webSocketClient){
            try {
                if(infoRecibida.equals("no")) {
                    Log.i("WebSocket", "Vamos a esperar a los datos");
                    webSocketClient.wait();

                    if(infoRecibida.equals("no")){
                        webSocketClient.close();
                        String [] noServInfo = new String[1];
                        noServInfo[0] = "noInfo";
                        return noServInfo;
                    }
                    webSocketClient.close();
                }
            } catch (InterruptedException e) {
                //when the object is interrupted
            }
        }
        return results;
    }

}