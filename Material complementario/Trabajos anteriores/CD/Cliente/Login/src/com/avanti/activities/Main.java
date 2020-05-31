package com.avanti.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
	
	 public void onCreate(final Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
			
	       try {
				wait(5000);
;
			} catch (Exception e) {
				// TODO Auto-generated catch block				
			}	
			
		Intent i = new Intent(this,MainMenu.class);
	       
	        startActivity(i);
	 }
	 
		/*public Context getContext(){
			return this.getApplicationContext();
		}*/
		
		/*
		 * De aqu’ hasta el pr—ximo comentario TODO tiene que estar en TODAS las actividades
		 *//*
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		    return true;
		}
		
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			super.onMenuItemSelected(featureId, item);
	        switch(item.getItemId()) {
	        case R.id.escanear:
	        	this.startActivity(new Intent(this,WPSActivity.class));
	            break;
	            
	        case R.id.salir:
	    	   f_salir();
	            break;
	        }
	        return true;
		}
		
	    public void f_salir()
	    {
	        setResult(RESULT_OK);
	        finish();
	    }*/ 
	    
	    /*
	     * Hasta aqu’
	     */


}
