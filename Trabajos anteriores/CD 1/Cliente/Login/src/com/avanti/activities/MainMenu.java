package com.avanti.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avanti.dialogs.DialogController;
import com.avanti.http.HttpServices;
import com.avanti.wps.ThreadDatos;

public class MainMenu extends ListActivity {	
    //static private TabHost mTabHost;
    private Resources mResources;	
    public static Activity activeActivity = null;
    public static ThreadDatos thread;
    
    public final static int MENU_QUIT = 0; 
    
    private String username;
    private String password;
    
    
    public class IconAndText extends LinearLayout{
    	
    	private boolean isSelectable;
    	private ImageView mIcon;
    	private TextView mText;
    	
		public IconAndText(Context context, Drawable icon, String text, boolean isSelectable) {
			super(context);
			this.setOrientation(HORIZONTAL);
			this.isSelectable = isSelectable;
	          mIcon = new ImageView(context);
	          mIcon.setImageDrawable(icon);
	         
	          mIcon.setPadding(0, 2, 5, 0);
	          
	          
	          addView(mIcon,  new LinearLayout.LayoutParams(
	                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	          
	          mText = new TextView(context);
	          mText.setText(text);
	          mText.setEms(20);
	        //  mText.setBackgroundColor(android.R.color.white);
	          addView(mText, new LinearLayout.LayoutParams(
	                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		public boolean isSelectable(){
			return isSelectable;
		}
		
		public ImageView getIcon(){
			return mIcon;
		}
    	
		public String getText(){
			return (String) mText.getText();
		}
		
		public void setText(String text){
			 mText.setText(text); 
		}
		
		public void setIcon(Drawable bullet){
			mIcon.setImageDrawable(bullet); 
		}
    }
    
    public class IconifiedTextListAdapter extends BaseAdapter {

       
        private Context mContext;

        private List<IconAndText> mItems = new ArrayList<IconAndText>();

        public IconifiedTextListAdapter(Context context) {
             mContext = context;
        }

        public void addItem(IconAndText it) { mItems.add(it); }

        public void setListItems(List<IconAndText> lit) { mItems = lit; }

        public int getCount() { return mItems.size(); }

        public Object getItem(int position) { return mItems.get(position); }

        public boolean areAllItemsSelectable() { return false; }

        public boolean isSelectable(int position) {
             try{
                  return mItems.get(position).isSelectable();
             }catch (IndexOutOfBoundsException aioobe){
                  return false;
             }
        }

        public long getItemId(int position) {
             return position;
        }

        /** @param convertView The old view to overwrite, if one is passed
         * @returns a IconifiedTextView that holds wraps around an IconifiedText */
        public View getView(int position, View convertView, ViewGroup parent) {
             IconAndText btv;
             if (convertView == null) {
                  btv = new IconAndText(mContext, mItems.get(position).getIcon().getDrawable(), mItems.get(position).getText(), mItems.get(position).isSelectable());
             } else { 
                  btv = (IconAndText) convertView;
                  btv.setText(mItems.get(position).getText());
                  btv.setIcon(mItems.get(position).getIcon().getDrawable());
             }
             return btv;
        }
   }
    
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       if(thread==null){
    	   thread = createAndStartThread(); //Comienza a recuperar los datos de BD
       }
       
       mResources = getResources();
       //Obtengo los datos del usuario logueado.
       username = Login.getUsername();
       password = Login.getPassword();
        
       IconifiedTextListAdapter adapter = new IconifiedTextListAdapter(this);
       IconAndText aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_gallery), "Ver actividad WiFi",true);
       adapter.addItem(aux);
       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_agenda), "Posicióname en el mapa",true);
       adapter.addItem(aux);
       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_compass), "Aplicación real",true);
       adapter.addItem(aux);
//       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_camera ), "Prueba animación",true);
//       adapter.addItem(aux);
       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_mapmode), "Prueba del acelerómetro",true);
       adapter.addItem(aux);
//       aux = new IconAndText(this, mResources.getDrawable(android.R.drawable.ic_menu_myplaces), "Introducir destino", true);
//       adapter.addItem(aux);
       aux = new IconAndText(this, mResources.getDrawable(android.R.drawable.ic_menu_directions), "Indicar ruta hacia el destino", true);
       adapter.addItem(aux);
       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_preferences), "Medir intensidades",true);
       adapter.addItem(aux);
       aux = new IconAndText(this,mResources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel), "Cerrar sesión", true);
       adapter.addItem(aux);
       this.setListAdapter(adapter);       
    }	
    
    /**
 	 * Crea un hilo para recuperar los datos de las posiciones de la BD en segundo plano
 	 * @return
 	 */
 	public ThreadDatos createAndStartThread(){
 		ThreadDatos t = new ThreadDatos(ThreadDatos.ESTADO_EJECUCION);
 		t.start();
 		return t;
 	}
 	
 	 protected void onListItemClick (ListView l, View v, int position, long id){
 		 if(position==0){
 			Intent intent = new Intent(this, WPSActivity.class);
 			startActivity(intent);
 		 }else if(position == 1){
 			Intent intent = new Intent(this, Position.class);
 			startActivity(intent);
 		 }else if(position == 2){
 			Intent intent = new Intent(this, CameraActivity.class);
 			startActivity(intent);
// 		 } else if(position == 3){
// 			Intent intent = new Intent(this, SpriteTextActivity.class);
// 			startActivity(intent);
 		 }else if(position == 3){
 			Intent intent = new Intent(this, TestAccelerometer.class);
 			startActivity(intent);
 		 }else if (position == 4){	
   			Intent intent = new Intent(this, VoicePlaybackSystem.class);
   			startActivity(intent);
 		 }else if (position == 5){
 			Intent intent = new Intent(this, Medir.class);
   			startActivity(intent);
 		 }else if (position == 6){	
 			logout(username, password);
			 finish();
     	}
 		 
 	 }
 	 
 	 public void logout(String user, String password){
 		HttpServices service = new HttpServices();
 		try {
			if(service.logout(user, password).equals("")){
				
			}else{
				DialogController.createInformDialog(this, "Error en el logout");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			DialogController.createInformDialog(this, "Excepción: " + e.getMessage());
		}
 	 }
 	
	 public String getUsername(){
		 return username;
	 }
	 
	 public String getPassword(){
		 return password;
	 }
 	 
 	 public void setUsername(String u){
 		 username = u;
 	 }
 	 
 	 public void setPassword(String p){
 		 password = p;
 	 }
}