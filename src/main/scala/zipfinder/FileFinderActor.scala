package zipfinder.gui

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter.DirectoryFileFilter
import zipfinder.filefilter.ZipFileFilter

class FileFinderActor(statusLogger: StatusLogger, stringToFind: String) extends Actor {
  private val zipSearcherActor = new ZipSearcherActor(statusLogger, stringToFind) {start}

  case class Search(directory: File)
  case class Stop()
  case class Done()

  def act() {
    loop {
      react {
        case Stop => {
          println("StopSearch")
          zipSearcherActor.stop
          exit
        }
        case Search(directory) => {
          println("Search")
          if (stopInQueue) {
            println("request Stop")
            zipSearcherActor.stop
            exit
          } else {
            searchForFiles(directory)
          }
        }
        case Done => {
          zipSearcherActor.done
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
        zipSearcherActor.search(file)
      }
      val directories = directory.listFiles(DirectoryFileFilter)
      for (directory <- directories) {
        println("request Search")
        this ! Search(directory)
      }
    }
    if (mailbox.isEmpty) {
      println("request Done")
      this ! Done
    }
  }

  def search(directory: File) {
    this ! Search(directory)
  }

  def stop() {
    this ! Stop
  }

}
