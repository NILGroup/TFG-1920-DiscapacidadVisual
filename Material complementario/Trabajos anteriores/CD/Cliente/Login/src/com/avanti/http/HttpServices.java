package com.avanti.http;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

public class HttpServices {
	
//	public static String host = "31.170.164.18";  //"10.0.2.2";    
//	public static String port = "80";
//	public static String urlServer = "http://proyectosi.hol.es/"; //"http://10.0.2.2/"; 
	
	public static String host = "147.96.81.191";  //"10.0.2.2";    
	public static String port = "80";
	public static String urlServer = "http://nil.fdi.ucm.es/avanti/"; //"http://10.0.2.2/"; 
	
    public HttpServices(){
    	
	}
    
    public boolean ping() throws UnknownHostException, IOException, SecurityException {

    	String[] datos = {host, port};
    	PingConnect p = new PingConnect();
    	p.execute(datos);
    	try {
			p.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	boolean conecta = p.getRespuesta();
    	return conecta;
    	
    }
    
    /**
     * M�todo que logea a un usuario en el sistema
     * @param user
     * @param password
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String login(String user, String password) throws ClientProtocolException, IOException{
    	if(ping()){
    		
    		HttpGet httpget = new HttpGet(urlServer + "consultar.php?username=" + user + "&password=" + password);
    		HttpGet[] a = {httpget};
    		
    		// Llamamos a la clase que ejecuta en segundo plano.
            Conecta c= new Conecta();
	        c.execute(a);
	        		   	        
	        // Esperamos a que termine de ejecutarse el hilo con el m�todo get.
	       try {
			c.get();
	       } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	       
	        String response = c.getResponse();
	        
		    if(response.equals("SI")){
		    	return "";
		    }else{
		    	return "Usuario no registrado";
		    }
		    
    	}else{
    		return "Servidor inaccesible";
    	}
	        	        
    }
    
    public String logout(String user, String password) throws ClientProtocolException, IOException{
    	if(ping()){
    		
    		HttpGet httpget = new HttpGet(urlServer + "logout.php?username=" + user + "&password=" + password);
    		HttpGet[] a = {httpget};
    		
    		// Llamamos a la clase que ejecuta en segundo plano.
            Conecta c= new Conecta();
	        c.execute(a);
	        
	       // Esperamos a que termine de ejecutarse el hilo con el m�todo get.
	       try {
			c.get();
	       } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	       
	        String response = c.getResponse();
	            
		    if(response.equals("SI")){
		    	return "";
		    }else{
		    	return "No se ha podido salir correctamente de la aplicaci�n";
		    }
		    
    	}else{
    		return "Servidor inaccesible";
    	}
	        	        
    }
    
    /**
     * M�todo que registra a un usuario en el sistema
     * @param user
     * @param password
     * @param name
     * @param surname
     * @return <code>"CORRECTO"</code> si lo consigue, <code>"ERROR"</code> en caso de error e informaci�n
     * de la excepci�n en otro caso 
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String register(String user, String password, String name, String surname) throws ClientProtocolException, IOException{
    	if(ping()){
    		
    		HttpGet httpget = new HttpGet(urlServer + "registrar.php?username=" + user + "&password=" + password + "&nombre=" + name + "&apellidos="+ surname);
    		HttpGet[] a = {httpget};
    		
    		// Llamamos a la clase que ejecuta en segundo plano.
            Conecta c= new Conecta();
	        c.execute(a);
	        		   	        
	        // Esperamos a que termine de ejecutarse el hilo con el m�todo get.
	       try {
			c.get();
	       } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	       
	        String response = c.getResponse();
		    return response;
		    
    	}else{
    		return "Servidor inaccesible";
    	}
    }
    
    /**
     * M�todo que inserta unos datos para unas coordenadas en la Base de Datos
     * @param x
     * @param y
     * @param z
     * @param mac
     * @param strength
     * @param sobreescribir
     * @return "" si tiene �xito, la informaci�n del error en caso de fallo.
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String insertCoorDatabase(double x, double y, double z, String mac, int strength) throws ClientProtocolException, IOException{
    	if(ping()){
    		
    		HttpGet httpget = new HttpGet(urlServer + "insertNewPosition.php?mac=" + mac + "&x=" + x + "&y=" + y + "&z="+ z + "&strength=" + strength);
    		HttpGet[] a = {httpget};
    		
    		// Llamamos a la clase que ejecuta en segundo plano.
            Conecta c= new Conecta();
	        c.execute(a);
	        		   	        
	        // Esperamos a que termine de ejecutarse el hilo con el m�todo get.
	       try {
			c.get();
	       } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	       
	        String response = c.getResponse();
	        
		    if(response.equals("OK")){
		    	return "";
		    }else{
		    	return response;
		    }
		    
    	}else{
    		return "Servidor inaccesible";
    	}
    }
    
    /**
     * Recupera todas las coordenadas almacenadas en la BD
     * @return
     * @throws HttpServicesException
     */
    public JSONArray getCoordenadas() throws HttpServicesException{
    	try {
			if(ping()){
				
	    		HttpGet httpget = new HttpGet(urlServer + "getAllPositions.php");
	    		HttpGet[] a = {httpget};
	    		
	    		// Llamamos a la clase que ejecuta en segundo plano.
	            Conecta c= new Conecta();
		        c.execute(a);
		        		   	        
		        // Esperamos a que termine de ejecutarse el hilo con el m�todo get.
		       try {
				c.get();
		       } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		       } catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		       
		        String response = c.getResponse();
		        
			    JSONArray datos = new JSONArray(response);
			    return datos;
			    
			}else{
				throw new HttpServicesException("Host de destino inaccesible.");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		}
    }
  /*  
  public JSONArray getFires() throws HttpServicesException{
    	try{
    		if (ping()){
    			String url = urlServer + "getAllFires.php";
    			DefaultHttpClient httpClient = new DefaultHttpClient();
    			HttpGet http = new HttpGet(url);
    			BasicResponseHandler handler = new BasicResponseHandler();
    			String response = httpClient.execute(http,handler);
    			httpClient.getConnectionManager().shutdown();
    			JSONArray fuegos = new JSONArray(response);
    			return fuegos;
    		}
    		else{
    			throw new HttpServicesException("Host de destino inaccesible.");
    		}
    		
    	}
    	catch(UnknownHostException e){
    		e.printStackTrace();
    		throw new HttpServicesException(e.getMessage());
    	}
    	catch(SecurityException e){
    		e.printStackTrace();
    		throw new HttpServicesException(e.getMessage());
    	}
    	catch (FactoryConfigurationError e) {
 			e.printStackTrace();
 			throw new HttpServicesException(e.getMessage());
 		}
    	catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		}
    	catch (IOException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		}
    	catch (JSONException e) {
			e.printStackTrace();
			throw new HttpServicesException(e.getMessage());
		}
    }
*/
}
