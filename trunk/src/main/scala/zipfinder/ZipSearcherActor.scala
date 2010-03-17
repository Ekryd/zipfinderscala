package zipfinder

import actors._
import actors.Actor._
import java.io._
import collection.mutable.Queue
import zipfinder.logger._
import zipfinder.gui.SwingControllerActorAll
import zipfinder.gui.StopSearch


class ZipSearcherActor(statusLogger: StatusLogger, stringToFind: String) extends Actor {
  private var nrOfFiles = 0

  /**Search in this file */
  case class Search(file: File)

  /**Request a direct stop */
  case class Stop()

  /**No more searches */
  case class Done()


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
        case Search(file) => {
          if (stopInQueue) stopNow
          processFile(file)
        }
        case Stop => {
          stopNow
        }
        case Done => {
          println("done")
          statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
          statusLogger.logDone
          exit
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stopNow {
    println("stop")
    statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
    exit
  }

  def search(file: File) {
    this ! Search(file)
  }

  def stop {
    this ! Stop
  }

  def done {
    this ! Done
  }
}

