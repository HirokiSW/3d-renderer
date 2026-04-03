package renderer.window

import renderer.classifications.Buildable
import renderer.classifications.Drawable
import renderer.classifications.Renderable
import renderer.classifications.Updatable
import renderer.constants.Perspective
import renderer.constants.Screen
import renderer.core.Camera
import renderer.core.Controller
import renderer.mesh.Triangle3D
import renderer.scene.TestScene
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JComponent

class Canvas: JComponent() {
    val pov = Camera()
    val player = Controller(pov)
    val renderables = mutableListOf<Renderable>()
    val drawables = mutableListOf<Drawable>()
    val updatables = mutableListOf<Updatable>()
    val buildables = mutableListOf<Buildable>()

    init {
        preferredSize = Dimension(Screen.WIDTH, Screen.HEIGHT)
        isFocusable = true
        requestFocusInWindow()
        addKeyListener(player)
        updatables.add(pov)
        buildables.add(TestScene())
        buildables.forEach { it.build(renderables, updatables, drawables) }
    }

    override fun paintComponent(g: Graphics) {
        Screen.WIDTH = width
        Screen.HEIGHT = height
        Perspective.ASPECT_RATIO = Screen.WIDTH.toDouble()/Screen.HEIGHT
        val g2d = g as Graphics2D
        val revert = g2d.transform
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        g2d.background = Color.WHITE
        g2d.clearRect(0, 0, width, height)
        renderGraphics(g2d)
        g2d.transform = revert
    }

    fun renderGraphics(g2d: Graphics2D) {
        val allTris = mutableListOf<Triangle3D>()
        renderables.forEach { rdr -> allTris.addAll(rdr.trisToRender(pov)) }
        allTris.sortByDescending { it.viewDepth() }
        allTris.forEach { it.draw(g2d) }
        drawables.forEach { it.draw(g2d) }
    }

    fun updatePositions(timeElapsedInMillis: Int) {
        updatables.forEach { it.updateObject(timeElapsedInMillis) }
    }
}