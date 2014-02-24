package models;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Roa extends play.db.ebean.Model {

	@Id
	public int EESerial;
	
	public Date fechaInicio;
	public Date fechaFin;
	public int asn;
	
	@Required
	@ManyToOne(cascade=CascadeType.ALL)
	//public List<BD_Statement> prefijos;
	public BD_Roa_Statement prefijos = new BD_Roa_Statement();
	
	public Date fechaFirma;
	public URI crlUri;
	public URI cerPadre;
	public String contentType;
	public String EESujeto;
	public String EEEmisor;
	
	@Required
	@ManyToOne(cascade=CascadeType.ALL)
	//	public List<BD_Bloque> EERecursos;
	public BD_Roa_Bloque EERecursos = new BD_Roa_Bloque();
	
	public String EEClavePublica;
	public Date EEfechaInicio;
	public Date EEfechaFin;
	public boolean EEBitCA;
	
	public static Finder<String,BD_Roa> find = new Finder<String,BD_Roa>(String.class, BD_Roa.class);
	
	
}
