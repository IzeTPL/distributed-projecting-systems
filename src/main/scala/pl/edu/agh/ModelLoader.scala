package pl.edu.agh

import java.io.File

import pl.edu.agh.model.{Coordinates, Element, Node, SimulationModel, Step}

object ModelLoader {

  def loadModel(folder: Seq[Seq[String]]): SimulationModel =
    SimulationModel(folder.map(file => loadStep(file)))

  def loadStep(stepFile: Seq[String]): Step = {

    val rawData: Seq[String] = stepFile.filterNot( line =>
      line.contains("*NODE") ||
        line.contains("*ELEMENT_SOLID") ||
      line.contains("*END")
    )

    val entities: Seq[StepEntity] = rawData.map{line =>
      val fieldsCount = line.split(",").length
      fieldsCount match {
        case Node.fieldsCount => loadNode(line)
        case Element.fieldsCount => loadElement(line)
      }
    }

    val nodes = entities.collect { case node: Node => node }
    val elements = entities.collect { case element: Element => element }

    Step(nodes, elements)
  }

  def loadNode(nodeData: String): Node = {

    val fields = nodeData.split(",")

    Node(
      fields(0).toInt,
      Coordinates(
        fields(1).toDouble,
        fields(2).toDouble,
        fields(3).toDouble
      ),
      fields(4).toDouble
    )

  }

  def loadElement(elementData: String): Element = {

    val elementIds = elementData.split(",")
      .map(field => field.trim.toInt)
      .toSeq

    Element(elementIds)
  }

}
