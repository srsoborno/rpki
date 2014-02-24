package models;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Cer extends Model {

	@Id
	public int cer_SerialNumber;
	
	public String cerTipoArchivo;
	public String cerPublicKey;
	public String cerType;
	
	
}
