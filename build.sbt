organization := "org.scalaj"

name := "scalaj-time"

version := "0.8-SNAPSHOT"

publishMavenStyle := true

crossScalaVersions := Seq("2.10.4", "2.11.8")

scalacOptions <++= scalaVersion map { v =>
  Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
}

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.2",
  "org.joda" % "joda-convert" % "1.2" % "compile"
)

pomExtra := (
  <url>https://github.com/jorgeortiz85/scala-time</url>
  <licenses>
    <license>
      <name>Apache</name>
      <url>http://www.opensource.org/licenses/Apache-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:jorgeortiz85/scala-time.git</url>
    <connection>scm:git:git@github.com:jorgeortiz85/scala-time.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jorgeortiz85</id>
      <name>Jorge Ortiz</name>
      <url>https://github.com/jorgeortiz85</url>
    </developer>
  </developers>
)

publishTo <<= version { v =>
  val nexus = "http://oss.sonatype.org/"
  if (v.endsWith("-SNAPSHOT"))
    Some("snapshots" at nexus+"content/repositories/snapshots")
  else
    Some("releases" at nexus+"service/local/staging/deploy/maven2")
}

credentials ++= {
  val sonatype = ("Sonatype Nexus Repository Manager", "oss.sonatype.org")
  def loadMavenCredentials(file: java.io.File) : Seq[Credentials] = {
    xml.XML.loadFile(file) \ "servers" \ "server" map (s => {
      val host = (s \ "id").text
      val realm = if (host == sonatype._2) sonatype._1 else "Unknown"
      Credentials(realm, host, (s \ "username").text, (s \ "password").text)
    })
  }
  val ivyCredentials   = Path.userHome / ".ivy2" / ".credentials"
  val mavenCredentials = Path.userHome / ".m2"   / "settings.xml"
  (ivyCredentials.asFile, mavenCredentials.asFile) match {
    case (ivy, _) if ivy.canRead => Credentials(ivy) :: Nil
    case (_, mvn) if mvn.canRead => loadMavenCredentials(mvn)
    case _ => Nil
  }
}
