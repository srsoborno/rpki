package models;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Mft extends Model {
	
	@Id
	public int mftNumber;
	
	public String mftTipoArchivo;
	public Boolean mftEEIsCa;
	public Date mftSigningTime;
	public String mftEEIssuerName;
	public Date mftEENotValidAfter;
	public Date mftEENotValidBefore;
	public int mftEESerialNumber;
	public Date mftNotValidAfter;
	public String mftEEPublicKey;
	public String mftEEPrefijos; //TODO ver si el campo acepta prefijos o solo String ejemplo "INHERITED"
	public int mftVersion;
	public int mftSize;
	public Date mftNextUpdateTime;
	public String mftParentURI;
	public Date mftThisUpdateTime;
	public String mftEESubjectName;
	public Date mftNotValidBefore;
	
	@Required
	@ManyToOne(cascade=CascadeType.ALL)
	//public List<BD_Mft_Files> mftFiles;
	//TODO Aparte de ver el tema de la Lista, ver Por qué el Object Validator es diferente a lo que sale en consola
	public BD_Mft_Files mftFiles = new BD_Mft_Files();
	
	

}
