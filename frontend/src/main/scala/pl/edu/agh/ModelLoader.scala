package pl.edu.agh

import java.io.File

import pl.edu.agh.model.{Coordinates, Cube, Element, Node, SimulationModel, Step}

object ModelLoader {

  def loadModel(folder: Seq[Seq[String]]): SimulationModel =
    SimulationModel(
      folder.map(file =>
        loadStep(file)
      )
    )

  def loadStep(stepFile: Seq[String]): Step = {

    val rawData: Seq[String] = stepFile.filterNot( line =>
      line.contains("*NODE") ||
        line.contains("*ELEMENT_SOLID") ||
      line.contains("*END")
    )

    val entities: Seq[StepEntity] = rawData.map{line =>
      val fieldsCount = line.split(", ").length
      fieldsCount match {
        case Node.fieldsCount => loadNode(line)
        case Element.fieldsCount => loadElement(line)
      }
    }

    val nodes = entities.collect { case node: Node => node }
    val elements = entities.collect { case element: Element => element }

    val nodesWithNormalizedTemperature = normalizeTemperatures(nodes)

    Step(nodesWithNormalizedTemperature, elements)
  }

  def loadNode(nodeData: String): Node = {

    val fields = nodeData.split(", ")

    Node(
      fields(0).toInt,
      Coordinates(
        fields(1).toFloat,
        fields(2).toFloat,
        fields(3).toFloat
      ),
      fields(4).toFloat
    )

  }

  def loadElement(elementData: String): Element = {

    val fields = elementData.split(", ")

    Element(
      fields(0).toInt,
      fields(2).toInt,
      fields(3).toInt,
      fields(4).toInt,
      fields(5).toInt,
      fields(6).toInt,
      fields(7).toInt,
      fields(8).toInt,
      fields(9).toInt
    )
  }

  def normalizeTemperatures(nodes: Seq[Node]): Seq[Node] = {

    var min = Float.MaxValue
    var max = Float.MinValue

    nodes.foreach { node =>

      if (node.temperature < min) min = node.temperature
      if (node.temperature > max) max = node.temperature

    }


    nodes.map{oldNode =>
      val newTemp = (oldNode.temperature - min) / (max - min)
      Node(
        oldNode.nodeID,
        oldNode.coordinates,
        newTemp
      )
    }

  }

  def convertToCubePoints(step: Step): Seq[Cube] = {

    val result = step.elements.map { element =>

      val p0: Node = step.nodes.find(node => node.nodeID == element.point0).head
      val p1: Node = step.nodes.find(node => node.nodeID == element.point1).head
      val p2: Node = step.nodes.find(node => node.nodeID == element.point2).head
      val p3: Node = step.nodes.find(node => node.nodeID == element.point3).head
      val p4: Node = step.nodes.find(node => node.nodeID == element.point4).head
      val p5: Node = step.nodes.find(node => node.nodeID == element.point5).head
      val p6: Node = step.nodes.find(node => node.nodeID == element.point6).head
      val p7: Node = step.nodes.find(node => node.nodeID == element.point7).head

      val points = Seq(p0,p1,p2,p3,p4,p5,p6,p7)

      val array: Array[Float] = Array(
        p0.coordinates.x, p0.coordinates.y, p0.coordinates.z,
        p1.coordinates.x, p1.coordinates.y, p1.coordinates.z,
        p2.coordinates.x, p2.coordinates.y, p2.coordinates.z,
        p3.coordinates.x, p3.coordinates.y, p3.coordinates.z,
        p4.coordinates.x, p4.coordinates.y, p4.coordinates.z,
        p5.coordinates.x, p5.coordinates.y, p5.coordinates.z,
        p6.coordinates.x, p6.coordinates.y, p6.coordinates.z,
        p7.coordinates.x, p7.coordinates.y, p7.coordinates.z
      )

      var temp: Float = 0

      points.foreach {
        point => temp = temp + point.temperature
      }

      temp = temp / points.length

      Cube(array, temp)

    }

    result

  }

}
