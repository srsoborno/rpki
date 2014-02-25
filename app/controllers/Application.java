package controllers;

import java.util.List;

import models.BD_Roa;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import views.html.helper.form;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Hola Timmy!!"));
    }
    
    public static Result addRoa(){
    	BD_Roa entry;
    	entry = Ejemplo.carga();
    	entry.save();
    	return getRoa();
    }
    
    public static Result getRoa(){
    	@SuppressWarnings({ "unchecked", "rawtypes" })
		List <BD_Roa> entries = new Model.Finder(Integer.class, BD_Roa.class).all();
    	return ok(Json.toJson(entries));
    }
    

}
