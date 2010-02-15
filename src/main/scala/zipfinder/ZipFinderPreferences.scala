package zipfinder

import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

object ZipFinderPreferences {
	private val SEPARATOR = "|"
	private val SEPARATOR_REGEX = "\\" + SEPARATOR
	private val NR_OF_ENTRIES = 20
	private val STRINGS_TO_FIND_KEY = "StringsToFind"
	private val DIRECTORIES_KEY = "Directories"
	private val preferences = Preferences.systemNodeForPackage(this.getClass)

	def addDirectory(str:String) {
		if (isEmptyString(str)) {
			return
		}
		val oldArray = getRecentDirectories
		val newArray = createNewStringArray(str, oldArray)
		val newString = createNewString(newArray)
		preferences.put(DIRECTORIES_KEY, newString)
		try {
			preferences.sync
		} catch {
			case e:BackingStoreException => e.printStackTrace
		}
	}


	def addStringToFind(str:String) {
		if (isEmptyString(str)) {
			return
		}
		val oldArray = getRecentStringsToFind
		val newArray = createNewStringArray(str, oldArray)
		val newString = createNewString(newArray)
		preferences.put(STRINGS_TO_FIND_KEY, newString)
		try {
			preferences.sync
		} catch {
		  case e:BackingStoreException => e.printStackTrace
		}
	}

	def getRecentDirectories = getValueArray(DIRECTORIES_KEY)

	def getRecentStringsToFind = getValueArray(STRINGS_TO_FIND_KEY)

	private def createNewString(newArray:Array[String]) = {
	  newArray mkString SEPARATOR
	}

	private def createNewStringArray(str:String, oldCollection:Array[String]):Array[String] = {
	  val list = (List(str) /: oldCollection) { (list, elem) => if (elem == str) list else elem :: list }
	  list.reverse.take(NR_OF_ENTRIES).toArray
	}

	private def getValueArray(key:String):Array[String] = {
		val value = preferences.get(key, "")
		if (isEmptyString(value)) Array() else value.split(SEPARATOR_REGEX)
	}

	private def isEmptyString(str:String) = str == null || str.isEmpty

}
