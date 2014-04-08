import akka.actor.ActorRef;
import akka.actor.Props;

import play.*;
import play.libs.Akka;
import play.libs.F.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import controllers.Manager;

import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.*;


public class Global extends GlobalSettings {
	
  @Override
  public void onStart(Application app) {
	  Manager m = new Manager();
	  m.carga();
	  ActorRef myActor = Akka.system().actorOf(
		        new Props(Retreiver.class));
	  Akka.system()
      .scheduler()
      .schedule(Duration.create(0, TimeUnit.MILLISECONDS),
              Duration.create(10, TimeUnit.SECONDS), myActor, "TICK",
              Akka.system().dispatcher(),null);

  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}