package pl.edu.agh

import java.io.File

import org.scalatest.Assertion
import pl.edu.agh.model.{Element, Node, Step}

import scala.io.Source

class ModelLoaderSpec extends UnitSpec {

  val firstNodeAssertion = List(1, -0.0070711, 0, 0, 45.0385)
  val lastNodeAssertion = List(2268, 0.00707104, 0, 0.124, 45.0205)
  val firstElementAssertion = List(1, 4, 1, 2, 8, 7, 37, 38, 44, 43)
  val lastElementAssertion = List(1550, 4, 2225, 2226, 2232, 2231, 2261, 2262, 2268, 2267)

  "model loader" should "properly load single node from given line" in {

    val testString: String = "1, -0.0070711, 0, 0, 45.0385"
    val node: Node = ModelLoader.loadNode(testString)

    nodeSpec(node, firstNodeAssertion)

  }

  it should "properly load single step from file" in {

    val testData: Seq[String] = Source.fromResource("CoolingS355/step1.k").getLines.toSeq

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

    val folder = Source.fromResource("CoolingS355").getLines.toList
    val sortedFolderFiles = folder.sortBy(file => file.substring(4, file.length - 3))
    val files = sortedFolderFiles
      .map(filename => Source.fromResource(s"CoolingS355/$filename")
        .getLines
        .toSeq)

    ModelLoader.loadModel(files)

  }

  def nodeSpec(node: Node, values: List[Double]): Assertion = {

    node.nodeID should be (values(0))
    node.coordinates.x should be (values(1))
    node.coordinates.y should be (values(2))
    node.coordinates.z should be (values(3))
    node.temperature should be (values(4))

  }

  def elementSpec(element: Element, list: List[Int]): Seq[Assertion] = {

    (element.nodeId,list).zipped.map((nodeId, spec) => nodeId should be (spec))

  }

  def stepSpec(step: Step): Seq[Assertion] = {

    nodeSpec(step.nodes.head, firstNodeAssertion)
    nodeSpec(step.nodes.last, lastNodeAssertion)
    elementSpec(step.elements.head, firstElementAssertion)
    elementSpec(step.elements.last, lastElementAssertion)

  }

}
