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
public class BD_Mft extends Model {
	
	
	public int mftNumber;
	
	public Boolean mftEEIsCa;
	public Date mftSigningTime;
	@Id
	public String mftEEIssuerName;
	public Date mftEENotValidAfter;
	public Date mftEENotValidBefore;
	public int mftEESerialNumber;
	public Date mftNotValidAfter;
	public String mftEEPublicKey;
	public int mftVersion;
	public int mftSize;
	public Date mftNextUpdateTime;
	public URI mftParentURI;
	public Date mftThisUpdateTime;
	public String mftEESubjectName;
	public Date mftNotValidBefore;
	public Date mftFechaBorrado;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<BD_Mft_Files> mftFiles = new ArrayList<BD_Mft_Files>();
	
	//Cambio por pruebas de Historico
	@OneToMany(cascade = CascadeType.PERSIST)
	public List<BD_Mft_His> mftHistoric = new ArrayList<BD_Mft_His>();
	
	//@OneToMany(cascade = CascadeType.PERSIST)
	//public List<BD_Roa_Bloque> mftbloques = new ArrayList<BD_Roa_Bloque>();
	public boolean mftEEPrefijos;
	
	public static Finder<String,BD_Mft> find = new Finder<String,BD_Mft>(String.class, BD_Mft.class);

}
