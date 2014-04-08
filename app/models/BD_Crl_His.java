package models;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.annotation.EnumValue;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class BD_Crl_His extends Model{

	@Id
	public int id_crl;
	
	public Date crlFechaCambio;
	
	public String crlValorAnterior;
	
	public String crlValorActual;
	
	public String crlTipoCambio;
	
}
