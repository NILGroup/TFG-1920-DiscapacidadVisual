import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SAXBuilder builder = new SAXBuilder();
	    //File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\edificioNEW.xml");
	    
	    
	    FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\edificioNEW.xml");
            pw = new PrintWriter(fichero);

            
            pw.println("<?xml version='1.0' encoding='UTF-8'?>");
            pw.println("<edificio>");
            pw.println("</edificio>");
            
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\edificioNEW.xml");
        CargaXML carga;
        Document document = null;
		try {
			document = (Document) builder.build( xmlFile );
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Se crea el documento a traves del archivo
        Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
        System.out.println(rootNode.toString());
        
        
        
        //***************************************************************
	    /*CargaXML carga;
	    ArrayList<Estancia> aEstancias = new ArrayList<Estancia>();
	    ArrayList<Cuadrante> aCuadrantes= new ArrayList<Cuadrante>();
	    
	    try
        {

            Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
            Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
            List lista_plantas = rootNode.getChildren( "planta" );//Se obtiene la lista de hijos de la raiz 'edificio'

            for ( int indexPlanta = 0; indexPlanta < lista_plantas.size(); indexPlanta++ )//Se recorre la lista de 'plantas'
            {
                Element planta = (Element) lista_plantas.get(indexPlanta);//Se obtiene el elemento 'planta'
                String nombre = planta.getAttributeValue("nombre");//Se obtiene el atribullo 'idp'


                String archivo = planta.getChildText("archivo");
                if (!archivo.equals("nada")){
                    carga= new CargaXML(archivo);
                    System.out.println("Archivo "+ archivo );
                    aEstancias.addAll(carga.getEstancias());
                    aCuadrantes.addAll(carga.getCuadrantes());
                }
                for(int i=0; i < aCuadrantes.size(); i++) {
                	System.out.println("Cuadrante " + aCuadrantes.get(i).getID());
                	System.out.println("Conectado " + aCuadrantes.get(i).conectado[0]);
                	System.out.println("Beacon immediate " + aCuadrantes.get(i).beacons[0]);
                }
            }
        }catch ( IOException io ) {
            System.out.println( io.getMessage() );
        }catch ( JDOMException jdomex ) {
            System.out.println( jdomex.getMessage() );
        }*/
		
        
    }

}
	
	

