package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@Entity
public class BD_Roa_Statement extends play.db.ebean.Model {
	
	@Id
	@GeneratedValue
	public int id_statement;

	
	public BD_Roa_Bloque bloque;
	
	@Required
	@Min(0)
	@Max(32) //Ejemplo para marcar minimo y maximo del campo
	public int largoMaximo;
	
}
