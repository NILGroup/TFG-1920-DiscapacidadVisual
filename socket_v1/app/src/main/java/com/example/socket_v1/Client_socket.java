package com.example.socket_v1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import android.os.Handler;

import androidx.annotation.NonNull;
import android.widget.EditText;

import 	android.text.method.ScrollingMovementMethod;

import 	android.util.Log;

public class Client_socket extends AppCompatActivity implements View.OnClickListener {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, Client_socket.class);
    }

    public static final String TAG = "Client_socket";
    private EditText editText;
    String addressServer ="192.168.1.38"; //"147.96.96.209";//"172.20.10.6";//"192.168.1.43";//"192.168.1.34"; //"147.96.102.38";//

    int PORT = 2222;
    Socket socket = null;
    String msg = "Hola servidor\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_socket);
        //Log.i(TAG, "antes de llamar a new edificio");
        //Setup buttons
        setupButtons();

    }

    private void setupButtons() {
        Button startScanButton =  findViewById(R.id.start_scan_button);
        Button stopScanButton =  findViewById(R.id.stop_scan_button);

        editText = findViewById(R.id.beacon_text);
        editText.setMovementMethod(new ScrollingMovementMethod());

        startScanButton.setOnClickListener(this);
        stopScanButton.setOnClickListener(this);
    }

    protected Long socketConnect() {
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

                    out.writeUTF(msg);
                    editText.setText(msg);

                    String msgServ  = in.readUTF();
                    editText.append(msgServ);

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
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_scan_button:
                socketConnect();
                break;
            case R.id.stop_scan_button:
                //stopScanning();
                break;
        }
    }

}
