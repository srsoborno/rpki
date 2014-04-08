package models;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class BD_Crl extends Model {

	
	public int crlNumber;//Numeros repetidos????
	
	public Date crlnextUpdateTime;
	@Id
	public String crlIssuer;
	public Date crlUpdateTime;
	public URI crlUri;
	public int crlVersion;
	public Date crlFechaBorrado;
	
//	public List<BD_Crl_Revocado> crlRevocados;
//	public BD_Crl_Revocado crlRevocados = new BD_Crl_Revocado();
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<BD_Crl_Revocado> crlRevocados = new ArrayList<BD_Crl_Revocado>();
	
	//Cambio por pruebas de Historico
	@OneToMany(cascade = CascadeType.PERSIST)
	public List<BD_Crl_His> crlHistoric = new ArrayList<BD_Crl_His>();
	
	public static Finder<String,BD_Crl> find = new Finder<String,BD_Crl>(String.class, BD_Crl.class);
	
	
}
