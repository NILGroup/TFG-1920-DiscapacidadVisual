package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import routes.Cuadrante;
import routes.Estancia;

/**
 * 
 * Clase de carga los xml que representan el edificio completo. En el archivo "edificio.xml" se encuentran
 * los diferentes archivos a cargar.
 * 
 *
 */
public class Edificio {

	private CargaXML carga;
	private ArrayList<Estancia> aEstancias = new ArrayList<Estancia>();
	private ArrayList<Cuadrante> aCuadrantes= new ArrayList<Cuadrante>();

	public Edificio() {

			 //Se crea un SAXBuilder para poder parsear el archivo
		    SAXBuilder builder = new SAXBuilder();
		    
		    File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();    
		    File xmlFile = new File( catalinaBase, "webapps/xml_modif/edificio.xml" );
		    
		    
		    try
		    {

		        Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
		        Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
		        List lista_plantas = rootNode.getChildren( "planta" );//Se obtiene la lista de hijos de la raiz 'edificio'
		        
		        for ( int indexPlanta = 0; indexPlanta < lista_plantas.size(); indexPlanta++ )//Se recorre la lista de 'plantas'
		        {
		            Element planta = (Element) lista_plantas.get(indexPlanta);//Se obtiene el elemento 'planta'
		            //String nombre = planta.getAttributeValue("nombre");//Se obtiene el atribullo 'idp'
		            
		           
		           String archivo = planta.getChildText("archivo");
		           if (!archivo.equals("nada")){
		        	   carga= new CargaXML(archivo);
		        	   aEstancias.addAll(carga.getEstancias());
		        	   aCuadrantes.addAll(carga.getCuadrantes());
		           }
		        }
		    }catch ( IOException io ) {
		        System.out.println( io.getMessage() );
		    }catch ( JDOMException jdomex ) {
		        System.out.println( jdomex.getMessage() );
		    }
		}
	
	public ArrayList<Estancia> getEstancias(){
		return aEstancias;
	}
	
	public ArrayList<Cuadrante> getCuadrantes() {
		return aCuadrantes;
	}
	
	public static void main(String[] args) {
		new Edificio();
	}

}
