package zipfinder.gui

import swing._
import GridBagPanel._
import javax.swing._
import java.awt.Insets
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

object SwingGuiRunner extends SimpleGUIApplication {
  private val TEXT_ROWS = 10
  private val INSET = 5
  private val MAX_LINES = 500
  private val SEARCH_LABEL = "Search"
  private val insets = new Insets(INSET, INSET, INSET, INSET)

  def top = new MainFrame {
    val upperPanel = new GridBagPanel {
      peer.getLayout.asInstanceOf[GridBagLayout].columnWidths = Array(INSET, INSET)
      peer.getLayout.asInstanceOf[GridBagLayout].rowHeights = Array(INSET, INSET)
      peer.getLayout.asInstanceOf[GridBagLayout].columnWeights = Array(1, 0)
      peer.getLayout.asInstanceOf[GridBagLayout].rowWeights = Array(0, 1)
      val directoryComboBox = new ComboBox(Array[String]())
      //          TODO directoryComboBox.makeEditable()
      layout(directoryComboBox) = new Constraints(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, insets, 0, 0))
      val directoryTree = new LimitedFileChooser {
        fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
        customize
      }
      layout(directoryTree) = new Constraints(
        0, 1, 2, 1, 0.0, 0.0, Anchor.CENTER.id, Fill.Both.id, insets, 0, 0)
    }
    val lowerPanel = new GridBagPanel {
      peer.getLayout.asInstanceOf[GridBagLayout].columnWidths = Array(INSET, INSET)
      peer.getLayout.asInstanceOf[GridBagLayout].rowHeights = Array(INSET, INSET)
      peer.getLayout.asInstanceOf[GridBagLayout].columnWeights = Array(1, 0)
      peer.getLayout.asInstanceOf[GridBagLayout].rowWeights = Array(0, 1)
      val textToFindField = new RecentStringsTextField(List()) {text = ""}
      layout(textToFindField) = new Constraints(
        0, 0, 1, 1, 0.0, 0.0, Anchor.CENTER.id, Fill.Horizontal.id, insets, 0, 0);
      val searchButton = new Button(SEARCH_LABEL)
      layout(searchButton) = new Constraints(
        1, 0, 1, 1, 0.0, 0.0, Anchor.CENTER.id, Fill.None.id, insets, 0, 0)
      val console = new TextArea("") {rows = TEXT_ROWS}
      layout(console) = new Constraints(
        0, 1, 2, 1, 0.0, 0.0, Anchor.CENTER.id, Fill.Both.id, insets, 0, 0)
    }
    val splitPanel = new SplitPane(Orientation.Horizontal, upperPanel, lowerPanel) {resizeWeight = 1.0}
    //          splitPanel.requestFocus = false
    contents = splitPanel

    // jSplitPanel
    //		upperPanelLayout.columnWidths =  Array(INSET, INSET)
    //		upperPanelLayout.rowHeights =  Array(INSET, INSET)
    //		upperPanelLayout.columnWeights =  Array( 1, 0 )
    //		upperPanelLayout.rowWeights =  Array( 0, 1 )
    //		lowerPanelLayout.columnWidths = Array(INSET, INSET )
    //		lowerPanelLayout.rowHeights = Array(INSET, INSET )
    //		lowerPanelLayout.columnWeights = Array(1, 0 )
    //		lowerPanelLayout.rowWeights = Array(0, 1 )

  }

}
