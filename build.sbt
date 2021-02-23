import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

val commonSettings = Seq(
  version := "1.3.0",
  crossScalaVersions := Seq("2.12.13", "2.13.4"),
  name := "chelona",
  organization := "com.github.jupfu",
  homepage := Some(new URL("http://github.com/JuPfu/chelona")),
  description := "W3C RDF 1.1 Turtle-, TriG-, Quad- and NTriples-Parser",
  startYear := Some(2014),
  licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  test in assembly := {},
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8",
    "-Xlint:unchecked",
    "-Xlint:deprecation"),
  scalacOptions ++= List(
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xlint",
    "-opt:l:method",
    "-language:_",
    "-target:jvm-1.8"))

val formattingSettings = scalariformSettings(false) ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true))

/////////////////////// PROJECTS /////////////////////////


resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.sonatypeRepo("public")

resolvers += Resolver.typesafeRepo("releases")

parallelExecution in Test := false

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)

lazy val chelona = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(
    libraryDependencies ++=
    Seq(
      "org.parboiled" %%% "parboiled" % "2.2.1",
      "com.chuusai" %%% "shapeless" % "2.3.3",
      "org.scalatest" %%% "scalatest" % "3.2.5" % Test
      )
    )
  .jvmSettings(
    libraryDependencies ++=
    Seq(
      "com.github.scopt" %%  "scopt" % "3.7.1"
    )
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++=
      Seq(
      )
  )
  .settings(commonSettings)
  .settings(scalariformSettings(false))
  .settings(formattingSettings)
  .settings(publishingSettings)

lazy val chelonaJVM = chelona.jvm
lazy val chelonaJS = chelona.js

lazy val root = project.in(file("."))
  .aggregate(chelonaJVM, chelonaJS)
  .settings(commonSettings)

/*
scalatex.SbtPlugin.projectSettings

lazy val readme = scalatex.ScalatexReadme(
  projectId = "readme",
  wd = file(""),
  url = "https://github.com/lihaoyi/scalatex/tree/master",
  source = "Readme"
)*/
/////////////////////// PUBLISH /////////////////////////

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  useGpg := false,
  useGpgAgent := false,
  sonatypeProfileName := "JuPfu",
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <scm>
      <connection>scm:git:github.com/jupfu/chelona</connection>
      <developerConnection>scm:git:git@github.com:jupfu/chelona.git</developerConnection>
      <url>github.com/jupfu/chelona</url>
    </scm>
      <developers>
        <developer>
          <id>JuPfu</id>
          <name>Jürgen Pfundt</name>
          <url>http://github.com/jupfu</url>
        </developer>
      </developers>)
