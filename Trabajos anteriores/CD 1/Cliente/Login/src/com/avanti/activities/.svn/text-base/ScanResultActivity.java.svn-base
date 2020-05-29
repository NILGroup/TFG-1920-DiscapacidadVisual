package com.avanti.activities;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanResultActivity extends Activity{
	
	 public void onCreate(final Bundle savedInstanceState){
		 
		 super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.wirelessnetworks);
		 
		 ScanResult resultado =  WPSActivity.resultados.get(WPSActivity.elementoSelec);
		 
		 TextView ssid = (TextView)findViewById(R.id.ssid);
		 ssid.setText(resultado.SSID);
		 
		 TextView bssid = (TextView)findViewById(R.id.bssid);
		 bssid.setText(resultado.BSSID);
		 
		 TextView frequency = (TextView)findViewById(R.id.frequency);		 
		 frequency.setText("" + resultado.frequency);
		 
		 TextView level = (TextView)findViewById(R.id.level);
		 level.setText("" + resultado.level);
		 
		 Button b = (Button)findViewById(R.id.ScanResVolver);
		 b.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				finish();				
			}
			 
		 });
		 
	 }
}
