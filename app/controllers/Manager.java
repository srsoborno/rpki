package controllers;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;

import models.*;

public class Manager {

	public static Model carga() {
		try {
			ParseRPKI parseRPKI = new ParseRPKI("../../prueba/");
			byte[] rtaBinario = FileUtils.getBytesFromFile(new File("../../prueba/34bd2b0b76d7e1752cb8e8041fa8e9fb1455e707.roa"));
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.mft");
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/17e8f6b46f37259605d95989456283ba62e2bdfb.crl");
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.cer");
			return parseRPKI.fileToParameters(rtaBinario);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
