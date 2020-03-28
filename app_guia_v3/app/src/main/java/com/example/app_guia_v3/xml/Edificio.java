package com.example.app_guia_v3.xml;


import java.util.ArrayList;

import com.example.app_guia_v3.routes.Cuadrante;
import com.example.app_guia_v3.routes.Estancia;

//import android.os.Environment;

//import java.lang.String;
/**
 *
 * Clase de carga los xml que representan el edificio completo. En el archivo "edificio.xml" se encuentran
 * los diferentes archivos a cargar.
 *
 * 26/05/2014 - Revisado y limpiado
 *
 */
public class Edificio {

    private CargaXML carga;
    private ArrayList<Estancia> aEstancias = new ArrayList<Estancia>();
    private ArrayList<Cuadrante> aCuadrantes = new ArrayList<Cuadrante>();

    public Edificio(/*File xmlFile*/) {


        /*SAXBuilder builder = new SAXBuilder();
        //File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\app_guia_v3\\app\\xml\\edificioNEW.xml");


        /*FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Not sure if the / is on the path or not
            File f = new File(baseDir + File.separator + "edificioNEW.xml");

            f.write();
            f.flush();
            f.close();

            /*File file = new File(path, "/" + fname);
            fichero = new FileWriter("C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\app_guia_v3\\app\\xml\\edificioNEW.xml");
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
                    //fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        Log.i("EDIFICIO", "En EDIFICIO 5555555 ");
        File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\app_guia_v3\\app\\xml\\edificioNEW.xml");
        CargaXML carga;
        Document document = null;
        try {
            document = (Document) builder.build( xmlFile );
            Log.i("EDIFICIO", "En EDIFICIO 222222 ");
        } catch (JDOMException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//Se crea el documento a traves del archivo
        Element rootNode = document.getRootElement();//Se obtiene la raiz 'edificio'
        System.out.println(rootNode.toString());*/

        //Se crea un SAXBuilder para poder parsear el archivo
        /*SAXBuilder builder = new SAXBuilder();
        //String path = "C:\\Users\\MRS\\Documents\\Belen\\xml\\edificio.xml";
        String path = "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\app_guia_v3\\app\\xml\\edificio.xml";
        //String path = "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\app_guia_v3\\app\\src\\main\\AndroidManifest.xml";
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
    */
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