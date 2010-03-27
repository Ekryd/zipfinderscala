package zipfinder.gui

import swing._

object RecentStringsTextFieldRunner extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "LimitedFileChooser instance test"
    val instance = new RecentStringsTextField(Array("aaaa", "bbbbb", "ccccc"))
    contents = instance
  }
}
