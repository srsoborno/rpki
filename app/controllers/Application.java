package controllers;

import models.BD_roa;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import views.html.helper.form;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Hola Timmy!!"));
    }
    
    public static Result addRoa(){
    	BD_roa roa = Form.form(BD_roa.class).bindFromRequest().get();
    	roa.save();
    	return redirect(routes.Application.index());
    }

}
