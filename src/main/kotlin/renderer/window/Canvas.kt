package renderer.window

import renderer.classifications.Renderable
import renderer.classifications.Updatable
import renderer.constants.Perspective
import renderer.constants.Screen
import renderer.core.Camera
import renderer.core.Controller
import renderer.mesh.Triangle3D
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

class Canvas: JComponent() {
    val pov = Camera()
    val player = Controller(pov)
    val renderables = mutableListOf<Renderable>()
    val updatables = mutableListOf<Updatable>()

    init {
        preferredSize = Dimension(Screen.WIDTH, Screen.HEIGHT)
        isFocusable = true
        requestFocusInWindow()
        addKeyListener(player)
    }

    override fun paintComponent(g: Graphics) {
        Screen.WIDTH = width
        Screen.HEIGHT = height
        Perspective.ASPECT_RATIO = Screen.WIDTH.toDouble()/Screen.HEIGHT
        val g2d = g as Graphics2D
        val revert = g2d.transform
        g2d.background = Color.BLACK
        g2d.clearRect(0, 0, width, height)
        renderGraphics(g2d)
        g2d.transform = revert
    }

    fun renderGraphics(g2d: Graphics2D) {
        val allTris = mutableListOf<Triangle3D>()
        for (rdr in renderables) {
            allTris.addAll(rdr.trisToRender)
        }
        allTris.sortByDescending { it.viewDepth() }
        for (tri in allTris) {
            tri.draw(g2d)
        }
    }

    fun updatePositions(timeElapsedInMillis: Int) {
        for (updatable in updatables) {
            updatable.updateObject(timeElapsedInMillis)
        }
    }
}