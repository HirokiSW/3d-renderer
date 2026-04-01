package renderer.constants

import kotlin.math.PI
import kotlin.math.tan

object Perspective {
    var ASPECT_RATIO = Screen.WIDTH.toDouble()/Screen.HEIGHT
    const val ANGLE_OF_VIEW = 90.0*PI/180.0
    const val Z_FAR = 1000.0
    const val Z_NEAR = 0.1
}
