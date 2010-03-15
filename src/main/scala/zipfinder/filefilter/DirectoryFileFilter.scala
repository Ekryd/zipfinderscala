package zipfinder.filefilter

import java.io._

object DirectoryFileFilter extends FileFilter {
  override def accept(pathname: File) = pathname isDirectory
}
