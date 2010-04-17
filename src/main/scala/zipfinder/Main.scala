package zipfinder

import zipfinder.actor._

/**Startprogram */
object Main {
  def main(args: Array[String]) {
    args.length match {
      case 0 =>
        new SwingControllerActor {start}
      case 1 =>
        ZipFinderPreferences addDirectory args(0)
        new SwingControllerActor {start}
      case 2 =>
        ZipFinderPreferences addDirectory args(0)
        ZipFinderPreferences addStringToFind args(1)
        new HeadLessController(args(0), args(1)) {start}
      case _ =>
        System.out.println("Usage: directory stringToFind")
        System.exit(1)
    }
  }
}
