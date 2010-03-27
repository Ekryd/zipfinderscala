package zipfinder.actor

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter._
import zipfinder.gui._


class SwingControllerActor extends Actor with StatusLogger with SearchButtonListener {
  private val swingGui = getSwingGui
  private var running = false
  private var fileFinder: FileFinderActor = _


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
          println("StartSearch")
          ZipFinderPreferences.addDirectory(directory)
          ZipFinderPreferences.addStringToFind(stringToFind)
          fileFinder = new FileFinderActor(this, stringToFind) {start}
          fileFinder.search(new File(directory))
        }
        case LogEndSearch(nrOfFiles) => {
          swingGui.addToConsole(nrOfFiles)
          stop
        }
        case StopSearch => {
          stop
        }
        case LogError(msg) => {
          swingGui.addToConsole(msg)
        }
        case LogFoundFile(file: File, classNames: List[String]) => {
          if (!stopInQueue) {
            println("LogFilesFound")
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

  private def stopInQueue = mailbox.foldLeft(false) {(found, msg) => (found || msg == StopSearch)}

  def stop {
    println("StopSearch")
    running = false
    fileFinder.stop
    swingGui.showDoneWorking
  }

  def performButtonPress(directory: String, stringToFind: String) {
    println("performButtonPress")
    this ! (if (running) StopSearch else StartSearch(directory, stringToFind))
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
