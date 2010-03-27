package zipfinder.filefilter

import java.io._

/** Avgör om det är en katalog */
object DirectoryFileFilter extends FileFilter {
  override def accept(pathname: File) = pathname isDirectory
}
