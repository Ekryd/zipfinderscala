package zipfinder.gui

import javax.swing.JTextField
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument

class RecentStringsJTextField(protected val recentStringsToFind: Array[String]) extends JTextField {
  override protected def createDefaultModel = new UpperCaseDocument

  private def getCurrentText = super.getText

  class UpperCaseDocument extends PlainDocument {
    override def replace(offset: Int, length: Int, text: String, attrs: AttributeSet) {
      super.replace(offset, length, text, attrs)
      if (text == null || text.length == 0) {
        return
      }
      val currentText = getCurrentText
      for (recentString <- recentStringsToFind) {
        if (recentString.startsWith(currentText) && recentString.length > currentText.length) {
          super.replace(0, currentText.length, recentString, attrs)
          selectAll
          setSelectionStart(offset + 1)
          return
        }
      }
    }
  }
}
