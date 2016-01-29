import sbt.Keys._
import sbt._
import sbt.dsl._

object B extends Build {

  lazy val Foo = config("foo").describedAs("i hope this works")

  lazy val root = (project in file("."))
    .enablePlugins(InjectionPlugin)

}