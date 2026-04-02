package renderer.utility

import renderer.math.Plane
import renderer.math.Vector3
import renderer.mesh.Triangle3D

object Clipper {
    fun depthClip(tri: Triangle3D): MutableList<Triangle3D> {
        return trianglesClippingAgainstPlane(Plane.depth(), tri)
    }
    fun screenClip(tri: Triangle3D): MutableList<Triangle3D> {
        val toClip = ArrayDeque<Triangle3D>()
        val planes = arrayOf(Plane.top(), Plane.bottom(), Plane.left(), Plane.right())
        toClip.addLast(tri)
        var newTrisCount = 1
        for (plane in planes) {
            while (newTrisCount > 0) {
                val currTri = toClip.removeFirst()
                newTrisCount--
                toClip.addAll(trianglesClippingAgainstPlane(plane, currTri))
            }
            newTrisCount = toClip.size
        }
        return toClip.toMutableList()
    }

    private fun trianglesClippingAgainstPlane(plane: Plane, tri: Triangle3D): MutableList<Triangle3D> {
        val trisClipped = mutableListOf<Triangle3D>()
        val insidePoints = mutableListOf<Vector3>()
        val outsidePoints = mutableListOf<Vector3>()
        for (point in listOf(tri.p1, tri.p2, tri.p3)) {
            if (distanceToPlane(plane, point) >= 0) insidePoints.add(point)
            else outsidePoints.add(point)
        }
        when (insidePoints.size) {
            0 -> Unit
            1 -> trisClipped.add(Triangle3D(
                insidePoints[0],
                vectorsIntersectingPlane(plane, insidePoints[0], outsidePoints[0]),
                vectorsIntersectingPlane(plane, insidePoints[0], outsidePoints[1])
            ))
            2 -> {
                val sharedPoint = vectorsIntersectingPlane(plane, insidePoints[0], outsidePoints[0])
                trisClipped.add(Triangle3D(
                    insidePoints[0],
                    insidePoints[1],
                    sharedPoint
                ))
                trisClipped.add(Triangle3D(
                    insidePoints[1],
                    sharedPoint,
                    vectorsIntersectingPlane(plane, insidePoints[1], outsidePoints[0])
                ))
            }
            3 -> trisClipped.add(tri)
        }
        return trisClipped
    }
    private fun vectorsIntersectingPlane(plane: Plane, lineStart: Vector3, lineEnd: Vector3): Vector3 {
        val planeD = -plane.normal.dot(plane.point)
        val ad = plane.normal.dot(lineStart)
        val bd = plane.normal.dot(lineEnd)
        val t = (-planeD - ad)/(bd - ad)
        return lineStart + (lineEnd - lineStart)*t
    }
    private fun distanceToPlane(plane: Plane, point: Vector3): Double {
        return plane.normal.dot(point) - plane.normal.dot(plane.point)
    }
}