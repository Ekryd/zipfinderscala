package zipfinder.actor

import java.io.File

case class SearchFile(directory: File)
case class Stop()
case class Done()
case class StartSearch(directory: String, stringToFind: String)
case class LogError(msg: String)
case class LogFoundFile(file: File, classNames: List[String])
case class LogFoundZipFile()
case class StopSearch()
case class LogEndSearch(nrOfFiles: Int)

