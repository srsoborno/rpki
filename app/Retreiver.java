import controllers.Manager;
import play.Logger;
import akka.actor.UntypedActor;

public class Retreiver extends UntypedActor {


@Override
public void onReceive(Object message) throws Exception {

	 if("TICK".equals(message)) {
		 Manager m = new Manager();
		 m.obtenerListaDirectoriosArchivos();
     }

}

}