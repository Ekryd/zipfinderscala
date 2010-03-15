package zipfinder

import java.io.File

import zipfinder.filefilter.DirectoryFileFilter
import zipfinder.filefilter.ZipFileFilter
import zipfinder.logger.StatusLogger

class FileFinder(directory: String, statusLogger: StatusLogger, zipSearcherActor: ZipSearcherActor) extends Runnable {
  private val DIRECTORY_FILE_FILTER = DirectoryFileFilter
  private val ZIP_FILE_FILTER = ZipFileFilter
  private val startDirectory: File = new File(directory)
  private var stop = false

  def run {
    zipSearcherActor.start
    try {
      if (!startDirectory.isDirectory) {
        statusLogger.logError(startDirectory + " is not a directory")
      } else {
        searchForFiles(startDirectory)
      }
    } finally {
      println("request done")
      zipSearcherActor ! Done
    }
  }

  def setStop {
    stop = true
  }

  def searchForFiles(directory: File) {
    if (stop) {
      return
    }
    val zipFiles = directory.listFiles(ZIP_FILE_FILTER)
    for (file <- zipFiles) {
      if (stop) {
        return
      }
      statusLogger.logFoundZipFile
      try {
        println("request search")
        zipSearcherActor ! Search(file)
      } catch {
        case e: InterruptedException => statusLogger.logError(e.getMessage)
      }
    }
    val directories = directory.listFiles(DIRECTORY_FILE_FILTER)
    for (directory <- directories) {
      if (stop) {
        return
      }
      searchForFiles(directory)
    }
  }
}
