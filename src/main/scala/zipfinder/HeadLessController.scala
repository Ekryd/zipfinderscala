package zipfinder

import zipfinder.logger.StandardStatusLogger
import zipfinder.logger.StatusLogger

class HeadLessController(directory: String, stringToFind: String) {
  private var statusLogger: StatusLogger = _
  //  private val foundFilesQueue = new FoundFilesQueue

  def start {
    //    setStatusLogger(StandardStatusLogger)
    findClassesInArchives
  }

  private def createFileFinderThread = {
    //    val zipSearcherActor = new ZipSearcherActor(stringToFind, statusLogger)
    //    val fileFinder = new FileFinder(directory, statusLogger, zipSearcherActor)
    //    new Thread(fileFinder)
  }

  //	private def createZipSearcherThread = {
  //		val zipSearcherRunner = new ZipSearcherRunner(foundFilesQueue, stringToFind)
  //		zipSearcherRunner.setStatusLogger(statusLogger)
  //		new Thread(zipSearcherRunner)
  //	}

  private def findClassesInArchives {

    val fileFinderThread = createFileFinderThread
    //		val zipSearcherThread = createZipSearcherThread
    //    fileFinderThread.start
    //    //		zipSearcherThread.start
    //    try {
    //      fileFinderThread.join
    //    } catch {
    //      case e: InterruptedException => statusLogger.logError(e.getMessage)
    //    }
    //		try {
    //			zipSearcherThread.join
    //		} catch {
    //		  case e:InterruptedException => statusLogger.logError(e.getMessage)
    //		}
    null
  }

  private def setStatusLogger(statusLogger: StatusLogger) {
    this.statusLogger = statusLogger
  }
}
