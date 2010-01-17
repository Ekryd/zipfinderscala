package zipfinder

import java.util.Arrays
import java.util.Collection
import java.util.LinkedList
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

	private def createNewString(newArray:Collection[String]) = {
		val newString = new StringBuffer
		var first = true
		for (str <- newArray.toArray) {
			if (first) {
				first = false
			} else {
				newString.append(SEPARATOR)
			}
			newString.append(str)
		}
		newString.toString
	}

	private def createNewStringArray(str:String, oldCollection:Collection[String]) = {
		val returnValue = new LinkedList[String](oldCollection)
		if (returnValue.contains(str)) {
			returnValue.remove(str)
		}
		returnValue.addFirst(str)
		while (returnValue.size > NR_OF_ENTRIES) {
			returnValue.removeLast
		}
		returnValue
	}

	private def getValueArray(key:String) = {
		val value = preferences.get(key, "")
		val valueArray = value.split(SEPARATOR_REGEX)
		val returnValue = new LinkedList[String]
		if (valueArray.length != 1 || !valueArray(0).isEmpty) {
			returnValue.addAll(Arrays.asList(valueArray:_*))
		}
		returnValue
	}

	private def isEmptyString(str:String) = str == null || str.isEmpty

}
