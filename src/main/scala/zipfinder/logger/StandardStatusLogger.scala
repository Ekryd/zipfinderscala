package zipfinder.logger

import java.io.File

/**Logger som skriver till standard out */
object StandardStatusLogger extends StatusLogger {
  def logError(msg: String) {
    System.err println (msg)
  }

  def logEndSearch(nrOfFiles: Int) {
    System.out println ("Found " + nrOfFiles + " compressed files")
  }

  def logFoundZipFile {}

  def logFoundFile(file: File, classNames: List[String]) {
    val text = new StringBuilder
    text.append(file.getAbsolutePath).append('\n')
    for (name <- classNames) {
      text.append("  ").append(name).append('\n')
    }
    System.out println (text.toString)
  }

}
