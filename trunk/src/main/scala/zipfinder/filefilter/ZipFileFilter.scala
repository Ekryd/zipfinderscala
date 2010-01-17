package zipfinder.filefilter

import java.io._

object ZipFileFilter extends FileFilter {
	override def accept(pathname:File) = {
		if (!pathname.isFile) {
			false
		} else {
			val name = pathname.getName.toLowerCase
			(name endsWith ".zip") || (name endsWith ".jar")
		}
	}
}
