package zipfinder

import java.io.IOException
import scala.util.matching.Regex
import java.util.zip._

import zipfinder.logger.StatusLogger

/**Söker efter ett namn i en jarfil */
class ZipSearcher(str: String) {
  private val pattern = ("\\S*" + str.trim.replaceAll("\\W", ".") + "\\S*").r
  private var statusLogger: StatusLogger = _

  def findEntries(entries: ZipFileEntries): List[String] = {
    try {
      val zipEntries = entries.getEntries
      zipEntries.map(_.getName).filter(isFileMatch(_))
    } catch {
      case e: ZipException => {
        statusLogger.logError("ZipFinder Error, ZipFile: " + entries.file.getAbsoluteFile + " "
                + e.getMessage)
        e.printStackTrace
        return List[String]()
      }
      case e: IOException => {
        statusLogger.logError("ZipFinder Error, ZipFile: " + entries.file.getAbsoluteFile + " "
                + e.getMessage)
        e.printStackTrace
        return List[String]()
      }
    }
  }


  private def isFileMatch(fileName: String) = pattern.pattern.matcher(fileName).matches

  def setStatusLogger(statusLogger: StatusLogger) {
    this.statusLogger = statusLogger
  }
}
