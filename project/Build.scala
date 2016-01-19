import sbt.Keys._
import sbt._

object B extends Build {

  lazy val Foo = config("foo").extend(Compile).describedAs("i hope this works")


  lazy val root = (project in file(".")).settings(
    version in Global := "1.2.3"
  )
    .settings(envSettings(Foo): _*)
    .settings(envSettings(Test): _*)


  lazy val envString = settingKey[String]("your message")

  lazy val createEnv = taskKey[Seq[File]]("create env files")

  def envSettings(conf: Configuration) = inConfig(conf) {
    Defaults.configSettings ++
    Seq(
      createEnv := {
        val envFile = sourceManaged.value / "environment" / "Env.scala"

        val content =
          s"""|package environment
              |
              |object Env {
              |  val env = "Errand for a ${envString.value}"
              |}
          """.stripMargin

        IO.write(envFile, content)

        Seq(envFile)
      },
      sourceGenerators += createEnv.taskValue
    )
  }


}