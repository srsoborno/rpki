package models;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Roa extends play.db.ebean.Model {

	@Id
	public int roaEESerialNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date roaNotValidBefore;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date roaNotValidAfter;
	
	public int roaAsn;
	
	
	public Date roaSigningTime;
	public URI roaCrlUri;
	public URI roaParentCertificateURI;
	public String roaEESubjectName;
	public String roaEEIssuerName;
	public String roaContentType;
	
	
	public String roaEEPublicKey;
	public Date roaEENotValidBefore;
	public Date roaEENotValidAfter;
	public boolean roaEEIsCa;
	
	public static Finder<Integer,BD_Roa> find = new Finder<Integer,BD_Roa>(Integer.class, BD_Roa.class);
	
	
}
