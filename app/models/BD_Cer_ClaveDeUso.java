package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


@Entity
public class BD_Cer_ClaveDeUso extends Model {

	@Id
	@GeneratedValue //Auto-Increment
	public int id_bloque;
	
	
	public boolean digitalSingature;
	public boolean nonRepudiation;
	public boolean keyEncipherment;
	public boolean dataEncipherment;
	public boolean keyAgreement;
	public boolean keyCertSign;
	public boolean crlSign;
	public boolean encipherOnly;
	public boolean decipherOnly;
	
	public static Finder<Integer,BD_Cer_ClaveDeUso> find = new Finder<Integer,BD_Cer_ClaveDeUso>(Integer.class, BD_Cer_ClaveDeUso.class);

	
	

}
