import com.typesafe.sbt.SbtAspectj._
import sbt._
import Keys._
import spray.revolver.RevolverPlugin.Revolver
import xerial.sbt.Pack._

object ServiceBuild extends Build {

  import Versions._

  lazy val rootSettings: Seq[Setting[_]] = Seq(
    organization := "com.myfitnesspal",
    version := "1.0",
    scalaVersion := scalaV,

    scalacOptions in Compile ++= Seq(
      "-unchecked",
      "-feature",
      "-language:postfixOps",
      "-deprecation",
      "-encoding",
      "UTF-8"),

    resolvers ++= AdditionalResolvers.resolvers
  )

  lazy val aspectJSettings = aspectjSettings ++ Seq(
    fork in run := true,
    javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj
  )

  lazy val projSettings = packSettings ++ rootSettings ++ Seq(
    name := "mfp-instagram-links",

    packMain := Map("app" -> "mfp.platform.services.mfpinstagramlinks.ServiceBootstrap"),

    packExtraClasspath := Map("app" -> Seq("${EPHEMERAL_DATA}/conf")),

    packJvmOpts := Map("app" -> Seq("-javaagent:${PROG_HOME}/lib/aspectjweaver-" + s"${aspectjV}.jar")),

    libraryDependencies := Dependencies.main ,

    packGenerateWindowsBatFile := false,

    pack <<= pack map { old =>
      IO.touch(new File(".", ".assembled"), true)
      println("[info] Pack touch file .assembled to restart dev app." )
      old
    }
  )

  // This will create the root project instance, in addition of enabling sbt-revolver
  lazy val root = Project("mfp-instagram-links", file("."))
    .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*) // required by 'sbt-dependency-graph'
    .settings(Revolver.settings ++ aspectJSettings ++ projSettings: _*)
}

object AdditionalResolvers {
  val resolvers = Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases"),
    "spray repo" at "http://repo.spray.io/",
    "mfp repo" at "http://maven-devtools-repo-prod.mfpaws.com:8081/artifactory/maven-releases/"
  )
}
