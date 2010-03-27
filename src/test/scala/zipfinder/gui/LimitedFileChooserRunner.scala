package zipfinder.gui

import swing._

object LimitedFileChooserRunner extends SimpleGUIApplication {
  def top = new MainFrame {
    title = "LimitedFileChooser instance test"
    val instance = new LimitedFileChooser
    instance.customize
    contents = instance
  }
}
