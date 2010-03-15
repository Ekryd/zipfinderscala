package zipfinder.gui

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger
import zipfinder.filefilter.DirectoryFileFilter
import zipfinder.filefilter.ZipFileFilter

case class StopSearch()

class SwingControllerActorAll extends Actor with StatusLogger with SearchButtonListener {
  private val swingGui = getSwingGui
  private var running = false

  case class StartSearch(directory: String, stringToFind: String)
  case class LogError(msg: String)
  case class LogFilesFound(msg: String)
  case class LogFoundZipFile()
  case class Search(zipSearcherActor: ZipSearcherActor, directory: File)
  case class ToggleSearch()

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
          val zipSearcherActor = new ZipSearcherActor(stringToFind, this)
          ZipFinderPreferences.addDirectory(directory)
          ZipFinderPreferences.addStringToFind(stringToFind)
          zipSearcherActor.start
          this ! Search(zipSearcherActor, new File(directory))
        }
        case StopSearch => {
          println("StopSearch")
          running = false
          swingGui.showDoneWorking
        }
        case Search(zipSearcherActor, directory) => {
          println("Search")
          if (stopInQueue) {
            println("request Stop")
            zipSearcherActor ! Stop
          } else {
            searchForFiles(zipSearcherActor, directory)
          }
        }
        case LogError(msg) => {
          swingGui.addToConsole(msg)
        }
        case LogFilesFound(msg) => {
          swingGui.addToConsole(msg)
        }
        case LogFoundZipFile => {
          swingGui.showWorking
        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  private def stopInQueue = mailbox.foldLeft(false) {(found, msg) => (found || msg == StopSearch)}

  private def searchInQueue = mailbox.foldLeft(false) {(found, msg) => (found || matchSearch(msg))}

  private def matchSearch(msg: Any) = {
    msg match {
      case Search(a, b) => true
      case msg => false
    }
  }

  private def searchForFiles(zipSearcherActor: ZipSearcherActor, directory: File) {
    val zipFiles = directory.listFiles(ZipFileFilter)
    if (zipFiles != null) {
      for (file <- zipFiles) {
        logFoundZipFile
        println("request SearchFile")
        zipSearcherActor ! SearchFile(file)
      }
      val directories = directory.listFiles(DirectoryFileFilter)
      for (directory <- directories) {
        println("request Search")
        this ! Search(zipSearcherActor, directory)
      }
    }
    if (!searchInQueue) {
      println("request Done")
      zipSearcherActor ! Done(this)
    }
  }

  def performButtonPress(directory: String, stringToFind: String) {
    println("performButtonPress")
    this ! (if (running) StopSearch else StartSearch(directory, stringToFind))
  }

  def logError(msg: String) {
    this ! LogError(msg)
  }

  def logFilesFound(msg: String) {
    this ! LogFilesFound(msg)
  }

  def logFoundZipFile {
    this ! LogFoundZipFile
  }

}
