package pl.edu.agh.model

import pl.edu.agh.StepEntity

final case class Node(
               nodeID: NodeId,
               coordinates: Coordinates,
               temperature: Temperature
               ) extends StepEntity

object Node {
  val fieldsCount: Int = 5
}