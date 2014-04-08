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
public class BD_Roa_His extends Model {
	
	@Id
	public int id_roa;
	
	public Date roaFechaCambio;
	
	public String roaValorAnterior;
	
	public String roaValorActual;
	
	public String roaTipoCambio;
	
//	public enum TIPO {
//        NEW,
//        ACTIVE,
//        INACTIVE,
//    }
//	
//
//	@ElementCollection
//    @Enumerated(EnumType.STRING)
//	@CollectionTable(name = "roa_tipos", joinColumns = @JoinColumn(name = "id_cambio", referencedColumnName = "tipoCambio"))
//    public List<TIPO> tipos;
}
