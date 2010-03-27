package zipfinder.actor

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter._
import zipfinder.gui._

/**Styr interaktionen med gui och andra actors */
class SwingControllerActor extends ApplicationActor with StatusLogger with SearchButtonListener {
  private val swingGui = getSwingGui
  private var running = false
  private var fileFinder: FileFinderActor = _
  private var zipSearcher: ZipSearcherActor = _

  /**Skapa gui */
  def getSwingGui = {
    var recentDirectories = ZipFinderPreferences.getRecentDirectories
    if (recentDirectories.size == 0) {
      recentDirectories = Array("/tmp2")
    }

    val recentStringsToFind = ZipFinderPreferences getRecentStringsToFind
    val gui = new SwingGui(recentDirectories, recentStringsToFind, this) {
      createComponents
      showFrame
    }
    gui
  }

  def act() {
    loop {
      react {
        case StartSearch(directory, stringToFind) => {
          running = true
          swingGui.showWorking
          //          println("StartSearch")
          ZipFinderPreferences.addDirectory(directory)
          ZipFinderPreferences.addStringToFind(stringToFind)
          zipSearcher = new ZipSearcherActor(this, stringToFind) {start}
          fileFinder = new FileFinderActor(this, zipSearcher) {start}
          fileFinder ! Search(new File(directory))
        }
        case LogEndSearch(nrOfFiles) => {
          swingGui.addToConsole(nrOfFiles)
          stop
        }
        case Stop => {
          stop
        }
        case LogError(msg) => {
          swingGui.addToConsole(msg)
        }
        case LogFoundFile(file: File, classNames: List[String]) => {
          if (!stopInQueue) {
            //            println("LogFilesFound")
            swingGui.addToConsole(file, classNames)
          }
        }
        case LogFoundZipFile => {
          if (!stopInQueue) {
            swingGui.showWorking
          }
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stop {
    //    println("StopSearch")
    running = false
    swingGui.showDoneWorking
  }

  /**Om användaren trycker på sök-knappen */
  def performButtonPress(directory: String, stringToFind: String) {
    //    println("performButtonPress")
    if (running) {
      // Lite proaktiv nedstängning
      zipSearcher ! Stop
      fileFinder ! Stop
      this ! Stop
    } else {
      this ! StartSearch(directory, stringToFind)
    }
  }

  def logError(msg: String) {
    this ! LogError(msg)
  }

  def logFoundFile(file: File, classNames: List[String]) {
    this ! LogFoundFile(file, classNames)
  }

  def logFoundZipFile {
    this ! LogFoundZipFile
  }

  def logEndSearch(nrOfFiles: Int) {
    this ! LogEndSearch(nrOfFiles)
  }

}
