package renderer.mesh

import renderer.classifications.Renderable
import renderer.core.Camera
import renderer.math.Vector4
import renderer.utility.Rasterizer.cull
import renderer.utility.Rasterizer.project
import renderer.utility.Rasterizer.transform

class Mesh3D(val tris: MutableList<Triangle3D> = mutableListOf<Triangle3D>()): Renderable {
    var pos = Vector4()
    var axis = Vector4.worldUp()
    var referenceAxis = Vector4.worldUp()
    var spin = 0.0
    var pitch = 0.0; var yaw = 0.0; var roll = 0.0
    var scale = 1.0

    override fun trisToRender(pov: Camera): MutableList<Triangle3D> {
        val transformedTris = transform(tris, this)
        val culledTris = cull(transformedTris, pov)
        val projectedTris = project(culledTris)
        return projectedTris
    }

    companion object {
        fun loadOBJ(fileName: String): Mesh3D {
            val mesh = Mesh3D()
            val path = "/models/$fileName"
            val stream = Mesh3D::class.java.getResourceAsStream(path)
            if (stream == null) {
                println("Model not found: $path")
                return mesh
            }

            val vectorCache = mutableListOf<Vector4>()
            stream.bufferedReader().forEachLine { line ->
                val data = line.trim().split(" ")
                when (data[0]) {
                    "v" -> vectorCache.add(Vector4(
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