import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "loyaltyMgmt"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    //"org.jongo" % "jongo" % "0.4",
    //"org.mongodb" % "mongo-java-driver" % "2.11.2"
    "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0",
    "org.json" % "json" % "20090211",
    "org.apache.commons" % "commons-email" % "1.2",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    "com.google.code.gson" % "gson" % "2.2.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Maven repository" at "http://mirrors.ibiblio.org/maven2/",
    resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/",   
    resolvers += "Morphia Repository" at  "http://morphia.googlecode.com/svn/mavenrepo/"
  )
}
