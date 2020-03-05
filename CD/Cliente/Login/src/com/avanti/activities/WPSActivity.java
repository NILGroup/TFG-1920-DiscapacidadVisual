package com.avanti.activities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.avanti.dialogs.DialogController;
import com.avanti.http.HttpServices;
import com.avanti.wps.WPS;
import com.avanti.wps.WPSException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WPSActivity extends ListActivity{
	
	private WPS wps;
	public static List<ScanResult> resultados;
	public static int elementoSelec;
	public final static int MENU_REFRESH = 1;
	public final static int MENU_INSERT = 2;
	
	 public void onCreate(final Bundle savedInstanceState) {
		MainMenu.activeActivity = this;
		super.onCreate(savedInstanceState);
		cargarDatos();
	 }
	 
	 protected void onListItemClick (ListView l, View v, int position, long id){
		elementoSelec = position;
		Intent i = new Intent(this,ScanResultActivity.class);
		startActivity(i);
	 }
	 
	 private void cargarDatos(){
		if(wps==null){
			wps = new WPS(this.getApplicationContext());
		}
		refresh(); 
	 }
	 
	 private void refresh(){
		 try {
			resultados = wps.scanAndShow();
			String[] valores = new String[resultados.size()];
			
			for(int i = 0; i < resultados.size(); i++){
				valores[i] = resultados.get(i).SSID + " (" + resultados.get(i).BSSID + ")";
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			      android.R.layout.simple_list_item_1, valores);
			this.setListAdapter(adapter);
			
		} catch (WPSException e) {	
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage());
			builder.setPositiveButton("Activar", new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface arg0, int arg1) {
					WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					manager.setWifiEnabled(true);
					
					int i = 0;
					boolean b = false;
					while(!b && i < 10){
						b = manager.isWifiEnabled();
						i++;
						try {
							SystemClock.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(manager.isWifiEnabled()){
						refresh();
					}else{
						DialogController.createInformDialog(getContext(), "Se ha superado el tiempo de espera de activación de la antena WIFI.");
					}
				}
				
			});
			builder.setNegativeButton("Salir", new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					finish();					
				}
				
			});
			builder.setCancelable(false);
			AlertDialog alert = builder.create();
			alert.show();
		}
		
	 }
	 
	 public boolean onCreateOptionsMenu (Menu menu){
		menu.add(0, MainMenu.MENU_QUIT,0,"Salir").setIcon(android.R.drawable.star_off);
		menu.add(1, MENU_REFRESH,1,"Refrescar lista").setIcon(android.R.drawable.ic_menu_send);
		MenuItem insertar = menu.add(0,MENU_INSERT,0,"Insertar(Server)");
		insertar.setIcon(android.R.drawable.ic_menu_upload);
		if(resultados.size()>0){
			insertar.setEnabled(true);
		}else{
			insertar.setEnabled(false);
		}
		return true;
	 }
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MainMenu.MENU_QUIT:
		    finish();
		    return true;
		case MENU_REFRESH:
		    refresh();
		    return true;
		case MENU_INSERT:
			createInsertDialog();
			return true;
		}			
		return false;
	 }
	 
	 public Activity getContext(){
		 return this;
	 }
	 
	 private void createInsertDialog(){
		 
		 final Dialog dialog = new Dialog(this);

		 dialog.setContentView(R.layout.insertdialog);
		 dialog.setTitle("Inserte los datos");
		 Button aceptar = (Button)dialog.findViewById(R.id.AceptarInsertDialog);
		 aceptar.setOnClickListener(new android.view.View.OnClickListener(){

			public void onClick(View arg0) {
				HttpServices service = new HttpServices();
				try {
					if(service.ping()){
						String x = ((EditText)dialog.findViewById(R.id.XInsertDialog)).getText().toString();
						String y = ((EditText)dialog.findViewById(R.id.YInsertDialog)).getText().toString();
						String z = ((EditText)dialog.findViewById(R.id.ZInsertDialog)).getText().toString();
						Double xNum;
						Double yNum;
						Double zNum;
						if(x.equals("") || y.equals("") || z.equals("")){
							DialogController.createInformDialog(dialog.getContext(), "Debes rellenar todos los campos");
						}else{
							try{
								xNum = Double.parseDouble(x);
								yNum = Double.parseDouble(y);
								zNum = Double.parseDouble(z);
								boolean error = false;
								for(int i = 0; i < resultados.size(); i++){
									try {
										service.insertCoorDatabase(xNum, yNum, zNum, resultados.get(i).BSSID, resultados.get(i).level);
									} catch (ClientProtocolException e) {
										e.printStackTrace();
										error = true;
									} catch (IOException e) {
										e.printStackTrace();
										error = true;
									}
								}
								dialog.dismiss();
								if(error){
									DialogController.createInformDialog(getContext(), "Algunos datos no se han podido insertar.");
								}else{
									DialogController.createInformDialog(getContext(), "Todos los datos han sido insertados correctamente.");
								}
							}catch (NumberFormatException e) {
								e.printStackTrace();
								DialogController.createInformDialog(dialog.getContext(), "Debes introducir números válidos.");
							}
						}
					}else{
						DialogController.createInformDialog(dialog.getContext(), "Servidor inaccesible.");
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					DialogController.createInformDialog(dialog.getContext(), "Servidor inaccesible.");
				} catch (IOException e) {
					e.printStackTrace();
					DialogController.createInformDialog(dialog.getContext(), "Servidor inaccesible.");
				}
			}
			 
		 });
		 Button cancelar = (Button)dialog.findViewById(R.id.CancelarInsertDialog);
		 cancelar.setOnClickListener(new android.view.View.OnClickListener(){

			public void onClick(View arg0) {
				dialog.dismiss();
			}
			 
		 });
		 dialog.setOwnerActivity(this);
		 dialog.show();
		 
	 }
}
