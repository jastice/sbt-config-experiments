import sbt.Keys._
import sbt._

trait InjectionPluginKeys {

  lazy val injectionSourceDirectory = settingKey[File]("directory with content to inject")
  lazy val inject = taskKey[Seq[File]]("create injected files")

  def injectionSettings(conf: Configuration) = inConfig(conf) {
    Defaults.configSettings ++
      Seq(
        injectionSourceDirectory := sourceDirectory.value / "inject",
        inject := {
          IO.copyDirectory(injectionSourceDirectory.value, sourceManaged.value)
          Seq(sourceManaged.value)
        },

        sourceGenerators += inject.taskValue
      )
  }
}

object InjectionPlugin extends AutoPlugin {

  override def requires = plugins.JvmPlugin

  object autoImport extends InjectionPluginKeys

  import autoImport._

  override val projectSettings: Seq[Def.Setting[_]] =
    injectionSettings(Compile)

}