package mfp.platform.services.instagramlinks

import akka.actor.Props
import mfp.platform.db.DbActor
import mfp.platform.services.ActorSystemProvider
import mfp.platform.services.http.HttpClient
import mfp.platform.services.http.HttpClient._


trait DefaultInstagramLinksDependencies extends InstagramLinksDependencies {
  // Notice that we're adding an additional ActorSystemProvider type to the components we can swap
  // during bootstrap, in this case because we need an ActorSystem to create most of the dependencies.
  this: ActorSystemProvider with Databases ⇒

  import mfp.platform.services.metrics.{LoggingMetricsPublisher, ActorMetricsContext}
  import mfp.platform.services.publisher.PublisherBus

  // NOTE: It's extremely important that dependencies declared with 'val' are made 'lazy'.
  // When mixing in traits, some of the symbols might not be initialized yet, and this should
  // guarantee that it happens during bootstrap/assembly of the final application

  override lazy val httpClient = {
    import HttpClient._
    actorOf(None)(actorSystem)
  }

  // TODO: Not working because DefaultInstagramLinksDbOperations & instagramLinksDbOperations are not active
  override lazy val dbActor = actorSystem.actorOf(Props[DbActor])

  //override lazy val instagramLinksDbOperations = new DefaultInstagramLinksDbOperations(dbActor, this, uidGenerator)


  /*override def instagramLinksActor = {
    import InstagramLinksActor._
    actorOf(uidGenerator, instagramLinksDbOperations, newsFeedEndpoint, httpClient)(actorSystem)
  }*/

  //What events does this publish?
  //Should I review the Airbrake library?
  /*implicit lazy val eventPublisher = new PublisherBus() {{
    // Register other event publishers here: Statsd Metrics, Airbrake support, etc...
    import mfp.airbrake.AirbrakePublisher
    val airbrakeActor = AirbrakeActor.actorOf(AirbrakeConfig(config), new SimpleAirbrakeNotifier())(actorSystem)
    register(new AirbrakePublisher(airbrakeActor))
  }}*/

  ActorMetricsContext.register(LoggingMetricsPublisher())
}

