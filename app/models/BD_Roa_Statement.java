package models;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class BD_Roa_Statement extends play.db.ebean.Model {
	
	@Id
	@GeneratedValue
	public int id_statement;

	public String stPrefijo;
	
	public int stLargo;
	
	
	@Required
	public int largoMaximo;
	
	public static Finder<Integer,BD_Roa_Statement> find = new Finder<Integer,BD_Roa_Statement>(Integer.class, BD_Roa_Statement.class);
	
}
