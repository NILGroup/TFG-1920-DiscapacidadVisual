package com.example.cuadrantes_v1.xml;
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
import com.example.cuadrantes_v1.routes.Posicion;


public class CargaXML {

    private ArrayList<Estancia> aEstancias;
    private ArrayList<Cuadrante> aCuadrantes = new ArrayList<Cuadrante>();

    @SuppressWarnings("rawtypes")
    public CargaXML(String nombreXML) {

        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File( "C:\\Users\\MRS\\Documents\\Belen\\ucm\\5\\TFG infor\\TFG-1920-DiscapacidadVisual\\cuadrantes_v1\\app\\xml\\"+nombreXML+".xml" );
        //File xmlFile = new File( "");
        try
        {

            Document document = (Document) builder.build( xmlFile );//Se crea el documento a traves del archivo
            Element rootNode = document.getRootElement();//Se obtiene la raiz 'planta'
            List lista_estancias = rootNode.getChildren( "estancia" );//Se obtiene la lista de hijos de la raiz 'planta'
            String planta = rootNode.getChildText("Z");

            aEstancias = new ArrayList<Estancia>();
            for ( int indexEstancia = 0; indexEstancia < lista_estancias.size(); indexEstancia++ )//Se recorre la lista de 'estancias'
            {
                Element estancia = (Element) lista_estancias.get(indexEstancia);//Se obtiene el elemento 'planta'
                String idEstancia = estancia.getAttributeValue("id");//Se obtiene el atribullo 'id'
                String tipo = estancia.getChildText("tipo");


                Element cuadrantes = estancia.getChild("cuadrantes");//Obtener el elemento 'cuadrantes'
                List lista_cuadrantes = cuadrantes.getChildren(); //Cogemos la lista de hijos de un cuadrante

                Estancia e = new Estancia(tipo, idEstancia);

                for ( int indexCuadrante = 0; indexCuadrante < lista_cuadrantes.size(); indexCuadrante++ ) //Se recorre la lista de 'cuadrantes'
                {
                    Element cuadrante = (Element) lista_cuadrantes.get(indexCuadrante);//Se obtiene el elemento 'cuadrante'
                    String idCuadrante = cuadrante.getAttributeValue("idc");//Se obtiene el atribullo 'idc'

                    Element NW = cuadrante.getChild("NW"); //el elemento NW con sus coordenadas
                    String NWX = NW.getChildText("X");
                    String NWY = NW.getChildText("Y");

                    Element SE = cuadrante.getChild("SE");//el elemento SE con sus coordenadas
                    String SEX = SE.getChildText("X");
                    String SEY = SE.getChildText("Y");

                    Element conectado = cuadrante.getChild("conectado");
                    String norte = conectado.getChildText("norte");
                    String sur = conectado.getChildText("sur");
                    String este = conectado.getChildText("este");
                    String oeste = conectado.getChildText("oeste");

                    Element beacons = cuadrante.getChild("beacons");
                    String immediate = beacons.getChildText("immediate");
                    String near = beacons.getChildText("near");
                    String far = beacons.getChildText("far");

                    String objeto = cuadrante.getChildTextTrim("objeto");

                    ArrayList<String> aO = new ArrayList<String>();
                    aO.add(objeto);
                    Cuadrante c = new Cuadrante(Integer.parseInt(idCuadrante), new Posicion(Double.parseDouble(NWX),
                            Double.parseDouble(NWY)), new Posicion(Double.parseDouble(SEX),Double.parseDouble(SEY)),
                            Integer.parseInt(planta), new String[]{norte,sur,este,oeste},
                            new String[] {immediate, near, far}, aO);
                    aCuadrantes.add(c);
                    e.add(c);

                }

                aEstancias.add(e);

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


    public ArrayList<Estancia> getEstancias(){
        return aEstancias;
    }


}

