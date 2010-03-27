package zipfinder.actor

import java.io.File
import actors.Actor

/* Alla meddelanden som skickas i applikationen */
trait ApplicationActor extends Actor {
  case class Search(directory: File)
  case class Stop()
  case class Done()
  case class StartSearch(directory: String, stringToFind: String)
  case class LogError(msg: String)
  case class LogFoundFile(file: File, classNames: List[String])
  case class LogFoundZipFile()
  case class LogEndSearch(nrOfFiles: Int)

  /**Finns det ett stopmeddelande i actorkön */
  protected def stopInQueue = mailbox.foldLeft(false) {(found, msg) => (found || msg == Stop)}
}
