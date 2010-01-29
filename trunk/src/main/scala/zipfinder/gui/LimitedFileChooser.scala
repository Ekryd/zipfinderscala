package zipfinder.gui

import java.awt.Component
import java.awt.Container

import javax.swing.AbstractButton
import swing._
import javax.swing.JPanel
import FileChooser._
import java.io._
import scala.collection.mutable.LinkedHashMap  


class LimitedFileChooser extends swing.Component {
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

//  private def getAllButtons(parent:Container, components:List[Component]):Map[AbstractButton, Container] = {
//    components match {
//      case List() => Map[AbstractButton, Container]()
//      case comp :: rest => 
//        if (comp.isInstanceOf[AbstractButton]) getAllButtons(parent, rest)(comp.asInstanceOf[AbstractButton]) = parent
//        else if (comp.isInstanceOf[Container]) getAllButtons(comp.asInstanceOf[Container], List.fromArray(comp.asInstanceOf[Container].getComponents)) ++ getAllButtons(parent, rest).elements
//        else getAllButtons(parent, rest)
//    }    
//  }
  private def getAllButtons(parent:Container, components:List[Component]):List[(AbstractButton, Container)] = {
    components match {
      case List() => List[(AbstractButton, Container)]()
      case comp :: rest if comp.isInstanceOf[AbstractButton] => (comp.asInstanceOf[AbstractButton], parent) :: getAllButtons(parent, rest) 
      case comp :: rest if comp.isInstanceOf[Container] => getAllButtons(comp.asInstanceOf[Container], List.fromArray(comp.asInstanceOf[Container].getComponents)) ::: getAllButtons(parent, rest)
      case comp :: rest => getAllButtons(parent, rest)
    }    
  }
  
	private def removeExcessButtons(parent:Container, components:Array[Component]) {
//	  ((true, parent) /: components){((first, parent), comp) => (first, parent) }
	  
	  val buttons = getAllButtons(parent, List.fromArray(components))
      (true /: buttons)((first, pair) => { 
        if (!first) { 
          pair._2 remove pair._1 
          println("remove" + pair._1.getToolTipText)
        } 
        false })
	
//	  val buttons = new Array[AbstractButton]
//      /: components) { (buttonArray, component) 
//     => if ()
//   }
//	
//	  
//	  (true /: components){(first, component) => 
//	    if (component.isInstanceOf[AbstractButton]) {
//				if (first) {
//					false
//				} else {
//					val button = component.asInstanceOf[AbstractButton]
//					parent remove button
//                    false
//				}
//    }else if (component.isInstanceOf[Container]) {
//				val container = component.asInstanceOf[Container]
//				removeExcessButtons(container, container.getComponents)
//				first        
//	  } else { first }}
//		var first = true
//		for (component <- components) {
//			if (component.isInstanceOf[AbstractButton]) {
//				if (first) {
//					first = false
//				} else {
//					val button = component.asInstanceOf[AbstractButton]
//					parent remove button
//				}
//			}
//			if (component.isInstanceOf[Container]) {
//				val container = component.asInstanceOf[Container]
//				removeExcessButtons(container, container.getComponents)
//			}
//		}
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

