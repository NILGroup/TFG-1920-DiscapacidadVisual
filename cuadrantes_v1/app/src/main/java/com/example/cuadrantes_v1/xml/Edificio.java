package com.example.cuadrantes_v1.xml;


import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document; // |
import org.jdom2.Element; // |\ Librer�as
import org.jdom2.JDOMException; // |/ JDOM
import org.jdom2.input.SAXBuilder; // |

import com.example.cuadrantes_v1.routes.Cuadrante;
import com.example.cuadrantes_v1.routes.Estancia;

//import android.os.Environment;
import java.io.InputStream;
import java.io.StringReader;

/**
 *
 * Clase de carga los xml que representan el edificio completo. En el archivo "edificio.xml" se encuentran
 * los diferentes archivos a cargar.
 *
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class Edificio  {

    private CargaXML carga;
    private ArrayList<Estancia> aEstancias = new ArrayList<Estancia>();
    private ArrayList<Cuadrante> aCuadrantes= new ArrayList<Cuadrante>();

    public Edificio(/*File xmlFile*/) {

        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        //String path = "C:\\Users\\MRS\\Documents\\Belen\\xml\\edificio.xml";
        String path = "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\edificio.xml";
        File xmlFile = new File(path);

        //Log.i("EDIFICIO", "En EDIFICIO");
        try
        {
            Log.i("EDIFICIO", "En EDIFICIO 5555555 "+xmlFile.getName());
            //Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
            Document document = builder.build(new StringReader(path));
            //Document document = null;
            Log.i("EDIFICIO", "En EDIFICIO 2222222 "+xmlFile.getName());
            Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
            List lista_plantas = rootNode.getChildren( "planta" );//Se obtiene la lista de hijos de la raiz 'edificio'

            for ( int indexPlanta = 0; indexPlanta < lista_plantas.size(); indexPlanta++ )//Se recorre la lista de 'plantas'
            {
                Element planta = (Element) lista_plantas.get(indexPlanta);//Se obtiene el elemento 'planta'
                String nombre = planta.getAttributeValue("nombre");//Se obtiene el atribullo 'idp'


                String archivo = planta.getChildText("archivo");
                if (!archivo.equals("nada")){
                    carga= new CargaXML(archivo);
                    aEstancias.addAll(carga.getEstancias());
                    aCuadrantes.addAll(carga.getCuadrantes());
                }
                for(int i=0; i < aCuadrantes.size(); i++) {
                    Log.i("EDIFICIO", "Cuadrante " + aCuadrantes.get(i).getID());

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

    /*public static void main(String[] args) {
        new Edificio();
    }*/

}
