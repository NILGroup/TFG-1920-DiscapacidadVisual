package com.e.tomcatclient_prueba;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebSocketClient webSocketClient;
    TextView textV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpButtons();
        createWebSocketClient();
    }

    void setUpButtons(){
        Button send_button =  findViewById(R.id.send_msg);
        send_button.setOnClickListener(this);

        textV = findViewById(R.id.text);
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://88.18.203.7:8080/WebSocketServerExample/websocketendpoint");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }
            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            textV.setText(message);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
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
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage() {
        Log.i("WebSocket", "Button was clicked");
        // Send button id string to WebSocket Server
        webSocketClient.send("hola servidor, soy el cliente");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.send_msg):
                sendMessage();
                break;
        }
    }
}
