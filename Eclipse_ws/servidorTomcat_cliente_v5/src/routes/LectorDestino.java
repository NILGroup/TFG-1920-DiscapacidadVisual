package routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Clase que se encarga de leer el JSON que contiene los posibles destinos a los que puede ir el usuario
 * Almacena cada uno de estos posibles destinos en un HashTable, poniendo como clave el lugar.
 * Para solicitar el cuadrante, únicamente debo preguntar por el lugar destino y obtendría el id del cuadrante
 * @author Mariana
 *
 */
public class LectorDestino {
	
	private Hashtable<String,Integer> t_valores;
	
	public LectorDestino(){
		
		t_valores=new Hashtable<String,Integer>();
		JSONParser parser = new JSONParser();
		JSONArray destinos;
		
		try {
			File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();
			//System.out.println("Catalina: " + catalinaBase);
			destinos = (JSONArray) parser.parse(new FileReader(catalinaBase + "/webapps/destinos.json"));
			for (Object o : destinos){
	
				JSONObject dest = (JSONObject) o;
				
				String lugar = (String) dest.get("lugar");
				String numCuadrante = (String) dest.get("cuadrante");
				Integer cuadrante = Integer.parseInt(numCuadrante);
				
				t_valores.put(lugar, cuadrante);
			
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public int buscarDestino(String d){
		
		if(t_valores.containsKey(d)){
			return t_valores.get(d);
		}
		else return -1;
	}

}
