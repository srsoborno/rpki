package controllers;

import java.util.Map;

import play.mvc.*;
import play.data.*;
import play.db.ebean.Model;
import static play.data.Form.*;

import views.html.*;

import models.*;

/**
 * Manage the Application
 */
public class Application extends Controller {
    
    /**
     * This result directly redirect to application home.
     */
    public static Result GO_HOME = redirect(
        routes.Application.list(0, "stPrefijoStart", "asc")
    );
    
    /**
     * Handle default path requests
     */
    public static Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of ROA´s
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     */
    public static Result list(int page, String sortBy, String order) {
        return ok(
            list.render(
            	//El segundo parametros '0' indica que no se especifica el paginado
                BD_Roa_Statement.page(page, 0, sortBy, order),
                sortBy, order
            )
        );
    }
    
    /**
     * Create the MapList of Roa's.
     */
//    public static Result create() {
//    	Map<String, Model> mapList = Manager.carga();
//    	for (Map.Entry<String, Model> entry : mapList.entrySet()) {
//    		try {
//    			entry.getValue().save();
//			} catch (Exception e) {
//				flash("success", "Problem saving data in DB");
//				break;
//			}
//    	    // ...
//    	}
//        return GO_HOME;
//    }

}
            
