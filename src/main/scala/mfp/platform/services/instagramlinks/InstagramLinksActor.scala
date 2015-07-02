package mfp.platform.services.instagramlinks

import akka.actor.{ActorRef, Actor, ActorLogging}
import mfp.platform.services.UIDFactory
import mfp.platform.services.metrics.ActorMetrics


class InstagramLinksActor(uidGenerator: UIDFactory,
                          dbOperations: InstagramLinksDbOperations,
                          endpoint: String,
                          httpClient: ActorRef) extends Actor with ActorLogging with ActorMetrics {

  override def receive = {
    case _ =>
    println("Message received!")
  }

    /*case IgLinksActorRequest(req, id) => {
      import mfp.platform.services.http.HttpClientTypes.HttpClientRequest
      log.info(s"Retreiving instagram link for link id ${id}")

      dbOperations.//dosometinghere

      import mfp.platform.services.Pagination._
      val req = FromRequest(req)
    }
  }*/

}
