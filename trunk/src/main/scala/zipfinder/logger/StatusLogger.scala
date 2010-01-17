package zipfinder.logger

trait StatusLogger {
	def logError(msg:String)

	def logFilesFound(msg:String)

	def logFoundZipFile
}
