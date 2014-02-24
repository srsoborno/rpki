package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import views.html.helper.form;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Hola Timmy!!"));
    }
    

}
