package models;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Crl extends Model {

	@Id
	public int crlNumber;
	
	public Date crlnextUpdateTime;
	public String crlIssuer;
	public Date crlUpdateTime;
	
//	public List<BD_Crl_Revocado> crlRevocados;
	public BD_Crl_Revocado crlRevocados = new BD_Crl_Revocado();
	
	public URI crlUri;
	public String crlTipoArchivo;
	public int crlVersion;
	
	
}
