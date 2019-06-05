package pl.edu.agh.model

import scalafx.scene.shape.TriangleMesh

case class Cube(points: Array[Float], colorValue: Float) {

    val mesh = new TriangleMesh()

    mesh.points = points

    mesh.texCoords = Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

    mesh.faces = Array(
      0, 0, 2, 0, 1, 0,
      0, 0, 3, 0, 2, 0,
      4, 0, 5, 0, 6, 0,
      4, 0, 6, 0, 7, 0,
      0, 0, 1, 0, 5, 0,
      0, 0, 5, 0, 4, 0,
      3, 0, 6, 0, 2, 0,
      3, 0, 7, 0, 6, 0,
      1, 0, 2, 0, 6, 0,
      1, 0, 6, 0, 5, 0,
      0, 0, 4, 0, 3, 0,
      4, 0, 7, 0, 3, 0
    )

}