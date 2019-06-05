package pl.edu.agh

import pl.edu.agh.model.{Cube, SimulationModel}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.{DoubleProperty, ReadOnlyDoubleProperty, StringProperty}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.{shape, _}
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.effect.BlendMode.Overlay
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.paint.{Color, LinearGradient, PhongMaterial, Stops}
import scalafx.scene.shape.{MeshView, Rectangle, TriangleMesh}
import scalafx.scene.transform.Rotate
import scalafx.stage.{DirectoryChooser, FileChooser}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.CycleMethod.NoCycle

import scala.io.Source

object RenderFromFileTest extends JFXApp {

  var root3D: Group = _
  var shapes: Group = _
  var cubeObj: MeshView = _
  var box: MeshView = _
  var model: SimulationModel = _
  var index: Int = 0
  var all: Seq[Seq[MeshView]] = _
  var indexText: StringProperty = new StringProperty("Step")

  stage = new PrimaryStage {
    title = "TriangleMesh Demo"
    scene = new Scene(1200, 950, true, SceneAntialiasing.Balanced) {
      // these are read only properties for the scene
      var tmpw = this.width
      var tmph = this.height

      root = new BorderPane {

        fill = Color.Black
        right = new VBox() {
          fill = Color.Black
          minWidth = 100
          children = Seq(new Button {
            minWidth = 100
            text = "Choose folder"
            onAction = handle {

              shapes.children.clear()
              val directoryChooser = new DirectoryChooser()
              val directory = directoryChooser.showDialog(stage)
              val names = directory.listFiles().map(file => file.getName)
              val folderData = names
              val sortedFolderFiles = folderData.sortBy(file => file.substring(4, file.length - 3))
              val files = sortedFolderFiles
                .map(filename => {
                  val resource: Source = Source.fromFile(s"${directory.getPath}/$filename")
                  try resource.mkString.split("\r\n").toSeq// finally resource.close()
                })
              model = ModelLoader.loadModel(files)
              import collection.JavaConverters._

              all = model.steps.map(step => ModelLoader.convertToCubePoints(step)
                .map(box => new MeshView(box.mesh) {
                material = new PhongMaterial {
                  val color: Color = mapColor(box.colorValue)
                  specularColor = color
                  diffuseColor = color
                }
              }
              )
              )
              val process: Unit = all(index).foreach { box =>
                shapes.children.add(box)
              }

              indexText.value = s"Step ${index+1} of ${all.length}"

            }
          }
          )
        }

        center = createView(tmpw, tmph)

        bottom = new HBox() {
          fill = Color.Black
          val label = new Label {
            text <== indexText
            minHeight = 50
          }
          label.delegate.textProperty().bind(indexText)
          alignment = Pos.Center
          minHeight = 50
          children = Seq(
            new Button {
              minHeight = 50
              text = "Previous Step"
              onAction = handle {

                if (index > 0) {
                  index -= 1
                  reRender()
                }
                indexText.value = s"Step ${index+1} of ${all.length}"

              }
            },
            label,
            new Button {
              minHeight = 50
              text = "Next step"
              onAction = handle {

                if(all.length - 1 > index) {
                  index += 1
                  reRender()
                }
                indexText.value = s"Step ${index+1} of ${all.length}"

              }
            })
        }

        left = new HBox() {
          fill = Color.Black
          minWidth = 200
          children = Seq(new Rectangle {

            width = 100
            height = 500
            fill = new LinearGradient(0, 1, 1, 0, true, NoCycle,
              Stops(
                color(0.15, 0.45, 0.95),
                color(0.2, 0.8, 0.95),
                color(0.2, 1.0, 0.85),
                color(0.5, 0.9, 0.2),
                color(0.9, 0.95, 0.2),
                color(1.0, 0.7, 0.0),
                color(1.0, 0.5, 0.1),
                color(1.0, 0.2, 0.0),
                color(1.0, 0.0, 0.0)
              )

            )
          },
            new VBox {

              children = Seq(
                new Label {
                  text = "1400"
                  alignment = Pos.TopLeft
                  minWidth = 100
                  minHeight = 250
                },
                new Label {
                  text = "20"
                  alignment = Pos.BottomLeft
                  minWidth = 100
                  minHeight = 250
                }
              )
              minWidth = 100
              minHeight = 500

            }
          )

        }

      }

      onKeyPressed = k => k.code match {
          case KeyCode.W => println("UP")
      }

    }
    width onChange show
    height onChange show

    addMouseInteraction(this.scene.value, shapes)
  }

  def reRender(): Unit = {
    shapes.children.clear()
    val process: Unit = all(index).foreach { box =>
      shapes.children.add(box)
    }
  }

  def mapColor(value: Float): Color = {

    var newColor: Color = color(0.15f, 0.45f, 0.95f)

    if (value >= 0 && value < 0.1f) {
      newColor = color(0.15, 0.45, 0.95)
    } else if (value >= 0.1f && value < 0.3f) {
      newColor = color(0.2, 0.8, 0.95)
    } else if (value >= 0.3f && value < 0.4f) {
      newColor = color(0.2, 1.0, 0.85)
    } else if (value >= 0.4f && value < 0.5f) {
      newColor = color(0.5, 0.9, 0.2)
    } else if (value >= 0.5f && value < 0.6f) {
      newColor = color(0.9, 0.95, 0.2)
    } else if (value >= 0.6f && value < 0.7f) {
      newColor = color(1.0, 0.7, 0.0)
    } else if (value >= 0.7f && value < 0.8f) {
      newColor = color(1.0, 0.5, 0.1)
    } else if (value >= 0.8f && value < 0.9f) {
      newColor = color(1.0, 0.2, 0.0)
    } else if (value >= 0.9f && value <= 1.0f) {
      newColor = color(1.0, 0.0, 0.0)
    }

    newColor

  }

  def createView(boundWidth : ReadOnlyDoubleProperty, boundHeight : ReadOnlyDoubleProperty):         BorderPane = {

    new BorderPane {

      center = new SubScene(boundWidth()-300, boundHeight()-50, true, SceneAntialiasing.Balanced) {
        fill = Color.Black

        // Create a tetrahedron and add to a mesh view. Configure it.
        cubeObj = new MeshView(cube(1.0)) {
          material = new PhongMaterial {
            specularColor = Color.LightBlue
            diffuseColor = Color.LightBlue
          }
        }

        // Put shapes in a group so they can be rotated together
        shapes = new Group(cubeObj) {

          scaleX = 10000.0
          scaleY = 10000.0
          scaleZ = 10000.0

        }

        val ambientLight = new AmbientLight {
          color = Color.White
        }

        root3D = new Group {
          children = new Group(shapes, ambientLight)
          translateX = 450.0
          translateY = 450.0
          translateZ = 2000.0
          rotationAxis = Rotate.YAxis
        }

        root = root3D

        camera = new PerspectiveCamera(false) {

          translateX = 0

        }

      }

    }

  }

  private def cube(length: Double): TriangleMesh = {
    require(length > 0.0)

    val mesh = new TriangleMesh()

    mesh.points = Array(
      -0.0070711f, 0.0f, 0.0f,
      -0.00565689f, -0.00141421f, 0.0f,
      -0.00424267f, 0.0f, 0.0f,
      -0.00565689f, 0.00141421f, 0.0f,
      -0.0070711f, 0.0f, 0.002f,
      -0.00565689f, -0.00141421f, 0.002f,
      -0.00424267f, 0.0f, 0.002f,
      -0.00565689f, 0.00141421f, 0.002f
    )

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

    println(mesh.points)
    println(mesh.faces)

    mesh

  }

  /** Add mouse interaction to a scene, rotating given node. */
  private def addMouseInteraction(scene: Scene, node: Node) {
    val angleY = DoubleProperty(0)
    val angleX = DoubleProperty(0)
    val yRotate = new Rotate {
      angle <== angleY
      axis = Rotate.YAxis
    }
    val xRotate = new Rotate {
      angle <== angleX
      axis = Rotate.XAxis
    }
    var anchorX: Double = 0
    var anchorAngleY: Double = 0
    var anchorY: Double = 0
    var anchorAngleX: Double = 0

    node.transforms = Seq(yRotate, xRotate)

    scene.onMousePressed = (event: MouseEvent) => {
      anchorX = event.sceneX
      anchorAngleY = angleY()
      anchorY = event.sceneY
      anchorAngleX = angleX()
    }
    scene.onMouseDragged = (event: MouseEvent) => {
      angleY() = anchorAngleY + anchorX - event.sceneX
      angleX() = anchorAngleX - anchorY + event.sceneY
    }
  }

}
