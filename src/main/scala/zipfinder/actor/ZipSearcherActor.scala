package zipfinder.actor

import actors._
import actors.Actor._
import java.io._
import collection.mutable.Queue
import zipfinder.logger._
import zipfinder.gui._
import zipfinder._

/** Letar efter klassfiler i en jarfil */
class ZipSearcherActor(statusLogger: StatusLogger, stringToFind: String) extends ApplicationActor {
  private var nrOfFiles = 0

  /** Leta i en fil */
  private def processFile(file: File) {
    //    println("ZipSearch process")
    nrOfFiles += 1
    val searcher = new ZipSearcher(stringToFind)
    searcher.setStatusLogger(statusLogger)
    val names = searcher.findEntries(new ZipFileEntries(file))
    if (names.size != 0) {
      statusLogger.logFoundFile(file, names)
    }
  }


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
          stopNow
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stopNow {
    //    println("stop")
    statusLogger.logEndSearch(nrOfFiles)
    exit
  }

}

