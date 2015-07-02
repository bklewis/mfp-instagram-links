package mfp.platform.services.instagramlinks


import akka.actor.ActorRef
import mfp.platform.services.UIDFactory

trait InstagramLinksDependencies {

  this: Databases =>

  import mfp.platform.services.publisher.Publisher

  def httpClient: ActorRef

  def dbActor: ActorRef

  def IgLinksActor: ActorRef

  def IgLinksDbOps: InstagramLinksDbOperations

  val uidGenerator = new UIDFactory()

  implicit def eventPublisher: Publisher

}
