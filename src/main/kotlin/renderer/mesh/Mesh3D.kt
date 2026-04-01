package renderer.mesh

import renderer.math.Vector4

class Mesh3D {
    val tris = mutableListOf<Triangle3D>()
    var pos = Vector4()
    var axis = Vector4.worldUp()
    var referenceAxis = Vector4.worldUp()
    var spin = 0.0
    var pitch = 0.0; var yaw = 0.0; var roll = 0.0
    var scale = 1.0

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