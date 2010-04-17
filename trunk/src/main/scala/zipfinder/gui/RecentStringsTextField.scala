package zipfinder.gui

import javax.swing.JTextField
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument
import swing._

/**Utökar ett JTextField med minne för att komma ihåg tidigare sökningar */
class RecentStringsTextField(protected val recentStringsToFind: List[String]) extends TextField {
  override lazy val peer: JTextField = new JTextField("", 0) with SuperMixin {
    override protected def createDefaultModel = new UpperCaseDocument
  }

  private def getCurrentText = super.text

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
          peer.setSelectionStart(offset + 1)
          return
        }
      }
    }
  }
}
