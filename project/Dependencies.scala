import sbt._

//MVN Repository

object Dependencies {

  import Versions._

  lazy val main =  Seq(
    //"io.spray"            %%  "spray-caching"            % sprayV,
    //"io.spray"            %%  "spray-can"                % sprayV,
    //"io.spray"            %%  "spray-routing"            % sprayV,
    //"io.spray"            %%  "spray-json"               % "1.3.1",
    //"io.spray"            %%  "spray-testkit"            % sprayV     % "test",
    "ch.qos.logback"      %   "logback-classic"          % "1.1.2",
    "com.myfitnesspal"    %%  "core-services-common"     % "1.6.1",
    "com.myfitnesspal"    %%  "core-services-db"         % "1.1.2",
    //"com.myfitnesspal"    %%  "spray-akka-airbrake-support" % "1.6.1",
    "com.typesafe.akka"   %%  "akka-actor"               % akkaV,
    "org.aspectj"         %   "aspectjweaver"            % "1.7.4",
    "com.typesafe.akka"   %%  "akka-testkit"             % akkaV      % "test",
    "com.typesafe.akka"   %%  "akka-slf4j"               % akkaV,
    "org.scalatest"       %%  "scalatest"                % "2.2.2"    % "test",
    "org.slf4j"           %   "log4j-over-slf4j"         % "1.7.7",
    "org.specs2"          %%  "specs2-core"              % "2.3.10"    % "test",
    "mysql"               %   "mysql-connector-java"     % "5.1.30",
    "c3p0"                %   "c3p0"                     % "0.9.1.2",
    "com.typesafe.slick"  %%  "slick"                    % "2.0.2",
    "joda-time"           %   "joda-time"                % "2.8.1"
  )

}

