package zipfinder

import actors._
import actors.Actor._
import java.io._
import collection.mutable.Queue
import zipfinder.logger._
import zipfinder.gui.SwingControllerActorAll
import zipfinder.gui.StopSearch

/**Search in this file */
case class SearchFile(file: File)

/**Request a direct stop */
case class Stop()

/**No more searches */
case class Done(actor: SwingControllerActorAll)

class ZipSearcherActor(stringToFind: String, statusLogger: StatusLogger) extends Actor {
  private var nrOfFiles = 0

  private def printFileInfo(zipFile: File, names: Array[String]) {
    statusLogger.logFilesFound(zipFile.getAbsolutePath)
    for (name <- names) {
      val stringBuffer = new StringBuffer
      stringBuffer.append("  ").append(name)
      statusLogger.logFilesFound(stringBuffer.toString)
    }
  }

  private def processFile(file: File) {
    println("process")
    nrOfFiles += 1
    val searcher = new ZipSearcher(stringToFind)
    searcher.setStatusLogger(statusLogger)
    val names = searcher.findEntries(new ZipFileEntries(file))
    if (names.length != 0) {
      printFileInfo(file, names)
    }
  }

  private def stopInQueue = mailbox.foldLeft(false) {(found, msg) => (found || msg == Stop)}

  def act() {
    loop {
      react {
        case SearchFile(file) => {
          if (stopInQueue) stop
          processFile(file)
        }
        case Stop => {
          stop
        }
        case Done(actor) => {
          println("done")
          statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
          actor ! StopSearch
          exit
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stop {
    println("stop")
    statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
    exit
  }
}

