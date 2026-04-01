package renderer.window

import renderer.constants.Screen
import javax.swing.JFrame
import javax.swing.Timer

class Frame: JFrame() {
    private val canvas = Canvas()

    fun setUpGUI() {
        title = "3D Renderer"
        add(canvas)
        pack()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
        isResizable = true
        startAnimation()
    }

    private fun startAnimation() {
        Timer(Screen.MILLISECONDS_PER_FRAME) {
            canvas.updatePositions(Screen.MILLISECONDS_PER_FRAME)
            canvas.repaint()
        }.start()
    }
}