package zipfinder.gui

import java.awt.Component
import java.awt.Container

import javax.swing.AbstractButton
import javax.swing.JPanel
import javax.swing.JFileChooser

import swing._
import javax.swing.JPanel
import FileChooser._
import java.io._

/** Skapar en FileChooser, men plockar bort lite knappar som inte behövs */
class LimitedFileChooser extends swing.Component with Publisher {
  lazy val fileChooser: FileChooser = new FileChooser {
    controlButtonsAreShown = false
    fileHidingEnabled = false
  }
  override lazy val peer = fileChooser.peer

  def customize {
    removeExcessButtons(fileChooser.peer, fileChooser.peer.getComponents)
    removeExcessFields(fileChooser.peer, fileChooser.peer.getComponents)
  }

  def fileSelectionMode: SelectionMode.Value = SelectionMode(peer.getFileSelectionMode)

  def fileSelectionMode_=(s: SelectionMode.Value) {peer.setFileSelectionMode(s.id)}

  def selectedFile: File = peer.getSelectedFile

  def selectedFile_=(file: File) {peer.setSelectedFile(file)}

  // Matchar en container
  object AContainer {
    def unapply(comp: Component) = comp match {
      case cont: Container => Some(cont, List.fromArray(cont.getComponents))
      case _ => None
    }
  }

  // Matchar en knapp
  object AButton {
    def unapply(comp: Component) = comp match {
      case button: AbstractButton => Some(button)
      case _ => None
    }
  }


  private def getAllButtons(parent: Container, components: List[Component]): List[(AbstractButton, Container)] = {
    components match {
      case List() => List[(AbstractButton, Container)]()
      case AButton(button) :: tail => (button, parent) :: getAllButtons(parent, tail)
      case AContainer(cont, comps) :: tail => getAllButtons(cont, comps) ::: getAllButtons(parent, tail)
      case head :: tail => getAllButtons(parent, tail)
    }
  }

  private def removeExcessButtons(parent: Container, components: Array[Component]) {
    val buttons = getAllButtons(parent, List.fromArray(components))
    (true /: buttons)((first, pair) => {
      if (!first) {
        pair._2 remove pair._1
      }
      false
    })
  }

  private def removeExcessFields(parent: Container, components: Array[Component]) {
    val component = components(components.length - 1)
    if (component.isInstanceOf[JPanel]) {
      parent remove component
    } else {
      throw new IllegalStateException("Layout is not as expected")
    }
  }
}

