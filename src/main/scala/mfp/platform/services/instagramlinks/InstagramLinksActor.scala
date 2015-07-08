package mfp.platform.services.instagramlinks

import akka.actor.{ActorRef, Actor, ActorLogging}
import mfp.platform.services.{ServiceRequest, UIDFactory}
import mfp.platform.services.metrics.ActorMetrics


class InstagramLinksActor(uidGenerator: UIDFactory,
                          dbOperations: InstagramLinksDbOperations,
                          httpClient: ActorRef) extends Actor with ActorLogging with ActorMetrics {

  //instagram-links
  private[this] var instagramLinks: Option[List[InstagramLink]] = None

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

/*object InstagramLinksActor {

  import java.util.concurrent.atomic.AtomicInteger
  import akka.actor.{Props, ActorRefFactory}

  private[this] val id = new AtomicInteger

  case class InstagramLinkActorRequest(req: ServiceRequest)

  //private message exchange types
  private[instagramlinks] case class

  private[instagramlinks] trait

  private[instagramlinks] def props(uidGenerator: UIDFactory,
                                    dbOperations: InstagramLinksDbOperations,
                                    //newsFeedEndpoint: String,
                                    httpClient: ActorRef) =
    Props(classOf[InstagramLinksActor], dbOperations, /*newsFeedEndpoint,*/ httpClient)

  private[instagramlinks] def actorOf(uidGenerator: UIDFactory,
                                    dbOperations: InstagramLinksDbOperations,
                                    //newsFeedEndpoint: String,
                                    httpClient: ActorRef)(implicit actorRefFactory: ActorRefFactory) =
    actorRefFactory.actorOf(props(uidGenerator, dbOperations, /*newsFeedEndpoint,*/ httpClient), name)

  private[instagramlinks] def name = s"ig-links-actor-${id.incrementAndGet()}"
}*/