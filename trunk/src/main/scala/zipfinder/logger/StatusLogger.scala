package zipfinder.logger

import java.io.File

/** Trait för loggning av händelser i systemet */
trait StatusLogger {
  /** Ett fel har uppstått vid läsning av fil */
  def logError(msg: String)

  /** Hittat en jarfil vars innhåll matchar vår sökning */
  def logFoundFile(file: File, classNames: List[String])

  /** Hittat en jarfil */
  def logFoundZipFile

  /** Rapportera hur många filer vi sökte igenom */
  def logEndSearch(nrOfFiles: Int)
}
