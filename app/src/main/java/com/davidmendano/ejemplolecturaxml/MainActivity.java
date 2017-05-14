package com.davidmendano.ejemplolecturaxml;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    public static Context contexto;
    public static final String DIRECTORIO_ENTRADA = "/RPR/Input/";
    public static final String DIRECTORIO_INTERNO_APP = "/Android/data/com.davidmendano.ejemplolecturaxml/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = this;

        String direccionArchivosEntrada = Environment.getExternalStoragePublicDirectory
                                            (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;
        String direccionMemInternaApp = Environment.getExternalStorageDirectory() + DIRECTORIO_INTERNO_APP;

        try{//Listamos y movemos los ficheros del archivo de entrada
            Vector<File> lista = listarArchivos(direccionArchivosEntrada);
            if(lista.size() > 0) {
                for(int i=0; i<lista.size(); i++) {
                    moverArchivo(lista.get(i), direccionMemInternaApp);
                }
            }
        } catch (Exception e){
            print("Error al mover ficheros");
        }
        try{ // Leemos los archivos XML
            Vector<File> archivosALeer = listarArchivos(direccionMemInternaApp);
            int tamañoVectorLectura = archivosALeer.size();
            if(tamañoVectorLectura > 0){
                for(int i=0; i<tamañoVectorLectura; i++) {
                    leerXML(archivosALeer.get(i));
                }
            }
        } catch (Exception e){
            print("Error al leer ficheros");
        }
        try{ // Escribimos en archivo txt
            File directorioDestino = new File(direccionMemInternaApp);
            if(!directorioDestino.exists()) {
                directorioDestino.mkdirs();
            }
            File archivoDestino = new File(direccionMemInternaApp, "prueba.txt");
            escribirTXT(archivoDestino);
        } catch (Exception e){
            print("Error al escribir ficheros");
        }
    }

    /*
     * Método para listar los archivos de una ruta determinada pasada por parametro.
     * Se pasa la lista a un vector para facilitar su posterior gestión.
     */
    public Vector<File> listarArchivos(String ruta) throws Exception{
        Vector<File> listaArchivos = new Vector<File>();
        //Definimos la ruta donde buscaremos los ficheros
        File f = new File(ruta);
        //Creamos el array de tipo File con el contenido de la carpeta
        File[] archivos = f.listFiles();
        // Si hay nuevos archivos en el directorio de entrada los gestionamos
        if (archivos != null) {
            //Hacemos un Loop por cada fichero para extraer el nombre de cada uno
            for (int i = 0; i < archivos.length; i++) {
                //Sacamos del array files un fichero
                File file = archivos[i];
                //Si es fichero (no es directorio)...
                if (file.isFile()) {
                    String nombreFichero = file.getName();
                    if (esXML(nombreFichero)) {
                        listaArchivos.add(file);
                    }
                }
            }
        } else throw new NullPointerException();

        return listaArchivos;
    }

    public boolean esXML(String nombre){
        boolean esXML = false;
        String extension = nombre.substring(nombre.lastIndexOf("."));

        if (extension.equals(".xml")){
            esXML = true;
        }

        return esXML;
    }


    /*
     * Método que moverá el archivo recibido por parametro al directorio de destino recibido por parametro
     */
    public void moverArchivo(File archivoOrigen, String rutaDestino) throws Exception{
        //String directorioOrigen = archivoOrigen.getAbsolutePath();
        String rutaNuevoArchivo = rutaDestino + archivoOrigen.getName();
        File f = new File(rutaDestino);
        File nuevoArchivo = new File(rutaNuevoArchivo);

        if (!f.exists()){
            f.mkdirs();
        }
        archivoOrigen.renameTo(nuevoArchivo);

    }

    public void leerXML(File archivoALeer) throws Exception {
        //File archivo = new File(archivoALeer.getPath(), archivoALeer.getName());

        InputSource entrada = new InputSource(
                            new InputStreamReader(
                                new FileInputStream(archivoALeer)));

        SAXParserFactory fabrica = SAXParserFactory.newInstance();
        SAXParser parser = fabrica.newSAXParser();
        XMLReader lector = parser.getXMLReader();
        ManejadorXML manejadorXML = new ManejadorXML();
        lector.setContentHandler(manejadorXML);
        lector.parse(entrada);
        //cargadaLista = true;
    }

    public static void escribirTXT(File archivoDestino) {
        try
        {
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(archivoDestino));

            fout.write("Texto de prueba.");
            fout.close();
        }
        catch (Exception e)
        {
            print(e.toString() + ": Error al escribir fichero a tarjeta SD");
        }
    }

    public static void print(String texto) {
        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show();
    }

}