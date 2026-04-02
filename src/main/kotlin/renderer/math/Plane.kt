package renderer.math

import renderer.constants.Perspective
import renderer.constants.Screen

data class Plane(val point: Vector3, val normal: Vector3) {
    companion object {
        fun depth() = Plane(
            Vector3(0.0, 0.0, Perspective.Z_NEAR),
            Vector3.worldForward()
        )
        fun top() = Plane(
            Vector3(0.0, 0.0, 0.0),
            Vector3.worldUp()
        )
        fun bottom() = Plane(
            Vector3(0.0, Screen.HEIGHT - 1.0, 0.0),
            -Vector3.worldUp()
        )
        fun left() = Plane(
            Vector3(0.0, 0.0, 0.0),
            Vector3.worldRight()
        )
        fun right() = Plane(
            Vector3(Screen.WIDTH - 1.0, 0.0, 0.0),
            -Vector3.worldRight()
        )
    }
}
