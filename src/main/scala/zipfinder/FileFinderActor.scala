package zipfinder

import java.io.File
import actors._
import actors.Actor._
import zipfinder.filefilter.DirectoryFileFilter
import zipfinder.filefilter.ZipFileFilter
import zipfinder.logger.StatusLogger


class FileFinderActor(statusLogger:StatusLogger, zipSearcherActor:ZipSearcherActor) extends Actor {

	def act() {
	  loop {
		react {
			case Search(directory) => {
				Thread.sleep(100)
				if (stopInQueue) stop
				println("filefinder search")
				searchForFiles(directory)
				if (mailboxSize == 0) done
			}
			case Stop => stop
		}
	  }
	}

	private def stopInQueue = mailbox.foldLeft(false) { (found, msg) => (found || msg == Stop) }

	private def stop {
		println("filefinder stop")
		zipSearcherActor ! Stop
		exit
	}

	private def done {
		zipSearcherActor ! Done
		exit
	}

	private def searchForFiles(directory:File) {
		val zipFiles = directory.listFiles(ZipFileFilter)
		for (file <- zipFiles) {
			statusLogger.logFoundZipFile
			println("request search")
			zipSearcherActor ! Search(file)
		}
		val directories = directory.listFiles(DirectoryFileFilter)
		for (directory <- directories) {
			this ! Search(directory)
		}
	}

}
