package com.davidmendano.ejemplolecturaxml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class ManejadorXML extends DefaultHandler {
    private StringBuilder cadenaWorksheet;

    @Override
    public void startDocument() throws SAXException {
        MainActivity.print("Inicio de documento");
        cadenaWorksheet = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String nombreLocal, String
                    nombreCualif, Attributes atr) throws SAXException {
        switch (nombreCualif){
            case "Worksheet":
                cadenaWorksheet.setLength(0);
                for(int i=0; i<atr.getLength(); i++) {
                    cadenaWorksheet.append(atr.getLocalName(i) + ": "+ atr.getValue(i));
                }
                MainActivity.print("Worksheet: " + cadenaWorksheet);
                break;
            case "Table":
                //MainActivity.print("Table: " + atr.toString());
                break;
            default:
                break;
                //cadenaWorksheet.setLength(0);

        }
    }

    @Override
    public void characters(char ch[], int comienzo, int lon) {
        //cadena.append(ch, comienzo, lon);
    }

    @Override
    public void endElement(String uri, String nombreLocal,
                           String nombreCualif) throws SAXException {
        /*if (nombreLocal.equals("puntos")) {
            puntuacion.puntos = Integer.parseInt(cadena.toString());
        } else if (nombreLocal.equals("nombre")) {
            puntuacion.nombre = cadena.toString();
        } else if (nombreLocal.equals("puntuacion")) {
            listaPuntuaciones.add(puntuacion);
        }*/
    }

    @Override
    public void endDocument() throws SAXException {}
}