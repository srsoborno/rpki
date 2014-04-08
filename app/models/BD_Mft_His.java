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
public class BD_Mft_His extends Model{

	@Id
	public int id_mft;
	
	public Date mftFechaCambio;
	
	public String mftValorAnterior;
	
	public String mftValorActual;
	
	public String mftTipoCambio;
}
