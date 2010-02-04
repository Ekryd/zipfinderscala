package zipfinder.gui

import java.awt.Component
import java.awt.Container

import javax.swing.AbstractButton
import swing._
import javax.swing.JPanel
import javax.swing.JFileChooser
import FileChooser._
import java.io._
import scala.collection.mutable.LinkedHashMap


class LimitedFileChooser extends swing.Component with Publisher {
	lazy val fileChooser:FileChooser = new FileChooser{
		controlButtonsAreShown = false
		fileHidingEnabled = false
 	}
	override lazy val peer = fileChooser.peer

  	def customize {
		removeExcessButtons(fileChooser.peer, fileChooser.peer.getComponents)
		removeExcessFields(fileChooser.peer, fileChooser.peer.getComponents)
	}

  def fileSelectionMode: SelectionMode.Value = SelectionMode(peer.getFileSelectionMode)
  def fileSelectionMode_=(s: SelectionMode.Value) { peer.setFileSelectionMode(s.id) }
  def selectedFile: File = peer.getSelectedFile
  def selectedFile_=(file: File) { peer.setSelectedFile(file) }

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


  private def getAllButtons(parent:Container, components:List[Component]):List[(AbstractButton, Container)] = {
    components match {
      case List() => List[(AbstractButton, Container)]()
//      case comp :: rest if comp.isInstanceOf[AbstractButton] => (comp.asInstanceOf[AbstractButton], parent) :: getAllButtons(parent, rest)
//      case comp :: rest if comp.isInstanceOf[Container] => getAllButtons(comp.asInstanceOf[Container], List.fromArray(comp.asInstanceOf[Container].getComponents)) ::: getAllButtons(parent, rest)
      case AButton(button)         :: tail => (button, parent) :: getAllButtons(parent, tail)
      case AContainer(cont, comps) :: tail => getAllButtons(cont, comps) ::: getAllButtons(parent, tail)
      case head                    :: tail => getAllButtons(parent, tail)
    }
  }

	private def removeExcessButtons(parent:Container, components:Array[Component]) {
	  val buttons = getAllButtons(parent, List.fromArray(components))
      (true /: buttons)((first, pair) => {
        if (!first) {
          pair._2 remove pair._1
//          println("remove" + pair._1.getToolTipText)
        }
        false
      })
	}

	private def removeExcessFields(parent:Container, components:Array[Component]) {
		val component = components(components.length - 1)
		if (component.isInstanceOf[JPanel]) {
			parent remove component
		} else {
			throw new IllegalStateException("Layout is not as expected")
		}
	}
}

