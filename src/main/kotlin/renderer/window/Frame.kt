package renderer.window

import javax.swing.JFrame
import javax.swing.Timer

class Frame: JFrame() {
    private val canvas = Canvas()
    companion object {
        val MILLIS_PER_FRAME = 20
        val FRAMES_PER_SECOND = 1/(MILLIS_PER_FRAME/1000.0)
    }

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
        Timer(MILLIS_PER_FRAME) {
            canvas.updatePositions(MILLIS_PER_FRAME)
            canvas.repaint()
        }.start()
    }
}