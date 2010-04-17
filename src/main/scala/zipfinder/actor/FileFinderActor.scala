package zipfinder.actor

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter._

/**
 * Letar upp zip eller jar-filer rekursivt
 */
class FileFinderActor(statusLogger: StatusLogger, zipSearcherActor: ZipSearcherActor) extends ApplicationActor {
  def act() {
    loop {
      react {
        case Stop => {
          //          println("StopSearch")
          zipSearcherActor ! Stop
          exit
        }
        case Search(directory) => {
          //          println("Search")
          if (stopInQueue) {
            //            println("request Stop")
            zipSearcherActor ! Stop
            exit
          } else {
            searchForFiles(directory)
          }
        }
        case Done => {
          zipSearcherActor ! Done
          exit
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  /**Leta efter filer i en katalog rekursivt */
  private def searchForFiles(directory: File) {
    val zipFiles = directory.listFiles(ZipFileFilter)
    if (zipFiles != null) {
      for (file <- zipFiles) {
        statusLogger.logFoundZipFile
        //        println("request SearchFile")
        zipSearcherActor ! Search(file)
      }
      val directories = directory.listFiles(DirectoryFileFilter)
      for (directory <- directories) {
        //        println("request Search")
        this ! Search(directory)
      }
    }
    // Om det inte finns fler filer, så är vi klara
    if (mailbox.isEmpty) {
      //      println("request Done")
      this ! Done
    }
  }

}
