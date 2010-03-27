package zipfinder.logger

import java.io.File

trait StatusLogger {
  def logError(msg: String)

  def logFoundFile(file: File, classNames: List[String])

  def logFoundZipFile

  def logEndSearch(nrOfFiles: Int)
}
