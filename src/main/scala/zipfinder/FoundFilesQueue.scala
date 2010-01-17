package zipfinder

import java.io.File
import java.util.concurrent.ArrayBlockingQueue

class FoundFilesQueue extends ArrayBlockingQueue[File](FoundFilesQueue INITIAL_CAPACITY) {
	private var done = false

	def setDone {
		done = true
	}

	def isDone = done
}

object FoundFilesQueue {
	val INITIAL_CAPACITY = 40
}