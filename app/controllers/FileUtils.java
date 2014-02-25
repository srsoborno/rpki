package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	public static void writeFile(String ruta, String nombre, byte[] archivo) {
		try {
			FileOutputStream file = null;
			File onlyCreateDirectory = null;
			onlyCreateDirectory = new File(ruta);
			onlyCreateDirectory.mkdirs();
			file = new FileOutputStream(ruta + nombre);
			file.write(archivo);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	// Returns the contents of the file in a byte array.
		public static byte[] getBytesFromFile(File file) throws IOException {
		    InputStream is = new FileInputStream(file);

		    // Get the size of the file
		    long length = file.length();

		    // You cannot create an array using a long type.
		    // It needs to be an int type.
		    // Before converting to an int type, check
		    // to ensure that file is not larger than Integer.MAX_VALUE.
		    if (length > Integer.MAX_VALUE) {
		        // File is too large
		    }

		    // Create the byte array to hold the data
		    byte[] bytes = new byte[(int)length];

		    // Read in the bytes
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		        offset += numRead;
		    }

		    // Ensure all the bytes have been read in
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }

		    // Close the input stream and return bytes
		    is.close();
		    
		    return bytes;
		}
		
		public static String readFileAsString(String path, String file) throws java.io.IOException {
			byte[] buffer = new byte[(int) new File(path, file).length()];
			BufferedInputStream f = null;
			try {
				f = new BufferedInputStream(new FileInputStream(new File(path, file).toString()));
				f.read(buffer);
			} finally {
				if (f != null)
					try {
						f.close();
					} catch (IOException ignored) {
					}
			}
			return new String(buffer);
		}

//		public static List<String> getListaArchivosRSyncFromListaFile(File file, String rutaRSync) {
//			BufferedReader entrada;
//			if(!rutaRSync.endsWith("\\/"))
//			{
//				rutaRSync.concat("/");
//			}
//			List<String> listaRetorno = new ArrayList<String>();
//			boolean primeraLinea = true;
//			try {
//				entrada = new BufferedReader(new FileReader(file));
//				String linea;
//				boolean finish = false;
//				while(entrada.ready() && !finish){
//						linea = entrada.readLine();
//						if(!primeraLinea) 
//						{
//								listaRetorno.add(rutaRSync.concat(linea));
//								finish = linea.equals("");
//						}
//						else	//receiving incremental file list
////						{
//							primeraLinea = false;
//						}
//						System.out.println(linea);
//				}
//			}
//			catch (IOException e) {
//			e.printStackTrace();
//			}
//			return listaRetorno;
//		}

}
