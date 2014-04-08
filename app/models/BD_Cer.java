package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class BD_Cer extends play.db.ebean.Model {

	@Id
	public String name;
	public int cerNumeroSerial;
	
	public String cerUriRepositorio;
	public String cerUriCrl;
	
	public String cerAIA;
	
	public String cerSIA;
	
	public String cerUriManifiesto;
	public String cerEmisor;
	public String cerSujeto;
	public String cerUriPadre;
	public Date cerFechaInicio;
	public Date cerFechaFin;
	public Date cerFechaBorrado;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<BD_Cer_Recurso> cerRecursos = new ArrayList<BD_Cer_Recurso>();
	
	//Cambio por pruebas de Historico
	@OneToMany(cascade = CascadeType.PERSIST)
	public List<BD_Cer_His> cerHistoric = new ArrayList<BD_Cer_His>();
	
	public boolean cerEsCa;
	public String cerClavePublica;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<BD_Cer_ClaveDeUso> cerUsoDeClaves = new ArrayList<BD_Cer_ClaveDeUso>();
	
	public String cerIdentificadorClavePublica;
	public String cerIdAlgoritmoFirma;
	public String cerNombreAlgoritmoFirma;	
	public String cerFirma;
	public String cerTipo;
	public int cerVersion;
	public boolean cerSoportaExtension;
	public String cerUsoExtendidoDeClave;
	public int cerRestriccionesBasicas;
	
	public static Finder<String,BD_Cer> find = new Finder<String,BD_Cer>(String.class, BD_Cer.class);
	
	
	
}
