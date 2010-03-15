package zipfinder.gui

import java.io.File

import actors._
import actors.Actor._
import zipfinder.ZipFinderPreferences
import zipfinder.logger.StatusLogger

class SwingControllerActor extends Actor with StatusLogger with SearchButtonListener {
  private val swingGui = getSwingGui
  private var fileFinderActor: FileFinderActor = _
  private var running = false

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
        //        case StartSearch => {
        //          running = true
        //          swingGui.showWorking
        //          println("request search filefinder")
        //          val directory = swingGui.getDirectory
        //          val stringToFind = swingGui.getStringToFind
        //          val zipSearcherActor = new ZipSearcherActor(stringToFind, this)
        //          fileFinderActor = new FileFinderActor(this, zipSearcherActor)
        //          ZipFinderPreferences.addDirectory(directory)
        //          ZipFinderPreferences.addStringToFind(stringToFind)
        //          zipSearcherActor.start
        //          fileFinderActor.start
        //          fileFinderActor ! Search(new File(directory))
        //        }
        //        case StopSearch => {
        //          running = false
        //          if (fileFinderActor != null) {
        //            println("request stop filefinder")
        //            fileFinderActor ! Stop
        //          }
        //          swingGui.showDoneWorking
        //        }
        //        case LogError(msg) => {
        //          swingGui.addToConsole(msg)
        //        }
        //        case LogFilesFound(msg) => {
        //          swingGui.addToConsole(msg)
        //        }
        //        case LogFoundZipFile => {
        //          println("showwork")
        //          swingGui.showWorking
        //        }
        case msg => {println("WTF!" + msg)}
      }
    }
  }

  def performButtonPress(directory: String, stringToFind: String) {
    println("performSearch")
    //    this ! (if (running) StopSearch else StartSearch)
  }

  def logError(msg: String) {
    //    this ! LogError(msg)
  }

  def logFilesFound(msg: String) {
    //    this ! LogFilesFound(msg)
  }

  def logFoundZipFile {
    //    this ! LogFoundZipFile
  }

}
