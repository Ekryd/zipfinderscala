package zipfinder

import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences
import zipfinder.Preamble._

/**Sparar av tidigare sökningar och kataloger */
object ZipFinderPreferences {
  private val SEPARATOR = "|"
  private val SEPARATOR_REGEX = "\\" + SEPARATOR
  private val NR_OF_ENTRIES = 20
  private val STRINGS_TO_FIND_KEY = "StringsToFind"
  private val DIRECTORIES_KEY = "Directories"
  private val preferences = Preferences.systemNodeForPackage(this.getClass)

  def addDirectory(str: String) {
    if (isEmptyString(str)) {
      return
    }
    val oldList = getRecentDirectories
    val newList = createNewStringList(str, oldList)
    val newString = createNewString(newList)
    preferences.put(DIRECTORIES_KEY, newString)
    try {
      preferences.sync
    } catch {
      case e: BackingStoreException => e.printStackTrace
    }
  }


  def addStringToFind(str: String) {
    if (isEmptyString(str)) {
      return
    }
    val oldList = getRecentStringsToFind
    val newList = createNewStringList(str, oldList)
    val newString = createNewString(newList)
    preferences.put(STRINGS_TO_FIND_KEY, newString)
    try {
      preferences.sync
    } catch {
      case e: BackingStoreException => e.printStackTrace
    }
  }

  def getRecentDirectories = getValueList(DIRECTORIES_KEY)

  def getRecentStringsToFind = getValueList(STRINGS_TO_FIND_KEY)

  private def createNewString(newList: List[String]) = {
    newList mkString SEPARATOR
  }

  private def createNewStringList(str: String, oldCollection: List[String]): List[String] = {
    val list = (List(str) /: oldCollection) {(list, elem) => if (elem == str) list else elem :: list}
    list.reverse.take(NR_OF_ENTRIES)
  }

  private def getValueList(key: String): List[String] = {
    val value = preferences.get(key, "")
    if (isEmptyString(value)) List() else value.split(SEPARATOR_REGEX)
  }

  private def isEmptyString(str: String) = str == null || str.isEmpty

}
