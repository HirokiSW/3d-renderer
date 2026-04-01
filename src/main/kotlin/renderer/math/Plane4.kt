package renderer.math

import renderer.constants.Perspective
import renderer.constants.Screen

data class Plane4(val point: Vector4, val normal: Vector4) {
    companion object {
        fun depth() = Plane4(
            Vector4(0.0, 0.0, Perspective.Z_NEAR),
            Vector4.worldForward()
        )
        fun top() = Plane4(
            Vector4(0.0, 0.0, 0.0),
            Vector4.worldUp()
        )
        fun bottom() = Plane4(
            Vector4(0.0, Screen.HEIGHT - 1.0, 0.0),
            -Vector4.worldUp()
        )
        fun left() = Plane4(
            Vector4(0.0, 0.0, 0.0),
            Vector4.worldRight()
        )
        fun right() = Plane4(
            Vector4(Screen.WIDTH - 1.0, 0.0, 0.0),
            -Vector4.worldRight()
        )
    }
}
