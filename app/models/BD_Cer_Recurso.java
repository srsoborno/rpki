package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


@Entity
public class BD_Cer_Recurso extends Model {

	@Id
	@GeneratedValue //Auto-Increment
	public int id_bloque;
	
	public String asn;
	
	public String prefijo;
	
	public int largo;
	
	
	public static Finder<Integer,BD_Cer_Recurso> find = new Finder<Integer,BD_Cer_Recurso>(Integer.class, BD_Cer_Recurso.class);

}
