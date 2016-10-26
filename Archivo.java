/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */
import java.io.*;
public class Archivo
{
	public static String[] leerCadenas(String archivo)
	{
		try{
			BufferedReader x = new BufferedReader(
					   new FileReader(archivo));
			String cadena = "";
			int cont=0;
			while((cadena=x.readLine())!=null){
			      cont++;
			}
			//System.out.println("Numero de lineas: " + cont);
			String [] valores = new String[cont];
			x.close();
			x = new BufferedReader(
			    new FileReader(archivo));
			cont = 0;
			while((cadena=x.readLine())!=null){
			      valores[cont] = cadena;
			      //System.out.println("valores["+cont+"] = " + valores[cont]);
			      cont++;
			}
			x.close();
			return valores;
		}
		catch(IOException exc)
		{
			System.out.println("Imposible abrir el archivo para leer!" + exc);
		}
		return null;
	}
	public static boolean buscaCadena(String cad, String nombreArchivo)
	{
		boolean esta=true;
		try{
		     BufferedReader x = new BufferedReader(
			                new FileReader(nombreArchivo));
		     String cadena="";
		     while((cadena=x.readLine())!=null){
		           if(cadena.equals(cad)){
		               esta=true;
			       break;
			   }
		           else
		               esta=false;
		     }
		     x.close();
		     return esta;
		}
		catch(IOException exc){
			System.out.println("Imposible abrir el archivo para buscar!" + exc);
		}
		return false;
	}
	public static boolean buscaPalabra(String palabra, String nombreArchivo)
	{
		boolean esta=true;
		try{
		     BufferedReader x = new BufferedReader(
			                new FileReader(nombreArchivo));
		     String cadena="";
		     while((cadena=x.readLine())!=null){
		           if(cadena.indexOf(palabra)!=-1){
		               esta=true;
			       break;
			   }
		           else
		               esta=false;
		     }
		     x.close();
		     return esta;
		}
		catch(IOException exc){
			System.out.println("Imposible abrir el archivo para buscar!" + exc);
		}
		return false;
	}
}