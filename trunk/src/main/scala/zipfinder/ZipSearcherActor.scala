package zipfinder

import actors._
import actors.Actor._
import java.io._
import collection.mutable.Queue
import zipfinder.logger._

class ZipSearcherActor(stringToFind:String) extends Actor {
  case class Search(file:File)
  case class Stop()
  case class Process()

  private var nrOfFiles = 0
  private var statusLogger:StatusLogger = _
  private var run = true;
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
	  actor {
		val searcher = new ZipSearcher(stringToFind)
		searcher.setStatusLogger(statusLogger)
		val names = searcher.findEntries(new ZipFileEntries(zipFile))
		if (names.length != 0) {
			printFileInfo(zipFile, names)
		}
		mainActor ! Process
	  }
	} else {
		processing = false
	}
  }

 def act() {
	while(run) {
	  receive {
	    case Search(file) => addFile(file)
	    case Stop => run = false
	    case Process => processFirstFile
     }
	}
  }

  def setStatusLogger(statusLogger:StatusLogger) {
	this.statusLogger = statusLogger
  }
}
