package zipfinder.gui

import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.File

import javax.swing._
import javax.swing.text.BadLocationException
import swing._
import GridBagPanel._

class SwingGui(recentDirectories:Array[Object], recentStringsToFind:Array[String]) {
	private val TEXT_ROWS = 10
	private val INSET = 5
	private val MAX_LINES = 500
	private val SEARCH_LABEL = "Search"
	private val insets = new Insets(INSET, INSET, INSET, INSET)
	private var frame:Frame = _
	/*
	private var frame:JFrame = _
	private var console:JTextArea = _
	private var textToFindField:RecentStringsJTextField = _
	private var lowerPanel:JPanel = _
	private var upperPanel:JPanel = _
	private var jSplitPane1:JSplitPane = _
	private var directoryComboBox:JComboBox = _
	private var directoryTree:LimitedJFileChooser = _
	private var searchButtonListener:SearchButtonListener = _
	private var searchButton:JButton = _;
 	*/
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
		} catch {
		  case e:Exception => e.printStackTrace
		}
	}

 	def addListeners {
 	  /*
		directoryTree.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY,
				new PropertyChangeListener {
					 def propertyChange(evt:PropertyChangeEvent) {
					   val selectedItem = directoryComboBox.getSelectedItem
						val oldValue = if (selectedItem == null) "" else selectedItem.toString
						if (!oldValue.equals(directoryTree.getSelectedFile)) {
							directoryComboBox.setSelectedItem(directoryTree.getSelectedFile)
						}
					}
				})
		directoryComboBox.addActionListener(new ActionListener {
			 def actionPerformed(e:ActionEvent) {
				val file = new File(directoryComboBox.getSelectedItem.toString)
				if (file.canRead) {
					directoryTree.setSelectedFile(file)
				}
			}
		})
		searchButton.addActionListener(new ActionListener {
			def actionPerformed(e:ActionEvent) {
				if (searchButton.getText.equals(SEARCH_LABEL)) {
					searchButtonListener.performSearch
				} else {
					searchButtonListener.performStopSearch
				}
			}
		})
  */
	}
	def addToConsole(text:String) {
	  /*
		SwingUtilities.invokeLater(new Runnable {
			def run {
				while (console.getLineCount > MAX_LINES) {
					try {
						val offset = console.getLineEndOffset(0)
						console.getDocument.remove(0, offset)
					} catch {
					  case e:BadLocationException => e.printStackTrace
					}
				}
				console.append(text + "\n")
			}
		})
 */
	}


	def createComponents {
	    val upperPanel = new GridBagPanel {
	      val directoryComboBox = new ComboBox(recentDirectories)
//          TODO directoryComboBox.makeEditable()
	      layout(directoryComboBox) = new Constraints(
	        0, 0, 1, 1, 0.0, 0.0, Anchor.CENTER.id,	Fill.Horizontal.id, insets, 0, 0)
	      val directoryTree = new LimitedFileChooser{
	    	  fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
	    	  customize
       }
	      //directoryTree.customize
	      //directoryTree.fileSelectionMode_=( FileChooser.SelectionMode.DirectoriesOnly)
		  if (directoryComboBox.selection != null)
			directoryTree.selectedFile = new File(directoryComboBox.selection.toString)
          layout(directoryTree) = new Constraints(
	        0, 1, 2, 1, 0.0, 0.0, Anchor.CENTER.id,	Fill.Both.id, insets, 0, 0)
	    }
        val lowerPanel = new GridBagPanel {
           val textToFindField = new RecentStringsTextField(recentStringsToFind) {
             text = ""
           }
        }//
		// frame
		frame = new Frame {
		  title = "ZipFinder"
		  val splitPanel = new SplitPane(Orientation.Vertical, upperPanel, lowerPanel) {
		    resizeWeight = 1.0
		  }
//          splitPanel.requestFocus = false
		  contents = splitPanel
		}
		// jSplitPanel
//		jSplitPane1 = new JSplitPane(Orientation.Vertiv)
//		frame.getContentPane.add(jSplitPane1, BorderLayout.CENTER)
//		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT)
//		jSplitPane1.setResizeWeight(1.0)
//		jSplitPane1.setRequestFocusEnabled(false)
		// upperPanel
/*
		upperPanel = new JPanel
		jSplitPane1.add(upperPanel, JSplitPane.TOP)
		val upperPanelLayout = new GridBagLayout
		upperPanelLayout.columnWidths =  Array(INSET, INSET)
		upperPanelLayout.rowHeights =  Array(INSET, INSET)
		upperPanelLayout.columnWeights =  Array( 1, 0 )
		upperPanelLayout.rowWeights =  Array( 0, 1 )
		upperPanel.setLayout(upperPanelLayout)
		// directoryComboBox
//		val directoryComboBoxModel = new DefaultComboBoxModel(recentDirectories)
//		directoryComboBox = new JComboBox
//		upperPanel.add(directoryComboBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, insets, 0, 0))
//		directoryComboBox.setModel(directoryComboBoxModel)
//		directoryComboBox.setEditable(true)
		// directoryTree
//		directoryTree = new LimitedJFileChooser
//		upperPanel.add(directoryTree, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, insets, 0, 0))
//		directoryTree.customize
//		directoryTree.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
//		if (directoryComboBox.getSelectedItem != null)
//			directoryTree.setSelectedFile(new File(directoryComboBox.getSelectedItem.toString))
		// lowerPanel
//		lowerPanel = new JPanel
//		val lowerPanelLayout = new GridBagLayout
//		lowerPanelLayout.columnWidths = Array(INSET, INSET )
//		lowerPanelLayout.rowHeights = Array(INSET, INSET )
//		lowerPanelLayout.columnWeights = Array(1, 0 )
//		lowerPanelLayout.rowWeights = Array(0, 1 )
//		jSplitPane1.add(lowerPanel, JSplitPane.BOTTOM)
//		lowerPanel.setLayout(lowerPanelLayout)
		// textToFindField
		textToFindField = new RecentStringsJTextField(recentStringsToFind)
		lowerPanel.add(textToFindField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, insets, 0, 0))
		textToFindField.setText("")
		searchButton = new JButton
		lowerPanel.add(searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, insets, 0, 0))
		searchButton.setText(SEARCH_LABEL)
		// console
		console = new JTextArea
		lowerPanel.add(new JScrollPane(console), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0))
		console.setText("")
		console.setRows(TEXT_ROWS)
*/

		frame.pack
		frame.peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	}

 def getDirectory:String = {
		null //directoryComboBox.getSelectedItem.toString
	}

	def getStringToFind:String = {
		null //textToFindField.getText
	}

	def setSearchButtonListener(searchButtonListener:SearchButtonListener) {
		null //this.searchButtonListener = searchButtonListener
	}

	def showDoneWorking {
		SwingUtilities.invokeLater(new Runnable {
			def run {
				null //searchButton.setText(SEARCH_LABEL)
				// searchButton.setEnabled(true)
			}
		})
	}

	def showFrame {
		frame.setLocationRelativeTo(null)
		frame.visible = true
	}

	def showWorking {
	  /*
		// searchButton.setEnabled(false)
		SwingUtilities.invokeLater(new Runnable {
			def run {
				if (searchButton.getText.equals("working.   ")) {
					searchButton.setText("working..  ")
				} else if (searchButton.getText.equals("working..  ")) {
					searchButton.setText("working... ")
				} else if (searchButton.getText.equals("working... ")) {
					searchButton.setText("working....")
				} else {
					searchButton.setText("working.   ")
				}
			}
		})
  */
	}

}

trait SearchButtonListener {
	def performSearch
	def performStopSearch
}
