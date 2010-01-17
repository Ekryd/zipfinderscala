package zipfinder

import java.io._
import java.util.Enumeration
import java.util.zip._

/**
 * Extracts zip file entries from file
 *
 * @author bjorn
 *
 */
class ZipFileEntries(val file:File) {

	def getEntries = {
		val zipFile = new ZipFile(file)
		zipFile.entries
	}

	def this() { this(null) }
}
