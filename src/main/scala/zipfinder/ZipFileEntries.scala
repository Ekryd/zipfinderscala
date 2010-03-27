package zipfinder

import java.io._
import java.util.zip._
import java.util.Enumeration

/**
 * Extracts zip file entries from file
 *
 * @author bjorn
 *
 */
class ZipFileEntries(val file: File) {
  def getEntries: List[ZipEntry] = {
    val zipFile = new ZipFile(file)
    val entries = new RichIterator(zipFile.entries)
    (List[ZipEntry]() /: entries) {(elem, list) => list :: elem}
  }

  def this() {this (null)}
}

/** Hjälpklass för att konvertera en java.util.Enumerator till en scala.Iterator */
class RichIterator[T](e: Enumeration[T]) extends Iterator[T] {
  def hasNext: Boolean = e.hasMoreElements()

  def next: T = e.nextElement()
}