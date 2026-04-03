package renderer.core

import renderer.constants.Direction
import renderer.constants.Properties
import renderer.constants.RenderMode
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class Controller(val pov: Camera): KeyListener {
    override fun keyTyped(e: KeyEvent?) { }
    override fun keyPressed(e: KeyEvent?) {
        when (e?.keyCode) {
            KeyEvent.VK_D     -> pov.isMoving[Direction.RIGHT]      = true
            KeyEvent.VK_A     -> pov.isMoving[Direction.LEFT]       = true
            KeyEvent.VK_W     -> pov.isMoving[Direction.FORWARD]    = true
            KeyEvent.VK_S     -> pov.isMoving[Direction.BACKWARD]   = true
            KeyEvent.VK_SPACE -> pov.isMoving[Direction.UP]         = true
            KeyEvent.VK_SHIFT -> pov.isMoving[Direction.DOWN]       = true

            KeyEvent.VK_RIGHT -> pov.isRotating[Direction.RIGHT]    = true
            KeyEvent.VK_LEFT  -> pov.isRotating[Direction.LEFT]     = true
            KeyEvent.VK_E     -> pov.isRotating[Direction.FORWARD]  = true
            KeyEvent.VK_Q     -> pov.isRotating[Direction.BACKWARD] = true
            KeyEvent.VK_UP    -> pov.isRotating[if (Properties.INVERTED_PITCH) Direction.DOWN else Direction.UP] = true
            KeyEvent.VK_DOWN  -> pov.isRotating[if (Properties.INVERTED_PITCH) Direction.UP else Direction.DOWN] = true

            KeyEvent.VK_1     -> RenderMode.switchTo(RenderMode.WIREFRAME)
            KeyEvent.VK_2     -> RenderMode.switchTo(RenderMode.SOLID)
            KeyEvent.VK_3     -> RenderMode.switchTo(RenderMode.COLOR)
        }
    }
    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyCode) {
            KeyEvent.VK_D     -> pov.isMoving[Direction.RIGHT]      = false
            KeyEvent.VK_A     -> pov.isMoving[Direction.LEFT]       = false
            KeyEvent.VK_W     -> pov.isMoving[Direction.FORWARD]    = false
            KeyEvent.VK_S     -> pov.isMoving[Direction.BACKWARD]   = false
            KeyEvent.VK_SPACE -> pov.isMoving[Direction.UP]         = false
            KeyEvent.VK_SHIFT -> pov.isMoving[Direction.DOWN]       = false

            KeyEvent.VK_RIGHT -> pov.isRotating[Direction.RIGHT]    = false
            KeyEvent.VK_LEFT  -> pov.isRotating[Direction.LEFT]     = false
            KeyEvent.VK_E     -> pov.isRotating[Direction.FORWARD]  = false
            KeyEvent.VK_Q     -> pov.isRotating[Direction.BACKWARD] = false
            KeyEvent.VK_UP    -> pov.isRotating[if (Properties.INVERTED_PITCH) Direction.DOWN else Direction.UP] = false
            KeyEvent.VK_DOWN  -> pov.isRotating[if (Properties.INVERTED_PITCH) Direction.UP else Direction.DOWN] = false
        }
    }
}