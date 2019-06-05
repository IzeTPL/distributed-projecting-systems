package pl.edu.agh.model

import pl.edu.agh.StepEntity

case class Element(
                    elementId: ElementId,
                    point0: Int,
                    point1: Int,
                    point2: Int,
                    point3: Int,
                    point4: Int,
                    point5: Int,
                    point6: Int,
                    point7: Int,
                  ) extends StepEntity

object Element {
  val fieldsCount: Int = 10
}