package renderer.utility

import renderer.constants.RenderMode
import renderer.constants.Screen
import renderer.core.Camera
import renderer.math.Matrix4
import renderer.math.Vector3
import renderer.mesh.Mesh3D
import renderer.mesh.Triangle3D

object Projector {
    fun transform(tris: MutableList<Triangle3D>, mesh: Mesh3D): MutableList<Triangle3D> {
        val transformed = mutableListOf<Triangle3D>()
        val world = Matrix4.world(mesh)
        for (tri in tris) {
            val triWorld = tri.copy()
            triWorld *= world
            transformed.add(triWorld)
        }
        return transformed
    }
    fun cull(tris: MutableList<Triangle3D>, pov: Camera): MutableList<Triangle3D> {
        val culled = mutableListOf<Triangle3D>()
        for (tri in tris) {
            val isFacingCamera = (tri.centroid() - pov.pos).dot(tri.normal()) < 0.0
            val triView = tri.copy()
            if (isFacingCamera || RenderMode.current == RenderMode.WIREFRAME || RenderMode.current >= RenderMode.LIGHT) {
                triView *= Matrix4.view(pov)
                culled.addAll(Clipper.depthClip(triView))
            }
        }
        return culled
    }
    fun project(tris: MutableList<Triangle3D>): MutableList<Triangle3D> {
        val projected = mutableListOf<Triangle3D>()
        for (tri in tris) {
            val triProjected = tri.copy()
            triProjected *= Matrix4.projection()
            perspectiveDivide(triProjected)
            scaleToScreen(triProjected)
            projected.addAll(Clipper.screenClip(triProjected))
        }
        return projected
    }

    private fun perspectiveDivide(tri: Triangle3D) {
        val w1 = 1.0 / tri.p1.w
        val w2 = 1.0 / tri.p2.w
        val w3 = 1.0 / tri.p3.w
        tri.p1 = Vector3(tri.p1.x*w1, tri.p1.y*w1, tri.p1.z*w1, w1)
        tri.p2 = Vector3(tri.p2.x*w2, tri.p2.y*w2, tri.p2.z*w2, w2)
        tri.p3 = Vector3(tri.p3.x*w3, tri.p3.y*w3, tri.p3.z*w3, w3)
    }
    private fun scaleToScreen(tri: Triangle3D) {
        tri.p1.x = (tri.p1.x + 1.0)*0.5*Screen.WIDTH
        tri.p2.x = (tri.p2.x + 1.0)*0.5*Screen.WIDTH
        tri.p3.x = (tri.p3.x + 1.0)*0.5*Screen.WIDTH
        tri.p1.y = (1.0 - tri.p1.y)*0.5*Screen.HEIGHT
        tri.p2.y = (1.0 - tri.p2.y)*0.5*Screen.HEIGHT
        tri.p3.y = (1.0 - tri.p3.y)*0.5*Screen.HEIGHT
    }
}