package controllers;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;

import play.db.ebean.Model;

import models.*;

public class Ejemplo {

	public static BD_Roa carga() {
		BD_Roa roa = new BD_Roa();
		try {
			ParseRPKI parseRPKI = new ParseRPKI("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/");
			byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/34bd2b0b76d7e1752cb8e8041fa8e9fb1455e707.roa");
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.mft");
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/17e8f6b46f37259605d95989456283ba62e2bdfb.crl");
			//byte[] rtaBinario = parseRPKI.obtenerByteDesdeRSync("C:/Users/AMPLIFACACION/Desktop/Proyecto/workspaceproyecto/prueba/58c5894d660ecb557fb8cd7e41fab2d56a653fd0.cer");
			Map<String, Object> mapList = parseRPKI.fileToParameters(rtaBinario);
			
			roa.roaEESerialNumber = Integer.parseInt(mapList.get("EESerialNumber").toString());
			roa.roaAsn = Integer.parseInt(mapList.get("Asn").toString());
			roa.roaEEIsCa = Boolean.valueOf(mapList.get("EESerialNumber").toString());
			roa.roaSigningTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(mapList.get("SigningTime").toString().replace("T", " ").replace("Z", ""));
			roa.roaEENotValidAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(mapList.get("EENotValidAfter").toString().replace("T", " ").replace("Z", ""));
			roa.roaEENotValidBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(mapList.get("EENotValidBefore").toString().replace("T", " ").replace("Z", ""));
			roa.roaNotValidAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(mapList.get("NotValidAfter").toString().replace("T", " ").replace("Z", ""));
			roa.roaNotValidBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(mapList.get("NotValidBefore").toString().replace("T", " ").replace("Z", ""));
			roa.roaContentType = mapList.get("ContentType").toString();
			roa.roaEEIssuerName = mapList.get("EEIssuerName").toString();
			roa.roaEEPublicKey = mapList.get("EEPublicKey").toString();
			roa.roaEESubjectName = mapList.get("EESubjectName").toString();
			roa.roaParentCertificateURI = URI.create(mapList.get("ParentCertificateURI").toString());
			roa.roaCrlUri = URI.create(mapList.get("CrlUri").toString());
			String []prefijosEE = mapList.get("EEPrefijos").toString().split(",");
			for (int i = 0; i < prefijosEE.length; i++) {
				BD_Roa_Bloque aux = new BD_Roa_Bloque();
				aux.prefijo = prefijosEE[i].split("/")[0];
				aux.largo = Integer.parseInt(prefijosEE[i].split("/")[1]);
				aux.roa = roa;
				aux.save();
			}
			for (int i = 0; i < (Integer)mapList.get("PrefijosCant"); i++) {
				BD_Roa_Statement aux2 = new BD_Roa_Statement();
				
				//aux2.bloque = mapList.get("PrefijosCant").toString().split("/")[0];
				//aux.largo = Integer.parseInt(prefijosEE[i].split("/")[1]);
				//aux.roa = roa;
			}
			
			
			//Iterator it = mapList.entrySet().iterator();
			/*while (it.hasNext()) {
				Map.Entry ent = (Map.Entry) it.next();
				ent.getValue();
				System.out.println(ent.getKey() + " = " + ent.getValue());
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return roa;
	}

}
