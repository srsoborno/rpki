package models;

import javax.persistence.*;

import com.avaje.ebean.Page;

import play.data.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class BD_Roa_Statement extends play.db.ebean.Model {
	
	@Id
	@GeneratedValue
	public int id_statement;

	public String stPrefijoStart;
	public String stPrefijoEnd;
	
	
	@Required
	public int largoMaximo;
	
	@ManyToOne
    public BD_Roa roa;
	
	public static Finder<Integer,BD_Roa_Statement> find = new Finder<Integer,BD_Roa_Statement>(Integer.class, BD_Roa_Statement.class);
	
	public static Page<BD_Roa_Statement> page(int page, int pageSize, String sortBy, String order) {
        return 
            find.where()
                .orderBy(sortBy + " " + order)
                .fetch("roa")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }
    
	
}
