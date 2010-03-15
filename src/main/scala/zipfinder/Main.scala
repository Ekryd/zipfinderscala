package zipfinder

import zipfinder.gui.SwingControllerActorAll

object Main extends Application {
  override def main(args: Array[String]) {
    args.length match {
      case 0 =>
        new SwingControllerActorAll {start}
      case 1 =>
        ZipFinderPreferences addDirectory args(0)
        new SwingControllerActorAll {start}
      case 2 =>
        ZipFinderPreferences addDirectory args(0)
        ZipFinderPreferences addStringToFind args(1)
        new HeadLessController(args(0), args(1)).start
      case _ =>
        System.out.println("Usage: directory stringToFind")
        System.exit(1)
    }
  }
}
