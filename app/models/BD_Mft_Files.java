package models;

import javax.persistence.*;

import play.db.ebean.Model;


@Entity
public class BD_Mft_Files extends Model {

	@Id
	@GeneratedValue
	public int id_mftFile;
	
	
	public String mftFileName;
	public String mftFileHash;
}
