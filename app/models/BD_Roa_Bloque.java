package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;


@Entity
public class BD_Roa_Bloque extends Model {

	@Id
	@GeneratedValue //Auto-Increment
	public int id_bloque;
	
	
	public String prefijo;
	
	@Required
	@MaxLength(2) //Ejemplo para maximo dos caracteres
	public int largo;

}
