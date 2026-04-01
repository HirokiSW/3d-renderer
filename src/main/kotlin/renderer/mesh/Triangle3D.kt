package renderer.mesh

import renderer.classifications.Drawable
import renderer.constants.Properties
import renderer.constants.RenderMode
import renderer.constants.Screen
import renderer.core.Camera
import renderer.math.Matrix4
import renderer.math.Vector4
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.geom.Path2D

data class Triangle3D(
    var p1: Vector4 = Vector4(),
    var p2: Vector4 = Vector4(),
    var p3: Vector4 = Vector4()
): Drawable {
    override fun draw(g2d: Graphics2D) {
        val revert = g2d.transform
        val path = Path2D.Double()
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.closePath()
        when (RenderMode.current) {
            RenderMode.WIREFRAME -> {
                g2d.stroke = BasicStroke(Properties.OUTLINE_WIDTH)
                g2d.color = Properties.OUTLINE_COLOR
                g2d.draw(path)
            }
            RenderMode.SOLID -> {
                g2d.color = Properties.FILL_COLOR
                g2d.fill(path)
                g2d.stroke = BasicStroke(Properties.OUTLINE_WIDTH)
                g2d.color = Properties.OUTLINE_COLOR
                g2d.draw(path)
            }
            RenderMode.COLOR -> {
                //
            }
        }
        g2d.transform = revert
    }

    operator fun timesAssign(m: Matrix4) { p1 = m*p1; p2 = m*p2; p3 = m*p3 }

    fun centroid() = (p1 + p2 + p3)/3.0
    fun normal() = ((p2 - p1).cross(p3 - p2)).normalize()
    fun viewDepth() = (p1.z + p2.z + p3.z)/3.0
    fun facingCamera(pov: Camera) = (centroid() - pov.pos).dot(normal()) < 0.0
}