package xml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document; // |
import org.jdom2.Element; // |\ Librerías
import org.jdom2.JDOMException; // |/ JDOM
import org.jdom2.input.SAXBuilder; // |

import routes.Cuadrante;
import routes.Planta;


/**
 * 
 * Clase útil para la carga de los diferentes XML.
 * 
 *
 */
public class CargaXML {

	private ArrayList<Planta> aPlantas;
	private ArrayList<Cuadrante> aCuadrantes = new ArrayList<Cuadrante>();

	@SuppressWarnings("rawtypes")
	public CargaXML(String nombreXML) {
		
		 //Se crea un SAXBuilder para poder parsear el archivo
	    SAXBuilder builder = new SAXBuilder();
	    File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();
	    File xmlFile = new File(catalinaBase,"webapps/xml_modif/"+nombreXML+".xml");
	    try
	    {

	        Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
	        Element rootNode = document.getRootElement();//Se obtiene la raiz 'planta'
	        List lista_estancias = rootNode.getChildren( "estancia" );//Se obtiene la lista de hijos de la raiz 'planta'
	        String planta = rootNode.getChildText("Z");
	        
	        aPlantas = new ArrayList<Planta>();
	        for ( int indexEstancia = 0; indexEstancia < lista_estancias.size(); indexEstancia++ )//Se recorre la lista de 'estancias'
	        {
	            Element estancia = (Element) lista_estancias.get(indexEstancia);//Se obtiene el elemento 'estancia'
	            String idEstancia = estancia.getAttributeValue("id");//Se obtiene el atribullo 'id'
	            //String tipo = estancia.getChildText("tipo");
	            
	            
	            Element cuadrantes = estancia.getChild("cuadrantes");//Obtener el elemento 'cuadrantes'
            	 List lista_cuadrantes = cuadrantes.getChildren(); //Cogemos la lista de hijos de un cuadrante
	            
	            Planta e = new Planta(idEstancia);
	            
	            for ( int indexCuadrante = 0; indexCuadrante < lista_cuadrantes.size(); indexCuadrante++ ) //Se recorre la lista de 'cuadrantes'
	            {
		            Element cuadrante = (Element) lista_cuadrantes.get(indexCuadrante);//Se obtiene el elemento 'cuadrante'
	            	String idCuadrante = cuadrante.getAttributeValue("idc");//Se obtiene el atribullo 'idc'
	            	String beacon = cuadrante.getChildTextTrim("beacon");
	            	 
 		            Element conectado = cuadrante.getChild("conectado");
 		            String norte = conectado.getChildText("norte");
 		            String sur = conectado.getChildText("sur");
 		            String este = conectado.getChildText("este");
 		            String oeste = conectado.getChildText("oeste");
	           
 		            String dir = cuadrante.getChildTextTrim("direccion"); 
 		            
 		            Element pesos = cuadrante.getChild("pesos");
		            String p_norte = pesos.getChildText("p_norte");
		            String p_sur = pesos.getChildText("p_sur");
		            String p_este = pesos.getChildText("p_este");
		            String p_oeste = pesos.getChildText("p_oeste");
		            
		            System.out.println("Cuadrante: " + idCuadrante);
		            System.out.println("Pesos: " + p_norte + " " + p_sur + " " + p_este + " " + p_oeste);
 		            
	 	            String info = cuadrante.getChildTextTrim("info"); 
	 	            
	 	            String m = cuadrante.getChildTextTrim("metros");
	 	            float metros = Float.parseFloat(m);

	 	            Cuadrante c = new Cuadrante(Integer.parseInt(idCuadrante), beacon, Integer.parseInt(planta),
	 	            		new String[]{norte,sur,este,oeste}, dir,
	 	            		new int[] {Integer.parseInt(p_norte),Integer.parseInt(sur),
	 	            				Integer.parseInt(este),Integer.parseInt(oeste)},info, metros);
	 	            aCuadrantes.add(c);
	 	            e.add(c);
		 	        		 	            	 
	            }
	            
	            aPlantas.add(e);
	
	        }
	        
	    } catch (IOException io) {
	        System.out.println( io.getMessage() );
	    }catch (JDOMException jdomex) {
	        System.out.println( jdomex.getMessage() );
	    }
	}
	
	public ArrayList<Cuadrante> getCuadrantes() {
		return aCuadrantes;
	}


	public ArrayList<Planta> getPlantas(){
		return aPlantas;
	}


}
