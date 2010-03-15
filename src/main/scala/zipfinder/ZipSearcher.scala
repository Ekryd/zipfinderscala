package zipfinder

import java.io.IOException
import java.util._
import java.util.regex.Pattern
import java.util.zip._

import zipfinder.logger.StatusLogger

class ZipSearcher(str: String) {
  private val pattern = Pattern.compile("\\S*" + str.trim.replaceAll("\\W", ".") + "\\S*")
  private var statusLogger: StatusLogger = _

  def findEntries(entries: ZipFileEntries): Array[String] = {
    var zipEntries: Enumeration[_ <: ZipEntry] = null
    try {
      zipEntries = entries.getEntries
    } catch {
      case e: ZipException => {
        statusLogger.logError("ZipFinder Error, ZipFile: " + entries.file.getAbsoluteFile + " "
                + e.getMessage)
        e.printStackTrace
        return new Array[String](0)
      }
      case e: IOException => {
        statusLogger.logError("ZipFinder Error, ZipFile: " + entries.file.getAbsoluteFile + " "
                + e.getMessage)
        e.printStackTrace
        return new Array[String](0)
      }
    }
    val fileNames = new ArrayList[String]
    while (zipEntries.hasMoreElements) {
      val entry = zipEntries.nextElement
      val fileName = entry.getName
      if (isFileMatch(fileName)) {
        fileNames.add(fileName)
      }
    }
    fileNames.toArray(new Array[String](fileNames.size))
  }

  private def isFileMatch(fileName: String) = pattern.matcher(fileName).matches

  def setStatusLogger(statusLogger: StatusLogger) {
    this.statusLogger = statusLogger
  }
}
