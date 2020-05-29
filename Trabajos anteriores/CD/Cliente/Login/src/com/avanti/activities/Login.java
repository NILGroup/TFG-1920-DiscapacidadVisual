package com.avanti.activities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.avanti.dialogs.DialogController;
import com.avanti.http.HttpServices;


public class Login extends Activity {
	
	private static String username;
	private static String password;
	
	 public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	//requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 

	 	setContentView(R.layout.login);
	 	
	 	ArrayList<String> datos = this.recuperarDatosUsuario();
	 	if(datos!=null){
	 		((EditText)findViewById(R.id.txtUserName)).setText(datos.get(0));
	 		((EditText)findViewById(R.id.txtPassword)).setText(datos.get(1));
	 		((CheckBox)findViewById(R.id.chkRememberPassword)).setChecked(true);
	 	}
	 	
        Button loginButton = (Button)findViewById(R.id.buttonSignIn);
                    
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	EditText user = (EditText)findViewById(R.id.txtUserName);
            	String s = user.getText().toString();
            	if(s.equals("")){
            		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            		builder.setMessage("Debes rellenar el campo \"Usuario\"");
            		
            		builder.setPositiveButton("Aceptar", new OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
            		});
            		
            		builder.setCancelable(false);
            		AlertDialog alert = builder.create();
            		alert.show();
            	}else{
            		username = s;
            		password = ((EditText)findViewById(R.id.txtPassword)).getText().toString();
            		login(username,password);
            	}
            }
        });
        
        Button goRegister = (Button)findViewById(R.id.buttonRegFromLogin);
	     goRegister.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				Intent i = new Intent(getActivity(),Register.class);        	
				startActivity(i);			
			}			
	    	 
	     });

	 }
	 
	 	public void login(String user, String password) {
	 		HttpServices service = new HttpServices();
	 		try {
				if(service.login(user, password).equals("")){
					CheckBox check = (CheckBox)findViewById(R.id.chkRememberPassword);
					if(check.isChecked()){
						guardarDatosUsuario(user,password);
					}else{
						borrarDatosUsuario();
					}
					Intent i = new Intent(this,MainMenu.class); 
					startActivity(i);
				}else{
					DialogController.createInformDialog(this, "Error en el login");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
			}
	 	}
	 	
	 	public Context getContext(){
			return this.getApplicationContext();
		}
		
		public Activity getActivity(){
			return this;
		}
		
		/*
		 * De aqu’ hasta el pr—ximo comentario TODO tiene que estar en TODAS las actividades
		 */
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, MainMenu.MENU_QUIT,0,"Salir").setIcon(android.R.drawable.star_off);
			return true;
		}
		
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			//super.onMenuItemSelected(featureId, item);
			switch (item.getItemId()) {
			case MainMenu.MENU_QUIT:
				finish();
			    return true;
			}
			  return true;
		}
		
	    public void f_salir()
	    {
	        setResult(RESULT_OK);
	        finish();
	    } 
	    
	    /**
	     * Almacena los datos del usuario en el archivo "user.txt" para poder ser recuperados más tarde
	     * @param user
	     * @param password
	     */
	    public void guardarDatosUsuario(String user, String password){
	    	FileOutputStream fos; 
	    	DataOutputStream dos;
	    	try {
	    		
	    		File f = this.getFilesDir();
	    		String s = f.getCanonicalPath();
	    		File file= new File(s + "/user.txt");
	    		if(file.exists()){
	    			file.delete();
	    		}
	    		file.createNewFile();
	    		fos = new FileOutputStream(file);
	    		
	    		dos = new DataOutputStream(fos);
	    		dos.write(user.getBytes());
	    		dos.writeChars("\n");
	    		dos.write(password.getBytes());
	    		//dos.close();
	    	} catch (IOException e) {	
	    		e.printStackTrace();
	    	}
	    }
	    
	    /**
	     * Recupera los datos del usuario si han sido guardados anteriormente con la opción "Recordarme"
	     * @return <code>null</code> si no ha sido recuperado el usuario, los datos en otro caso
	     */
	    public ArrayList<String> recuperarDatosUsuario(){
	    	File f;
			try {
				f = new File(this.getFilesDir().getCanonicalPath() + "/user.txt");
				if(f.exists()){
			    	FileInputStream fis;
					try {
						fis = new FileInputStream(f);
						//BufferedInputStream bis = new BufferedInputStream(fis); 
				    	DataInputStream dis = new DataInputStream(fis);
				    	try {
							String user = dis.readLine();
							user = user.trim();
							String pass = dis.readLine();
							dis.close();
							ArrayList<String> result = new ArrayList<String>();
							result.add(user);
							result.add(pass);
							return result;
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
		    	}else{
		    		return null;
		    	}
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
	    	
	    }
	    
	    /**
	     * Elimina los datos del usuario
	     */
	    public void borrarDatosUsuario(){
	    	File f;
			try {
				f = new File(this.getFilesDir().getCanonicalPath() + "/user.txt");
				f.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
	    }

		public static String getUsername() {
			return username;
		}

		@SuppressWarnings("static-access")
		public void setUsername(String username) {
			this.username = username;
		}

		public static String getPassword() {
			return password;
		}

		@SuppressWarnings("static-access")
		public void setPassword(String password) {
			this.password = password;
		}
	    
}
