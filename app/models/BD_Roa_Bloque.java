package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


@Entity
public class BD_Roa_Bloque extends Model {

	@Id
	@GeneratedValue //Auto-Increment
	public int id_bloque;
	
	
	public String prefijo;
	
	@Required
	public int largo;
	
	
	public static Finder<Integer,BD_Roa_Bloque> find = new Finder<Integer,BD_Roa_Bloque>(Integer.class, BD_Roa_Bloque.class);

}
