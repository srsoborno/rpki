package models;

//Tiene Sentido que este en la BD??

import java.util.Date;
import javax.persistence.*;
import play.db.ebean.Model;

@Entity
public class BD_Crl_Revocado extends Model{

	@Id
	@GeneratedValue
	public int id_revocacion;
	
	
	public int crlSerialRevocacion;
	public Date crlFechaRevocacion;
	
}
