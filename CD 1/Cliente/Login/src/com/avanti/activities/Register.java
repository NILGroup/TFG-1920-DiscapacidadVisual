package com.avanti.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.avanti.dialogs.DialogController;
import com.avanti.http.HttpServices;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Actividad que representa la pantalla de registro en la aplicación
 * @author Javier
 *
 */
public class Register extends Activity{

	public void onCreate(final Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.register);
		 
		 Button cancel = (Button)findViewById(R.id.cancelRegister);
		 cancel.setOnClickListener(new OnClickListener(){
			 
			public void onClick(View v) {
				finish();			
			}
			 
		 });
		 
		 Button aceptar = (Button)findViewById(R.id.acceptRegister);
		 aceptar.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				String s = comprobarDatos();
				if(s.equals("")){
					register();
				}else{
					DialogController.createInformDialog(getActivity(), s);
				}
			}
			 
		 });
	}
	
	/**
	 * Método privado que registra al usuario
	 */
	private void register(){
		HttpServices service = new HttpServices();
		try {
			String s = service.register(((EditText)findViewById(R.id.UserRegister)).getText().toString(), 
					((EditText)findViewById(R.id.PassRegister)).getText().toString(), 
					((EditText)findViewById(R.id.NombreRegister)).getText().toString(),
					((EditText)findViewById(R.id.ApeRegister)).getText().toString());
			DialogController d = new DialogController(){
				public void clickAccept() {
					finish();				
				}	
			};
			d.showInformDialog(this, s); 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
		}
	}
	
	public Activity getActivity(){
		return this;
	}
	
	public String comprobarDatos() {
		EditText text = (EditText)findViewById(R.id.NombreRegister);
    	String s = text.getText().toString();
    	if(s.equals("")){
    		 return "Debes completar el campo \"Nombre\"";
    	}
    	text = (EditText)findViewById(R.id.ApeRegister);
    	if(s.equals("")){
   		 return "Debes completar el campo \"Apellidos\"";
    	}
    	text = (EditText)findViewById(R.id.UserRegister);
    	if(s.equals("")){
   		 return "Debes completar el campo \"Usuario\"";
    	}
    	text = (EditText)findViewById(R.id.PassRegister);
    	if(s.equals("")){
   		 return "Debes completar el campo \"Password\"";
    	}
    	return "";
	}
	
}
