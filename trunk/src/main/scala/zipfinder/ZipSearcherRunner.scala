package zipfinder

import java.io.File
import java.util.concurrent.TimeUnit

import zipfinder.logger.StatusLogger

class ZipSearcherRunner(foundFilesQueue:FoundFilesQueue, stringToFind:String) extends Runnable {
	private val TIMEOUT = 100
	private var nrOfFiles = 0
	private var statusLogger:StatusLogger = _

	def printFileInfo(zipFile:File, names:Array[String]) {
		statusLogger.logFilesFound(zipFile.getAbsolutePath)
		for (name <- names) {
			val stringBuffer = new StringBuffer
			stringBuffer.append("  ").append(name)
			statusLogger.logFilesFound(stringBuffer.toString)
		}
	}

	def run {
		while (!(foundFilesQueue.isDone && foundFilesQueue.isEmpty)) {
			val searcher = new ZipSearcher(stringToFind)
			searcher.setStatusLogger(statusLogger)
			var zipFile:File = null
			try {
				zipFile = foundFilesQueue.poll(TIMEOUT, TimeUnit.MILLISECONDS)
			} catch {
			  case e:InterruptedException => // Do nothing
			}
			if (zipFile != null) {
				nrOfFiles += 1
				val names = searcher.findEntries(new ZipFileEntries(zipFile))
				if (names.length != 0) {
					printFileInfo(zipFile, names)
				}
			}
		}
		statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files")
	}

	def setStatusLogger(statusLogger:StatusLogger) {
		this.statusLogger = statusLogger
	}
}
