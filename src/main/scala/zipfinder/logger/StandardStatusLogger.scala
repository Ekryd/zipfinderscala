package zipfinder.logger

object StandardStatusLogger extends StatusLogger {
  def logError(msg: String) {
    System.err println (msg)
  }

  def logFilesFound(msg: String) {
    System.out println (msg)
  }

  def logFoundZipFile {
  }

}
