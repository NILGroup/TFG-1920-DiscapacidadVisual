package com.avanti.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.avanti.dialogs.DialogController;
import com.avanti.position.Mapa;
import com.avanti.routes.ListaCuadrantes;
import com.avanti.wps.ThreadDatos;
import com.avanti.wps.ThreadUbicacion;
//import android.widget.AbsoluteLayout;


public class Position extends PositionActivity {
		
	private ThreadUbicacion thread;
	private ProgressDialog pd;
	private Thread comprobar;
	private Mapa m;
	private ListaCuadrantes cuadrantes;
		

	public void onCreate(final Bundle savedInstanceState) {
		MainMenu.activeActivity = this;
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		setContentView(R.layout.position);	
		RelativeLayout main = (RelativeLayout) findViewById(R.id.positionLayout);
//		AbsoluteLayout main = (AbsoluteLayout) findViewById(R.id.positionLayout);
		
		
		//cuadrantes = thread.getCuadrantes();
		cuadrantes = new ListaCuadrantes();
		m = new Mapa(this);
		m.setListaCuadrantes(cuadrantes);
//		main.addView(m, params);
		main.addView(m);
		pd = ProgressDialog.show(this, "", "Cargando datos...");
		comprobar = new Thread(){
			public void run(){
				comprobar();
			}
		};
		comprobar.start();
		

	}
	
	/*public boolean onTouchEvent (MotionEvent event){
		
		
	}*/
	
		
	private void comprobar(){
		if(MainMenu.thread.getEstado() == ThreadDatos.ESTADO_ERROR){
			pd.dismiss();
			DialogController.createInformDialog(this, "Ha ocurido un error mientras se recuperaban los datos.");
		}else if(MainMenu.thread.getEstado() == ThreadDatos.ESTADO_EJECUCION){
			SystemClock.sleep(2000);
			comprobar();
		}else if(MainMenu.thread.getEstado() == ThreadDatos.ESTADO_EXITO){
			//Mapa m = new Mapa(this);
			pd.dismiss();
			//setContentView(m);
			if(thread == null){
				thread = new ThreadUbicacion(this);
				thread.setCuadrantes(cuadrantes);
				thread.start();
			}
		}
	}
	
	public void refresh(double x, double y, double z, int c){
		m.setPosX(x);
		m.setPosY(y);
		m.setPosZ(z);
		m.setCuadrante(c);
		/*try{
			m.invalidate();
		}catch(Exception e){
			String s = e.getMessage();
			int i = 0;
		}*/
	}
	
	public void pintarPosicion(){
		//TODO
	}
	
	public ListaCuadrantes getCuadrantes(){
		return cuadrantes;
	}
	
	public void setCuadrantes(ListaCuadrantes l){
		cuadrantes= l;
	}
	
	public Mapa getMapa(){
		return m;
	}
	
}
