package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.CertificateParsingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import models.*;
import net.ripe.commons.certification.cms.manifest.ManifestCms;
import net.ripe.commons.certification.cms.manifest.ManifestCmsParser;
import net.ripe.commons.certification.cms.roa.RoaCms;
import net.ripe.commons.certification.cms.roa.RoaCmsParser;
import net.ripe.commons.certification.cms.roa.RoaPrefix;
import net.ripe.commons.certification.crl.X509Crl;
import net.ripe.commons.certification.crl.X509Crl.Entry;
import net.ripe.commons.certification.x509cert.X509CertificateInformationAccessDescriptor;
import net.ripe.commons.certification.x509cert.X509ResourceCertificate;
import net.ripe.ipresource.IpRange;
import net.ripe.ipresource.IpResource;
import net.ripe.ipresource.IpResourceSet;
import net.ripe.ipresource.Ipv4Address;
import net.ripe.ipresource.Ipv6Address;
import net.ripe.ipresource.UniqueIpResource;

import org.apache.commons.codec.binary.Base64;

import play.db.ebean.Model;

/**
 * 
 * @author
 *
 */
public class ParseRPKI {
	private String directorioAuxiliar;
	
	public ParseRPKI(String directorioAuxiliar){ setDirectorioAuxiliar(directorioAuxiliar);}
	
	public String getDirectorioAuxiliar(){ return directorioAuxiliar;}

	public void setDirectorioAuxiliar(String directorioAuxiliar){ this.directorioAuxiliar = directorioAuxiliar;}
	
	private enum Extensions{ mft, cer, roa, crl;}

	/**
	 * 
	 * @param binario
	 * @param ext
	 * @return
	 */
	public Model fileToParameters(byte[] binario, String ext) {
		// devuelve los datos del rpki object
		
		Extensions extension = Extensions.valueOf(ext);
		try {
			switch(extension) {
		    case roa:
		    	return parseaROA(binario);
		    case mft:
		    	return parseaMFT(binario);
		    case crl:
		    	return parseaCRL(binario);
		    case cer:
		    	return parseaCER(binario);
		    default:
		    	return null;
			}	
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}		
	}
/*		try {
			return parseaROA(binario);
		} catch (Exception e) {
			try {
				return parseaMFT(binario);
				//return parseaCER(binario, retorno);
			} catch (Exception e1) {
				try {
					//return parseaCRL(binario, retorno);
				} catch (Exception e3) {
					try {
						//
					} catch (Exception e4) {
						return null;
					}
				}
			}
		}
		return null;*/

	/**
	 * 
	 * @param binario
	 * @return
	 * @throws Exception
	 */
	private BD_Mft parseaMFT(byte[] binario) throws Exception {
		ManifestCmsParser manifestParser = new ManifestCmsParser();
		manifestParser.parse("", binario);
		// si llega aca es MFT
		BD_Mft mft = new BD_Mft();
		ManifestCms manifest = manifestParser.getManifestCms();
		mft.mftNumber = manifest.getNumber().intValue();
		//retorno.put("notValidBefore", manifest.getNotValidBefore());
		mft.mftNotValidBefore = correctDateFormat(manifest.getNotValidBefore().toString());
		//retorno.put("notValidAfter", manifest.getNotValidAfter());
		mft.mftNotValidAfter = correctDateFormat(manifest.getNotValidAfter().toString());
		//retorno.put("parentURI", manifest.getParentCertificateUri() != null ? manifest.getParentCertificateUri().toString() : "");
		mft.mftParentURI = manifest.getParentCertificateUri();//HAY UNA POSIBILIDAD QUE SEA NULL
		//retorno.put("SigningTime", manifest.getSigningTime());
		mft.mftSigningTime = correctDateFormat(manifest.getSigningTime().toString());
		//retorno.put("thisUpdateTime", manifest.getThisUpdateTime());
		mft.mftThisUpdateTime = correctDateFormat(manifest.getThisUpdateTime().toString());
		//retorno.put("version", manifest.getVersion());
		mft.mftVersion = manifest.getVersion();
		//retorno.put("size", manifest.size());
		mft.mftSize = manifest.size();
		//retorno.put("nextUpdateTime", manifest.getNextUpdateTime());
		mft.mftNextUpdateTime = correctDateFormat(manifest.getNextUpdateTime().toString());
		X509ResourceCertificate x509rc = manifest.getCertificate();
		String url = "";
		if (manifest.getParentCertificateUri() != null) {
			byte[] archi = obtenerByteDesdeRSync(x509rc.getParentCertificateUri().toString());
			BD_Cer cer = parseaCER(archi);
			url = url+cer.cerSIA.split(",")[0];
		}
		Set<java.util.Map.Entry<String, byte[]>> files = manifest.getFiles().entrySet();
		Iterator<java.util.Map.Entry<String, byte[]>> it = files.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry<String, byte[]> elemento = it.next();
			BD_Mft_Files aux = new BD_Mft_Files();
			aux.mftFileName = elemento.getKey();
			aux.mftFileHash = Base64.encodeBase64(elemento.getValue()).toString();
			aux.mftFileUri = new URI(url + elemento.getKey());//NO SÉ SI VA A FUNCAR EN EL CASO QUE EL PARTENT SEA NULL
			//filesManifest[i] = elemento.getKey() + "*" + Base64.encodeBase64(elemento.getValue()) + "*" + url + elemento.getKey();
			//retorno.put("files"+i, filesManifest[i]);
			mft.mftFiles.add(aux);
		}
		//retorno.put("files", filesManifest);

		parsearEE(mft, x509rc);
		return mft;
	}

	/**
	 * 
	 * @param binario
	 * @return
	 */
	private BD_Crl parseaCRL(byte[] binario) {
		X509Crl crl = X509Crl.parseDerEncoded(binario);
		// si llega aca es CRL
		BD_Crl crlO = new BD_Crl();
		//retorno.put("number", crl.getNumber());
		crlO.crlNumber = crl.getNumber().intValue(); 
		//retorno.put("crlURI", crl.getCrlUri() != null ? crl.getCrlUri() : "");
		crlO.crlUri = crl.getCrlUri();//HAY UNA POSIBILIDAD QUE SEA NULL
		//retorno.put("issuer", crl.getIssuer().getName());
		crlO.crlIssuer = crl.getIssuer().getName();
		//retorno.put("nextUpdateTime", crl.getNextUpdateTime().toString());
		crlO.crlnextUpdateTime = correctDateFormat(crl.getNextUpdateTime().toString());
		Set<Entry> revokedcs = crl.getRevokedCertificates();
		Iterator<Entry> it = revokedcs.iterator();
		while (it.hasNext()) {
			Entry e = it.next();
			BD_Crl_Revocado rev = new BD_Crl_Revocado();
			rev.crlSerialRevocacion=e.getSerialNumber().intValue();
			rev.crlFechaRevocacion = correctDateFormat(e.getRevocationDateTime().toString());
			crlO.crlRevocados.add(rev);
		}
		//retorno.put("revokados", revokados);
		//retorno.put("thisUpdateTime", crl.getThisUpdateTime());
		crlO.crlUpdateTime = correctDateFormat(crl.getThisUpdateTime().toString());
		//retorno.put("version", crl.getVersion());
		crlO.crlVersion = crl.getVersion();
		return crlO;
	}

	/**
	 * 
	 * @param binario
	 * @return
	 * @throws IOException
	 * @throws CertificateParsingException
	 * @throws ParseException
	 */
	private BD_Cer parseaCER(byte[] binario) throws IOException, CertificateParsingException, ParseException {
		X509ResourceCertificate cer = X509ResourceCertificate.parseDerEncoded(binario);
		BD_Cer certificado = new BD_Cer();
		// si llega aca es CER
		//retorno.put("tipoArchivo", "CER");
		certificado.cerUriRepositorio =  cer.getRepositoryUri().toString();
		certificado.cerUriCrl =  cer.getCrlUri() != null ? cer.getCrlUri().toString() : "";
		X509CertificateInformationAccessDescriptor[] AIA_aux = cer.getAuthorityInformationAccess();
		String AIA = "";
		if (AIA_aux != null) {
			for (int i = 0; i < AIA_aux.length; i++) {
				AIA = !AIA.equals("") ? AIA.concat(", " + AIA_aux[i].getLocation().toString()) : AIA_aux[i].getLocation().toString();
			}
		}
		certificado.cerAIA = AIA;
		
		X509CertificateInformationAccessDescriptor[] SIA_aux = cer.getSubjectInformationAccess();
		String SIA = "";
		if (SIA_aux != null) {
			for (int i = 0; i < SIA_aux.length; i++) {
				SIA = !SIA.equals("") ? SIA.concat(", " + SIA_aux[i].getLocation().toString()) : SIA_aux[i].getLocation().toString();
			}
		} 
		certificado.cerSIA = SIA;
		
		certificado.cerUriManifiesto = cer.getManifestUri().toString();
		certificado.cerEmisor = cer.getIssuer().getName();
		certificado.cerSujeto = cer.getSubject().getName();
		certificado.name = certificado.cerSujeto.split("=")[1];
		certificado.cerUriPadre = cer.getParentCertificateUri() != null ? cer.getParentCertificateUri().toString() : "";
		certificado.cerNumeroSerial = cer.getSerialNumber().intValue();
		
		certificado.cerFechaInicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(cer.getValidityPeriod().getNotValidBefore().toString().replace("T", " ").replace("Z", ""));
		certificado.cerFechaFin = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(cer.getValidityPeriod().getNotValidAfter().toString().replace("T", " ").replace("Z", ""));
		// List<String> prefijosCER = new ArrayList<String>();
		IpResourceSet CERPrefijos = cer.getResources();
		String[] cerprefixes = CERPrefijos.toString().split(",");
		for (int i = 0; i < cerprefixes.length; i++) {
			BD_Cer_Recurso recurso = new BD_Cer_Recurso();
			if(cerprefixes[i].trim().charAt(0)=='A'){
				recurso.asn=cerprefixes[i];
			}else{
				String[] prefijo = cerprefixes[i].split("/");
				recurso.prefijo= prefijo[0];
				if(prefijo.length>1){
					recurso.largo = Integer.parseInt(prefijo[1]);
				}
				
			}
			certificado.cerRecursos.add(recurso);
		}
		 /*Iterator<IpResource> it = CERPrefijos.iterator();
		 String prefijosConComa = new String();
		 while (it.hasNext()) {
		 prefijosConComa += !prefijosConComa.equals("") ? ", " +
		 it.next().toString() : it.next().toString();
		 }
		 retorno.put("prefijos", prefijosConComa);
		retorno.put("prefijos", cerprefixes);
		 String[] pepe = {"HardCodeado"};
		 retorno.put("prefijos", pepe);*/
		certificado.cerEsCa = cer.isCa();
		certificado.cerClavePublica =  Base64.encodeBase64(cer.getPublicKey().getEncoded()).toString();
		
		boolean[] keyUsage_aux = cer.getCertificate().getKeyUsage();
		BD_Cer_ClaveDeUso claves = new BD_Cer_ClaveDeUso();
		claves.digitalSingature = keyUsage_aux[0];
		claves.nonRepudiation = keyUsage_aux[1];
		claves.keyEncipherment = keyUsage_aux[2];
		claves.dataEncipherment = keyUsage_aux[3];
		claves.keyAgreement = keyUsage_aux[4];
		claves.keyCertSign = keyUsage_aux[5];
		claves.crlSign = keyUsage_aux[6];
		claves.encipherOnly = keyUsage_aux[7];
		claves.decipherOnly = keyUsage_aux[8];
		
		certificado.cerUsoDeClaves.add(claves);

		certificado.cerIdentificadorClavePublica = Base64.encodeBase64(cer.getSubjectKeyIdentifier()).toString();
		certificado.cerIdAlgoritmoFirma = cer.getCertificate().getSigAlgOID();
		certificado.cerNombreAlgoritmoFirma = cer.getCertificate().getSigAlgName();
		certificado.cerFirma = Base64.encodeBase64(cer.getCertificate().getSignature()).toString();
		certificado.cerTipo = cer.getCertificate().getType();
		certificado.cerVersion = cer.getCertificate().getVersion();
		certificado.cerSoportaExtension = cer.getCertificate().hasUnsupportedCriticalExtension();
		
		List<String> extendedKeyUsage = cer.getCertificate().getExtendedKeyUsage();
		String stringExtendedKeyUsage = "";
		if (extendedKeyUsage != null) {
			for (int i = 0; i < extendedKeyUsage.size(); i++) {
				stringExtendedKeyUsage = !stringExtendedKeyUsage.equals("") ? stringExtendedKeyUsage.concat(", " + extendedKeyUsage.get(i)) : extendedKeyUsage.get(i);
			}
		}
		certificado.cerUsoExtendidoDeClave = stringExtendedKeyUsage;
		
		certificado.cerRestriccionesBasicas = cer.getCertificate().getBasicConstraints();
		
		return certificado;
	}

	/**
	 * 
	 * @param binario
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private BD_Roa parseaROA(byte[] binario) throws IOException, ParseException {
		RoaCmsParser roaParser = new RoaCmsParser();
		roaParser.parse("", binario);
		// si llega aca es ROA
		BD_Roa roa = new BD_Roa();
		RoaCms roaCms = roaParser.getRoaCms();

		roa.roaNotValidBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(roaCms.getNotValidBefore().toString().replace("T", " ").replace("Z", ""));		
		roa.roaNotValidAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(roaCms.getNotValidAfter().toString().replace("T", " ").replace("Z", ""));		
		roa.roaAsn = roaCms.getAsn().getValue().intValue();
		
		List<RoaPrefix> listaPrefijos = roaCms.getPrefixes();
		for (int i = 0; i < listaPrefijos.size(); i++) {
			BD_Roa_Statement aux2 = new BD_Roa_Statement();
			aux2.stPrefijoStart=listaPrefijos.get(i).getPrefix().getStart().toString();
			//UniqueIpResource a = listaPrefijos.get(i).getPrefix().getStart();
			aux2.stPrefijoEnd=listaPrefijos.get(i).getPrefix().getEnd().toString();
			//aux2.stPrefijo = listaPrefijos.get(i).getPrefix().toString().split("/")[0];
			//aux2.stLargo = Integer.parseInt(listaPrefijos.get(i).getPrefix().toString().split("/")[1]);
			aux2.largoMaximo = listaPrefijos.get(i).getMaximumLength();
			roa.roaStatements.add(aux2);
		}
		
		roa.roaSigningTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(roaCms.getSigningTime().toString().replace("T", " ").replace("Z", ""));
		roa.roaCrlUri = roaCms.getCrlUri();
		roa.roaParentCertificateURI = roaCms.getParentCertificateUri();
		roa.roaContentType = roaCms.getContentType().toString();
		X509ResourceCertificate x509rc = roaCms.getCertificate();
		parsearEE(roa, x509rc);
		return roa;
	}

	/**
	 * 
	 * @param roa
	 * @param x509rc
	 * @throws IOException
	 * @throws ParseException
	 */
	private void parsearEE(BD_Roa roa, X509ResourceCertificate x509rc) throws IOException, ParseException {
		roa.roaEESerialNumber = x509rc.getSerialNumber().intValue();
		roa.roaEESubjectName =  x509rc.getSubject().getName();
		roa.roaEEIssuerName = x509rc.getIssuer().getName();
		Iterator<IpResource> it= x509rc.getResources().iterator();
		while (it.hasNext()) {
			IpResource ip = it.next();
			BD_Roa_Bloque aux = new BD_Roa_Bloque();
			aux.bloquePrefijoStart = ip.getStart().toString();
			aux.bloquePrefijoEnd = ip.getEnd().toString();
			roa.roabloques.add(aux);
		}
		roa.roaEEPublicKey = Base64.encodeBase64(x509rc.getPublicKey().getEncoded()).toString();
		roa.roaEENotValidBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(x509rc.getValidityPeriod().getNotValidBefore().toString().replace("T", " ").replace("Z", ""));
		roa.roaEENotValidAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(x509rc.getValidityPeriod().getNotValidAfter().toString().replace("T", " ").replace("Z", ""));
		roa.roaEEIsCa = x509rc.isCa();
	}
	
	/**
	 * 
	 * @param mft
	 * @param x509rc
	 * @throws IOException
	 * @throws ParseException
	 */
	private void parsearEE(BD_Mft mft, X509ResourceCertificate x509rc) throws IOException, ParseException {
		mft.mftEESerialNumber = x509rc.getSerialNumber().intValue();
		mft.mftEESubjectName =  x509rc.getSubject().getName();
		mft.mftEEIssuerName = x509rc.getIssuer().getName();
		if(x509rc.getResources().toString().compareTo("INHERITED")==0){
			mft.mftEEPrefijos = true;
		}/*else{
			String []prefijosEE = x509rc.getResources().toString().split(",");
			for (int i = 0; i < prefijosEE.length; i++) {
				BD_Roa_Bloque aux = new BD_Roa_Bloque();
				aux.prefijo = prefijosEE[i].split("/")[0];
				aux.largo = Integer.parseInt(prefijosEE[i].split("/")[1]);
				mft.mftbloques.add(aux);
			}
		}*/	
		mft.mftEEPublicKey = Base64.encodeBase64(x509rc.getPublicKey().getEncoded()).toString();
		mft.mftEENotValidBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(x509rc.getValidityPeriod().getNotValidBefore().toString().replace("T", " ").replace("Z", ""));
		mft.mftEENotValidAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(x509rc.getValidityPeriod().getNotValidAfter().toString().replace("T", " ").replace("Z", ""));
		mft.mftEEIsCa = x509rc.isCa();
	}

	
	/**
	 * Desde el Manifiesto busca la ruta del certificado del padre
	 * @param rutaArchivoRSYnc: Ubicación archivo rsync
	 * @return archivo en byte[] para ser procesado
	 * @throws Exception
	 */
	public byte[] obtenerByteDesdeRSync(String rutaArchivoRSYnc) throws Exception {
		if (!rutaArchivoRSYnc.contains(" ") || rutaArchivoRSYnc.startsWith("rsync")) {

			String[] splitBarra = rutaArchivoRSYnc.split("\\/");
			String rutaOrigen = splitBarra[splitBarra.length - 2]+"/"+splitBarra[splitBarra.length - 1];
			//TODO If para lacnic
			if(splitBarra[splitBarra.length - 2].compareTo("lacnic")!=0){
				String carpetaVisor = getDirectorioAuxiliar();
				rutaOrigen = carpetaVisor.concat(rutaOrigen);
			}

			//comando = comando.concat(rutaArchivoRSYnc + " /cygdrive/c" + rutaDestino.split(":")[1]);
			//Runtime.getRuntime().exec("cmd /c mkdir " + carpetaVisor); // crea carpeta  ---Esto hasta

			//Process pro = Runtime.getRuntime().exec(comando);
			//pro.getOutputStream();
			//pro.waitFor();           // ac�------
			// Thread.sleep(4000);
			byte[] bytesFromFile = FileUtils.getBytesFromFile(new File(rutaOrigen));
			//cleanDirectory(carpetaVisor);
			return bytesFromFile;
		} else
			return null;
	}

	/**
	 * 
	 * @param rutaRSync
	 * @return
	 * @throws Exception
	 */
	public List<String> obtenerListaDirectoriosArchivos(String rutaRSync) throws Exception {
		if (!rutaRSync.contains(" ") || rutaRSync.startsWith("rsync")) {
			List<String> listaRetorno = new ArrayList<String>();
			String comando = "rsync -rv4 ";
			if (!rutaRSync.endsWith("/")) {
				rutaRSync = rutaRSync.concat("/");
			}
			comando = comando.concat(rutaRSync);
			String carpetaVisor = getDirectorioAuxiliar();
			String carpeta = carpetaVisor.concat(UUID.randomUUID().toString());
			comando = comando.concat(" " + carpeta);
			Runtime.getRuntime().exec("mkdir " + carpetaVisor); // crea carpeta
																// visor
			Process p = Runtime.getRuntime().exec(comando);
			// Thread.sleep(7000);
			p.waitFor();
			String line;
			boolean primeraLinea = true;
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = in.readLine()) != null) {
				if (!primeraLinea) {
					if (line.endsWith("./"))
						listaRetorno.add(rutaRSync);
					else if (!line.equals("") && (line.endsWith(".roa") || line.endsWith(".cer") || line.endsWith(".crl") || line.endsWith(".mft") || line.endsWith("/")))
						listaRetorno.add(rutaRSync.concat(line));
				} else {
					primeraLinea = false;
				}
				System.out.println(line);
			}
			in.close();
			//cleanDirectory(carpetaVisor);
			return listaRetorno;
		} else
			return null;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private Date correctDateFormat (String date){
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").parse(date.replace("T", " ").replace("Z", ""));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param directorio
	 * @param models
	 * @throws Exception
	 */
	public void cargarFiles(String directorio, Map<String,Model> models) throws Exception {
		File f = new File(directorio);
		if (f.exists()) {

			File[] ficheros = f.listFiles();
			for (int x = 0; x < ficheros.length; x++) {

				if (ficheros[x].isDirectory()) {
					cargarFiles(ficheros[x], models);
				}else{
					String name = ficheros[x].getName();
					String ext = name.substring(name.lastIndexOf('.'),name.length()).replaceFirst(".", "");
					Model model = fileToParameters(FileUtils.getBytesFromFile(ficheros[x]),ext);
					if(model==null){
						System.out.println(name);
					}else{
						models.put(name,model);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param f
	 * @param models
	 * @throws Exception
	 */
	public void cargarFiles(File f, Map<String,Model> models) throws Exception {
		File[] ficheros = f.listFiles();
		for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				cargarFiles(ficheros[x], models);
			}else{
				String name = ficheros[x].getName();
				String ext = name.substring(name.lastIndexOf('.'),name.length()).replaceFirst(".", "");
				Model model = fileToParameters(FileUtils.getBytesFromFile(ficheros[x]),ext);
				if(model==null){
					System.out.println(name);
				}else{
					models.put(name,model);
				}

			}
		}

	}
}
