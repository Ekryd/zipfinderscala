package zipfinder.actor

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter._

class FileFinderActor(statusLogger: StatusLogger, stringToFind: String) extends Actor {
  private val zipSearcherActor = new ZipSearcherActor(statusLogger, stringToFind) {start}


  def act() {
    loop {
      react {
        case Stop => {
          println("StopSearch")
          zipSearcherActor ! Stop
          exit
        }
        case SearchFile(directory) => {
          println("Search")
          if (stopInQueue) {
            println("request Stop")
            zipSearcherActor ! Stop
            exit
          } else {
            searchForFiles(directory)
          }
        }
        case Done => {
          zipSearcherActor ! Done
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stopInQueue = mailbox.foldLeft(false) {(found, msg) => (found || msg == Stop)}

  private def searchForFiles(directory: File) {
    val zipFiles = directory.listFiles(ZipFileFilter)
    if (zipFiles != null) {
      for (file <- zipFiles) {
        statusLogger.logFoundZipFile
        println("request SearchFile")
        zipSearcherActor ! SearchFile(file)
      }
      val directories = directory.listFiles(DirectoryFileFilter)
      for (directory <- directories) {
        println("request Search")
        this ! SearchFile(directory)
      }
    }
    if (mailbox.isEmpty) {
      println("request Done")
      this ! Done
    }
  }

  def search(directory: File) {
    this ! SearchFile(directory)
  }

  def stop() {
    this ! Stop
  }

}
