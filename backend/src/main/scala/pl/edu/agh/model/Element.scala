package pl.edu.agh.model

import pl.edu.agh.StepEntity

case class Element(nodeId: Seq[ElementId]) extends StepEntity

object Element {
  val fieldsCount: Int = 10
}