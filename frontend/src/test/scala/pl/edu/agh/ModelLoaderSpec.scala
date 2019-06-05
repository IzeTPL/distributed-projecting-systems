package pl.edu.agh

import java.io.File

import org.scalatest.Assertion
import pl.edu.agh.model.{Cube, Element, Node, SimulationModel, Step}

import scala.io.Source

class ModelLoaderSpec extends UnitSpec {

  val firstNodeAssertion = List(1.0f, -0.0070711f, 0.0f, 0.0f, 45.0385f)
  val lastNodeAssertion = List(2268, 0.00707104f, 0.0f, 0.124f, 45.0205f)
  val firstElementAssertion = List(1, 4, 1, 2, 8, 7, 37, 38, 44, 43)
  val lastElementAssertion = List(1550, 4, 2225, 2226, 2232, 2231, 2261, 2262, 2268, 2267)
  val firstCubePointsAssertion = Array(
    -0.0070711f, 0.0f, 0.0f,
    -0.00565689f, -0.00141421f, 0.0f,
    -0.00424267f, 0.0f, 0.0f,
    -0.00565689f, 0.00141421f, 0.0f,
    -0.0070711f, 0.0f, 0.002f,
    -0.00565689f, -0.00141421f, 0.002f,
    -0.00424267f, 0.0f, 0.002f,
    -0.00565689f, 0.00141421f, 0.002f
  )

  "model loader" should "properly load single node from given line" in {

    val testString: String = "1, -0.0070711, 0, 0, 45.0385"
    val node: Node = ModelLoader.loadNode(testString)

    nodeSpec(node, firstNodeAssertion)

  }

  it should "properly load single step from file" in {

    val resource: Source = Source.fromResource("CoolingS355/step1.k")
    val testData: Seq[String] = try resource.mkString.split("\r\n").toSeq finally resource.close()

    val step: Step = ModelLoader.loadStep(testData)
    val node: Node = step.nodes.head
    val lastNode: Node = step.nodes.last
    val element: Element = step.elements.head
    val lastElement: Element = step.elements.last

    nodeSpec(node, firstNodeAssertion)
    nodeSpec(lastNode, lastNodeAssertion)
    elementSpec(element, firstElementAssertion)
    elementSpec(lastElement, lastElementAssertion)

  }

  it should "properly load whole simulation" in {

    val folder = Source.fromResource("CoolingS355")
    val folderData = folder.getLines.toList
    val sortedFolderFiles = folderData.sortBy(file => file.substring(4, file.length - 3))
    val files = sortedFolderFiles
      .map(filename => {
      val resource: Source = Source.fromResource(s"CoolingS355/$filename")
      try resource.mkString.split("\r\n").toSeq finally resource.close()
      })
    folder.close()

    val model: SimulationModel = ModelLoader.loadModel(files)

  }

  it should "properly convert step to cube objects" in {

    val resource: Source = Source.fromResource("CoolingS355/step1.k")
    val testData: Seq[String] = try resource.mkString.split("\r\n").toSeq finally resource.close()

    val step: Step = ModelLoader.loadStep(testData)

    val cubePoints: Seq[Cube] = ModelLoader.convertToCubePoints(step)

  }

  def nodeSpec(node: Node, values: List[Float]): Assertion = {

    node.nodeID should be (values(0))
    node.coordinates.x should be (values(1))
    node.coordinates.y should be (values(2))
    node.coordinates.z should be (values(3))
    //node.temperature should be (values(4))

  }

  def elementSpec(element: Element, list: List[Int]): Assertion = {

    element.elementId should be (list(0))
    element.point0 should be (list(2))
    element.point1 should be (list(3))
    element.point2 should be (list(4))
    element.point3 should be (list(5))
    element.point4 should be (list(6))
    element.point5 should be (list(7))
    element.point6 should be (list(8))
    element.point7 should be (list(9))

  }

  def stepSpec(step: Step): Assertion = {

    nodeSpec(step.nodes.head, firstNodeAssertion)
    nodeSpec(step.nodes.last, lastNodeAssertion)
    elementSpec(step.elements.head, firstElementAssertion)
    elementSpec(step.elements.last, lastElementAssertion)

  }

}
