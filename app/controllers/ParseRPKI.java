package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.ripe.commons.certification.cms.manifest.ManifestCms;
import net.ripe.commons.certification.cms.manifest.ManifestCmsParser;
import net.ripe.commons.certification.cms.roa.RoaCms;
import net.ripe.commons.certification.cms.roa.RoaCmsParser;
import net.ripe.commons.certification.cms.roa.RoaPrefix;
import net.ripe.commons.certification.crl.X509Crl;
import net.ripe.commons.certification.crl.X509Crl.Entry;
import net.ripe.commons.certification.x509cert.X509CertificateInformationAccessDescriptor;
import net.ripe.commons.certification.x509cert.X509ResourceCertificate;
import net.ripe.ipresource.IpResourceSet;

import org.apache.commons.codec.binary.Base64;

public class ParseRPKI {
	private String directorioAuxiliar;

	public ParseRPKI(String directorioAuxiliar) {
		setDirectorioAuxiliar(directorioAuxiliar);
	}

	public Map<String, Object> fileToParameters(byte[] binario) {
		// devuelve los datos del rpki object
		Map<String, Object> retorno = new HashMap<String, Object>();
		try {
			return parseaROA(binario, retorno);
		} catch (Exception e) {
			try {
				return parseaCER(binario, retorno);
			} catch (Exception e1) {
				try {
					return parseaCRL(binario, retorno);
				} catch (Exception e3) {
					try {
						return parseaMFT(binario, retorno);
					} catch (Exception e4) {
						retorno.put("tipoArchivo", "NONE");
					}
				}
			}
		}
		return retorno;
	}

	private Map<String, Object> parseaMFT(byte[] binario, Map<String, Object> retorno) throws Exception {
		ManifestCmsParser manifestParser = new ManifestCmsParser();
		manifestParser.parse("", binario);
		// si llega aca es MFT
		retorno.put("tipoArchivo", "MFT");
		ManifestCms manifest = manifestParser.getManifestCms();
		retorno.put("number", manifest.getNumber());
		retorno.put("notValidBefore", manifest.getNotValidBefore());
		retorno.put("notValidAfter", manifest.getNotValidAfter());
		retorno.put("parentURI", manifest.getParentCertificateUri() != null ? manifest.getParentCertificateUri().toString() : "");
		retorno.put("SigningTime", manifest.getSigningTime());
		retorno.put("thisUpdateTime", manifest.getThisUpdateTime());
		retorno.put("version", manifest.getVersion());
		retorno.put("size", manifest.size());
		retorno.put("nextUpdateTime", manifest.getNextUpdateTime());
		X509ResourceCertificate x509rc = manifest.getCertificate();
		String url = "Vacio";
		if (manifest.getParentCertificateUri() != null) {
			byte[] archi = obtenerByteDesdeRSync(x509rc.getParentCertificateUri().toString());
			Map<String, Object> cer = new HashMap<String, Object>();
			cer = parseaCER(archi, cer);
			url = cer.get("SIA0").toString();
		}
		Set<java.util.Map.Entry<String, byte[]>> files = manifest.getFiles().entrySet();
		String[] filesManifest = new String[files.size()];
		Iterator<java.util.Map.Entry<String, byte[]>> it = files.iterator();
		int i = 0;
		while (it.hasNext()) {
			java.util.Map.Entry<String, byte[]> elemento = it.next();
			filesManifest[i] = elemento.getKey() + "*" + Base64.encodeBase64(elemento.getValue()) + "*" + url + elemento.getKey();
			retorno.put("files"+i, filesManifest[i]);
			i++;
		}
		//retorno.put("files", filesManifest);

		parsearEE(retorno, x509rc);
		return retorno;
	}

	private Map<String, Object> parseaCRL(byte[] binario, Map<String, Object> retorno) {
		X509Crl crl = X509Crl.parseDerEncoded(binario);
		// si llega aca es CRL
		retorno.put("tipoArchivo", "CRL");
		retorno.put("number", crl.getNumber());
		retorno.put("crlURI", crl.getCrlUri() != null ? crl.getCrlUri() : "");
		retorno.put("issuer", crl.getIssuer().getName());
		retorno.put("nextUpdateTime", crl.getNextUpdateTime().toString());
		Set<Entry> revokedcs = crl.getRevokedCertificates();
		Iterator<Entry> it = revokedcs.iterator();
		String[] revokados = new String[revokedcs.size()];
		int i = 0;
		while (it.hasNext()) {
			Entry e = it.next();
			revokados[i] = e.getSerialNumber().toString() + "," + e.getRevocationDateTime();
			retorno.put("revokados"+i, revokados[i]);
			i++;
		}
		//retorno.put("revokados", revokados);
		retorno.put("thisUpdateTime", crl.getThisUpdateTime());
		retorno.put("version", crl.getVersion());
		return retorno;
	}

	private Map<String, Object> parseaCER(byte[] binario, Map<String, Object> retorno) throws IOException, CertificateParsingException {
		X509ResourceCertificate cer = X509ResourceCertificate.parseDerEncoded(binario);
		// si llega aca es CER
		retorno.put("tipoArchivo", "CER");
		retorno.put("repositoryURI", cer.getRepositoryUri().toString());
		retorno.put("crlUri", cer.getCrlUri() != null ? cer.getCrlUri().toString() : "");
		X509CertificateInformationAccessDescriptor[] AIA_aux = cer.getAuthorityInformationAccess();
		if (AIA_aux != null) {
			String[] AIA = new String[AIA_aux.length];
			for (int i = 0; i < AIA_aux.length; i++) {
				AIA[i] = AIA_aux[i].getLocation().toString();
				retorno.put("AIA"+i, AIA[i]);
			}
			//retorno.put("AIA", AIA);
		} else {
			String[] AIA = new String[1];
			AIA[0] = "";
			retorno.put("AIA", AIA[0]);
		}
		X509CertificateInformationAccessDescriptor[] SIA_aux = cer.getSubjectInformationAccess();
		if (SIA_aux != null) {
			String[] SIA = new String[SIA_aux.length];
			for (int i = 0; i < SIA_aux.length; i++) {
				SIA[i] = SIA_aux[i].getLocation().toString();
				retorno.put("SIA"+i, SIA[i]);
			}
			//retorno.put("SIA", SIA);
		} else {
			String[] SIA = new String[1];
			SIA[0] = "";
			retorno.put("SIA", SIA[0]);
		}
		retorno.put("manifestURI", cer.getManifestUri().toString());
		retorno.put("issuerName", cer.getIssuer().getName());
		retorno.put("subjectName", cer.getSubject().getName());
		retorno.put("parentURI", cer.getParentCertificateUri() != null ? cer.getParentCertificateUri().toString() : "");
		retorno.put("serialNumber", cer.getSerialNumber());
		retorno.put("notValidBefore", cer.getValidityPeriod().getNotValidBefore().toString());
		retorno.put("notValidAfter", cer.getValidityPeriod().getNotValidAfter().toString());
		// List<String> prefijosCER = new ArrayList<String>();
		IpResourceSet CERPrefijos = cer.getResources();
		String cerprefixes = CERPrefijos.toString();
		// Iterator<IpResource> it = CERPrefijos.iterator();
		// String prefijosConComa = new String();
		// while (it.hasNext()) {
		// prefijosConComa += !prefijosConComa.equals("") ? ", " +
		// it.next().toString() : it.next().toString();
		// }
		// retorno.put("prefijos", prefijosConComa);
		retorno.put("prefijos", cerprefixes);
		// String[] pepe = {"HardCodeado"};
		// retorno.put("prefijos", pepe);
		retorno.put("isCA", String.valueOf(cer.isCa()));
		retorno.put("publicKey", Base64.encodeBase64(cer.getPublicKey().getEncoded()));
		boolean[] keyUsage_aux = cer.getCertificate().getKeyUsage();
		String[] keyUsage = new String[keyUsage_aux.length];
		for (int i = 0; i < keyUsage_aux.length; i++) {
			keyUsage[i] = String.valueOf(keyUsage_aux[i]);
			retorno.put("keyUsage"+i, keyUsage[i]);
		}
		//retorno.put("keyUsage", keyUsage);

		retorno.put("subjectKeyIdentifier", Base64.encodeBase64(cer.getSubjectKeyIdentifier()));
		retorno.put("signatureAlgorithmId", cer.getCertificate().getSigAlgOID());
		retorno.put("signatureAlgorithmName", cer.getCertificate().getSigAlgName());
		retorno.put("signature", Base64.encodeBase64(cer.getCertificate().getSignature()));
		retorno.put("type", cer.getCertificate().getType());
		retorno.put("version", cer.getCertificate().getVersion());
		retorno.put("hasUnsupportedCriticalExtension", cer.getCertificate().hasUnsupportedCriticalExtension());
		List<String> extendedKeyUsage = cer.getCertificate().getExtendedKeyUsage();
		String stringExtendedKeyUsage = "";
		if (extendedKeyUsage != null) {
			for (int i = 0; i < extendedKeyUsage.size(); i++) {
				stringExtendedKeyUsage = !stringExtendedKeyUsage.equals("") ? stringExtendedKeyUsage.concat(", " + extendedKeyUsage.get(i)) : extendedKeyUsage.get(i);
			}
		}
		retorno.put("extendedKeyUsage", stringExtendedKeyUsage);
		retorno.put("basicConstraints", cer.getCertificate().getBasicConstraints());
		return retorno;
	}

	private Map<String, Object> parseaROA(byte[] binario, Map<String, Object> retorno) throws IOException {
		RoaCmsParser roaParser = new RoaCmsParser();
		roaParser.parse("", binario);
		// si llega aca es ROA
		retorno.put("TipoArchivo", "ROA");
		RoaCms roa = roaParser.getRoaCms();
		retorno.put("NotValidBefore", roa.getNotValidBefore().toString());
		retorno.put("NotValidAfter", roa.getNotValidAfter().toString());
		retorno.put("Asn", roa.getAsn().longValue());
		List<RoaPrefix> listaPrefijos = roa.getPrefixes();
		String[] prefijosConMaximo = new String[listaPrefijos.size()];
		retorno.put("PrefijosCant", listaPrefijos.size());
		for (int i = 0; i < listaPrefijos.size(); i++) {
			prefijosConMaximo[i] = listaPrefijos.get(i).getPrefix().toString() + "_" + listaPrefijos.get(i).getMaximumLength().toString();
			retorno.put("PrefijosConMaximo"+i, prefijosConMaximo[i]);
		}
		retorno.put("SigningTime", roa.getSigningTime().toString());
		retorno.put("CrlUri", roa.getCrlUri().toString());
		retorno.put("ParentCertificateURI", roa.getParentCertificateUri().toString());
		retorno.put("ContentType", roa.getContentType().toString());

		X509ResourceCertificate x509rc = roa.getCertificate();
		parsearEE(retorno, x509rc);

		return retorno;
	}

	private void parsearEE(Map<String, Object> retorno, X509ResourceCertificate x509rc) throws IOException {
		retorno.put("EESerialNumber", x509rc.getSerialNumber());
		retorno.put("EESubjectName", x509rc.getSubject().getName());
		retorno.put("EEIssuerName", x509rc.getIssuer().getName());
		retorno.put("EEPrefijos", x509rc.getResources().toString());
		retorno.put("EEPublicKey", Base64.encodeBase64(x509rc.getPublicKey().getEncoded()));
		retorno.put("EENotValidBefore", x509rc.getValidityPeriod().getNotValidBefore().toString());
		retorno.put("EENotValidAfter", x509rc.getValidityPeriod().getNotValidAfter().toString());
		retorno.put("EEIsCa", String.valueOf(x509rc.isCa()));
	}

	public byte[] obtenerByteDesdeRSync(String rutaArchivoRSYnc) throws Exception {
		if (!rutaArchivoRSYnc.contains(" ") || rutaArchivoRSYnc.startsWith("rsync")) {

			String comando = "rsync -v4 ";
			String[] splitBarra = rutaArchivoRSYnc.split("\\/");
			String nomArchivo = splitBarra[splitBarra.length - 1];
			String carpetaVisor = getDirectorioAuxiliar();
			String rutaDestino = carpetaVisor.concat(nomArchivo);

			comando = comando.concat(rutaArchivoRSYnc + " /cygdrive/c" + rutaDestino.split(":")[1]);
			Runtime.getRuntime().exec("cmd /c mkdir " + carpetaVisor); // crea carpeta  ---Esto hasta

			Process pro = Runtime.getRuntime().exec(comando);
			pro.getOutputStream();
			pro.waitFor();           // ac�------
			// Thread.sleep(4000);
			byte[] bytesFromFile = FileUtils.getBytesFromFile(new File(rutaDestino));
			//cleanDirectory(carpetaVisor);
			return bytesFromFile;
		} else
			return null;
	}

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

	private void cleanDirectory(String directorio) throws Exception {
		File f = new File(directorio);
		if (f.exists()) {

			File[] ficheros = f.listFiles();
			for (int x = 0; x < ficheros.length; x++) {

				if (ficheros[x].isDirectory()) {
					cleanDirectory(ficheros[x]);
				}
				ficheros[x].delete();
			}
		}
	}

	private void cleanDirectory(File f) throws Exception {
		File[] ficheros = f.listFiles();
		for (int x = 0; x < ficheros.length; x++) {

			if (ficheros[x].isDirectory()) {
				cleanDirectory(ficheros[x]);
			}
			ficheros[x].delete();
		}

	}

	public String getDirectorioAuxiliar() {
		return directorioAuxiliar;
	}

	public void setDirectorioAuxiliar(String directorioAuxiliar) {
		this.directorioAuxiliar = directorioAuxiliar;
	}
}
