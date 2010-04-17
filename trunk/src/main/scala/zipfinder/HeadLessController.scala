package zipfinder

import zipfinder.logger.StandardStatusLogger
import zipfinder.logger.StatusLogger
import zipfinder.actor._
import java.io.File

/**Kontroller som används då inte GUI finns */
class HeadLessController(directory: String, stringToFind: String) {
  private val statusLogger: StatusLogger = StandardStatusLogger

  def start {
          ZipFinderPreferences.addDirectory(directory)
          ZipFinderPreferences.addStringToFind(stringToFind)
          var zipSearcher = new ZipSearcherActor(statusLogger, stringToFind) {start}
          var fileFinder = new FileFinderActor(statusLogger, zipSearcher) {start}
          fileFinder ! Search(new File(directory))
  }
}
