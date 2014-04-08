package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import net.ripe.ipresource.Ipv4Address;

import com.avaje.ebean.Ebean;

import play.Logger;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.Array;

import models.*;

/**
 * 
 * @author 
 *
 */
public class Manager {
	
	private enum Extensions{ mft, cer, roa, crl;}	
	/**
	 * 
	 * @return
	 */
	public static void carga() {
		File file = new File("lacnic/");
			if(file.list().length==0){
				ParseRPKI parseRPKI = new ParseRPKI("lacnic/");
				try {
					String comando = "rsync -a rsync://repository.lacnic.net/rpki/lacnic/ lacnic/";
					Process p =Runtime.getRuntime().exec(comando);
					//Thread.sleep(60000);
					p.waitFor();
					Logger.info("Repo completo...");
					Map<String,Model> models = new HashMap<String,Model>();
					//byte[] rtaBinario = FileUtils.getBytesFromFile(new File("prueba/dd2304d9768ee76b19c75acb8f82f428b62b14a1.roa"));
					//byte[] rtaBinario2 = FileUtils.getBytesFromFile(new File("prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.mft"));
					//byte[] rtaBinario = FileUtils.getBytesFromFile(new File("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/17e8f6b46f37259605d95989456283ba62e2bdfb.crl"));
					//byte[] rtaBinario = FileUtils.getBytesFromFile(new File("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.cer"));
					//return parseRPKI.fileToParameters(rtaBinario,"cer");
					parseRPKI.cargarFiles("lacnic/",models);
					//parseRPKI.fileToParameters(rtaBinario, "roa");
					for (Map.Entry<String, Model> entry : models.entrySet()) {
						try {
							entry.getValue().save();
						} catch (Exception e) {
							// TODO: handle exception
							Logger.info(""+e);
						}
			    		
			    		
			    	    // ...
			    	}
				} catch (Exception e) {
					Logger.info("FALLA");
				}
			}
	}
	
	public static void obtenerListaDirectoriosArchivos() throws Exception {
		List<String> listaBorrados = new ArrayList<String>();
		List<String> listaModificados = new ArrayList<String>();
		String comando = "rsync -avc --delete rsync://repository.lacnic.net/rpki/lacnic/ lacnic/";
		Process p = Runtime.getRuntime().exec(comando);
		// Thread.sleep(7000);
		p.waitFor();
		String line;
		boolean primeraLinea = true;
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = in.readLine()) != null) {
			if (!primeraLinea) {
				//if (!line.equals("") && (line.endsWith(".roa") || line.endsWith(".cer") || line.endsWith(".crl") || line.endsWith(".mft") || line.endsWith("/")))
				if (!line.equals("") && line.endsWith(".roa")){
					if(line.startsWith("deleting")){
						listaBorrados.add(line.split(" ")[1]);
					}else{
						listaModificados.add(line);
					}
				}
					
			} else {
				primeraLinea = false;
			}
		}
		in.close();
		procesarBorrado(listaBorrados);
		procesarModificados(listaModificados);
	}

	public static void procesarBorrado(List<String> borrados){
		for (String string : borrados) {
			Logger.info(string);
			String [] split = string.split("\\.");
			System.out.println(split[0]);
			System.out.println(split[1]);
			Extensions extension = Extensions.valueOf(split[1]);
			switch(extension) {
			    case roa:{
			    	BD_Roa roa = Ebean.find(BD_Roa.class)
			    			.where().eq("roaEESubjectName","CN="+split[0].split("/")[1])  
			    	        .findUnique();
			    	System.out.println("Sale: "+roa.roaEESerialNumber);
			    	roa.roaFechaBorrado = new Date();
			    	roa.update();
			    }
			    case mft:
			    	//return parseaMFT(binario);
			    case crl:
			    	//return parseaCRL(binario);
			    case cer:
			    	//return parseaCER(binario);
			    default:
			    	//return null;
				System.out.println("Borrado: "+string);
			}
		}
	}

	public static void procesarModificados(List<String> modificados){
		for (String string : modificados) {
			System.out.println("Modificado: "+string);
		}	
	}

}
