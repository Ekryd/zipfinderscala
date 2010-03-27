package zipfinder.filefilter

import java.io._

/** Avgör om det är en zip eller jar-fil */
object ZipFileFilter extends FileFilter {
  override def accept(pathname: File) = {
    if (!pathname.isFile) {
      false
    } else {
      val name = pathname.getName.toLowerCase
      (name endsWith ".zip") || (name endsWith ".jar")
    }
  }
}
