package renderer.mesh

import renderer.classifications.Renderable
import renderer.core.Camera
import renderer.math.Vector3
import renderer.utility.Projector

class Mesh3D(
    var pos : Vector3 = Vector3(),
    val tris: MutableList<Triangle3D> = mutableListOf<Triangle3D>()
): Renderable {
    var axis = Vector3.worldUp()
    var referenceAxis = Vector3.worldUp()
    var spin = 0.0
    var pitch = 0.0; var yaw = 0.0; var roll = 0.0
    var scale = 1.0

    override fun trisToRender(pov: Camera): MutableList<Triangle3D> {
        val transformedTris = Projector.transform(tris, this)
        val culledTris = Projector.cull(transformedTris, pov)
        val projectedTris = Projector.project(culledTris)
        return projectedTris
    }

    companion object {
        fun loadOBJ(fileName: String, pos: Vector3 = Vector3()): Mesh3D {
            val mesh = Mesh3D(pos)
            val path = "/models/$fileName"
            val stream = Mesh3D::class.java.getResourceAsStream(path)
            if (stream == null) {
                println("Model not found: $path")
                return mesh
            }

            val vectorCache = mutableListOf<Vector3>()
            stream.bufferedReader().forEachLine { line ->
                val data = line.trim().split(" ")
                when (data[0]) {
                    "v" -> vectorCache.add(Vector3(
                        data[1].toDouble(),
                        data[2].toDouble(),
                        data[3].toDouble()
                    ))
                    "f" -> mesh.tris.add(Triangle3D(
                        vectorCache[data[1].toInt() - 1],
                        vectorCache[data[2].toInt() - 1],
                        vectorCache[data[3].toInt() - 1]
                    ))
                }
            }
            return mesh
        }
    }
}