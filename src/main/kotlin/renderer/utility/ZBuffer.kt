package renderer.utility

import renderer.classifications.Drawable
import renderer.constants.Properties
import renderer.constants.RenderMode
import renderer.constants.Screen
import renderer.math.Vector3
import renderer.mesh.Triangle3D
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt

object ZBuffer: Drawable {
    private val depth = DoubleArray(Screen.WIDTH*Screen.HEIGHT)
    val image = BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_ARGB)
    private val pixels = (image.raster.dataBuffer as DataBufferInt).data

    override fun draw(g2d: Graphics2D) {
        g2d.drawImage(image, 0, 0, null)
    }

    fun clear() {
        depth.fill(Double.MAX_VALUE)
        pixels.fill(0x00000000)
    }

    fun rasterize(tri: Triangle3D) {
        val p1 = tri.p1; val p2 = tri.p2; val p3 = tri.p3
        val minX = maxOf(0, minOf(p1.x, p2.x, p3.x).toInt())
        val maxX = minOf(Screen.WIDTH - 1, maxOf(p1.x, p2.x, p3.x).toInt())
        val minY = maxOf(0, minOf(p1.y, p2.y, p3.y).toInt())
        val maxY = minOf(Screen.HEIGHT - 1, maxOf(p1.y, p2.y, p3.y).toInt())

        val area = edgeFunction(p1, p2, p3.x, p3.y)
        if (area == 0.0) return

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val px = x + 0.5
                val py = y + 0.5

                val w1 = edgeFunction(p2, p3, px, py)/area
                val w2 = edgeFunction(p3, p1, px, py)/area
                val w3 = 1.0 - w1 - w2
                if (w1 < 0.0 || w2 < 0.0 || w3 < 0.0) continue

                val zOverW = w1*p1.z + w2*p2.z + w3*p3.z
                val oneOverW = w1*p1.w + w2*p2.w + w3*p3.w
                val z = zOverW/oneOverW

                val idx = y*Screen.WIDTH + x
                if (z < depth[idx]) {
                    depth[idx]  = z
                    when (RenderMode.current) {
                        RenderMode.LIGHT -> pixels[idx] = Properties.DEFAULT_FILL_COLOR.rgb
                        RenderMode.COLOR -> pixels[idx] = tri.color.rgb
                        else -> { }
                    }
                }
            }
        }
    }

    private fun edgeFunction(a: Vector3, b: Vector3, px: Double, py: Double): Double {
        return (b.x - a.x)*(py - a.y) - (b.y - a.y)*(px - a.x)
    }
}