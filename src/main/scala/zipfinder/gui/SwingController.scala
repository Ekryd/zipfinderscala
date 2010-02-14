package zipfinder.gui

import java.util.LinkedList

import javax.swing.SwingUtilities

import zipfinder.FileFinder
import zipfinder.FoundFilesQueue
import zipfinder.ZipFinderPreferences
import zipfinder.ZipSearcherRunner
import zipfinder.gui._
import zipfinder.logger.StatusLogger

class SwingController extends StatusLogger with SearchButtonListener {
	private val swingGui = getSwingGui
	private val statusLogger:StatusLogger = this
	private var fileFinder:FileFinder = null

 	def getSwingGui = {
		var recentDirectories = ZipFinderPreferences.getRecentDirectories
    if (recentDirectories.size == 0) {
      recentDirectories = new LinkedList[String]() { add ("/tmp2") }
    }

		val recentStringsToFind = ZipFinderPreferences getRecentStringsToFind
		val swingGui = new SwingGui(recentDirectories.toArray(new Array[String](recentDirectories.size)), recentStringsToFind
				.toArray(new Array[String](recentStringsToFind.size)),this)
//		swingGui.setSearchButtonListener(this)
		swingGui
 	}

	def logError(msg:String) {
		swingGui.addToConsole(msg)
	}

	def logFilesFound(msg:String) {
		swingGui.addToConsole(msg)
	}

	def logFoundZipFile {
		swingGui.showWorking
	}

	def performSearch {
		val foundFilesQueue = new FoundFilesQueue
		val directory = swingGui.getDirectory
		val stringToFind = swingGui.getStringToFind
		val fileFinderThread = createFileFinderThread(directory, foundFilesQueue)
		val zipSearcherThread = createZipSearcherThread(stringToFind, foundFilesQueue)
		ZipFinderPreferences.addDirectory(directory)
		ZipFinderPreferences.addStringToFind(stringToFind)
		fileFinderThread.start
		zipSearcherThread.start
		new Thread(new Runnable {
			def run {
				try {
					fileFinderThread.join
				} catch {
				  case e:InterruptedException => statusLogger.logError(e.getMessage)
				}
				try {
					zipSearcherThread.join
				} catch {
				  case e:InterruptedException => statusLogger.logError(e.getMessage)
				}
				swingGui.showDoneWorking
				fileFinder = null
			}
		}).start
	}

	def performStopSearch {
		fileFinder.setStop
	}

	def start {
		SwingUtilities.invokeLater(new Runnable {
			def run {
				swingGui.createComponents
				swingGui.showFrame
			}
		})
	}

	def createFileFinderThread(directory:String, foundFilesQueue:FoundFilesQueue) = {
		val fileFinder = new FileFinder(directory, foundFilesQueue, statusLogger)
		new Thread(fileFinder)
	}

	def createZipSearcherThread(stringToFind:String, foundFilesQueue:FoundFilesQueue) = {
		val zipSearcherRunner = new ZipSearcherRunner(foundFilesQueue, stringToFind)
		zipSearcherRunner.setStatusLogger(statusLogger)
		new Thread(zipSearcherRunner)
	}
}
