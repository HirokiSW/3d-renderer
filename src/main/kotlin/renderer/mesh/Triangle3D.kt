package renderer.mesh

import renderer.classifications.Drawable
import renderer.constants.ColorType
import renderer.constants.Properties
import renderer.constants.RenderMode
import renderer.math.Matrix4
import renderer.math.Vector3
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.geom.Path2D

data class Triangle3D(
    var p1: Vector3 = Vector3(),
    var p2: Vector3 = Vector3(),
    var p3: Vector3 = Vector3(),
): Drawable {
    var thickness = 0.5f
    val luminosity = mutableMapOf<ColorType, Float>()
    
    override fun draw(g2d: Graphics2D) {
        val revert = g2d.transform
        val path = Path2D.Double()
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.closePath()
        when (RenderMode.current) {
            RenderMode.WIREFRAME -> {
                g2d.stroke = BasicStroke(thickness)
                g2d.color = Properties.OUTLINE_COLOR
                g2d.draw(path)
            }
            RenderMode.SOLID -> {
                g2d.color = Properties.SOLID_FILL_COLOR
                g2d.fill(path)
                g2d.stroke = BasicStroke(thickness)
                g2d.color = Properties.OUTLINE_COLOR
                g2d.draw(path)
            }
            RenderMode.COLOR -> {
                TODO("Not yet implemented")
            }
        }
        g2d.transform = revert
    }

    operator fun timesAssign(m: Matrix4) { p1 = m*p1; p2 = m*p2; p3 = m*p3 }

    fun copyProperties(tri: Triangle3D) {
        thickness = tri.thickness
        luminosity[ColorType.RED] = tri.luminosity[ColorType.RED] ?: 0.0f
        luminosity[ColorType.BLUE] = tri.luminosity[ColorType.BLUE] ?: 0.0f
        luminosity[ColorType.GREEN] = tri.luminosity[ColorType.GREEN] ?: 0.0f
    }
    fun centroid() = (p1 + p2 + p3)/3.0
    fun normal() = ((p2 - p1).cross(p3 - p1)).normalize()
    fun viewDepth() = (p1.z + p2.z + p3.z)/3.0
}