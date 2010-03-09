package zipfinder

import actors._
import actors.Actor._
import java.io._
import collection.mutable.Queue
import zipfinder.logger._

/** Search in this file */
case class Search(file:File)
/** Request a direct stop */
case class Stop()
/** Finish all searches and then stop */
case class Done()

class ZipSearcherActor(stringToFind:String, statusLogger:StatusLogger) extends Actor {
  /** perform the actual search in file */
  private case class Process()

  private var nrOfFiles = 0
  private val queue = new Queue[File]
  private var processing = false

	def printFileInfo(zipFile:File, names:Array[String]) {
		statusLogger.logFilesFound(zipFile.getAbsolutePath)
		for (name <- names) {
			val stringBuffer = new StringBuffer
			stringBuffer.append("  ").append(name)
			statusLogger.logFilesFound(stringBuffer.toString)
		}
	}

  def addFile(file:File) {
    nrOfFiles += 1
	queue += file
	if (!processing) {
	  processing = true
	  this ! Process
	}
  }

  def processFirstFile() {
	if (!queue.isEmpty) {
	  val mainActor = this
      val zipFile = queue.dequeue

		val searcher = new ZipSearcher(stringToFind)
		searcher.setStatusLogger(statusLogger)
		val names = searcher.findEntries(new ZipFileEntries(zipFile))
		if (names.length != 0) {
			printFileInfo(zipFile, names)
		}
		mainActor ! Process

	} else {
		processing = false
	}
  }

 def act() {
    var run = true
	loopWhile(run) {
	  react {
	    case Search(file) => {
	      println("search")
	      addFile(file)
        }
	    case Stop => {
	      println("stop")
	      run = false
	      statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
        }
	    case Done => {
	      println("done")
	    	if (mailboxSize != 0)
	    		this ! Done
            else
            	this ! Stop
	    }
	    case Process => {
	      println("process")
	      processFirstFile
	    }
     }
	}
  }
}

