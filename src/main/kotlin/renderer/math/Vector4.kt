package renderer.math

import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt

data class Vector4(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val w: Double = 1.0
) {
    operator fun plus(s: Double) = Vector4(x+s, y+s, z+s, w)
    operator fun minus(s: Double) = Vector4(x-s, y-s, z-s, w)
    operator fun times(s: Double) = Vector4(x*s, y*s, z*s, w)
    operator fun div(s: Double) = if (s == 0.0) Vector4() else Vector4(x/s, y/s, z/s, w)

    operator fun plus(v: Vector4) = Vector4(x+v.x, y+v.y, z+v.z, w)
    operator fun minus(v: Vector4) = Vector4(x-v.x, y-v.y, z-v.z, w)
    operator fun unaryMinus() = Vector4(-x, -y, -z, w)

    fun dot(v: Vector4) = x*v.x + y*v.y + z*v.z
    fun cross(v: Vector4) = Vector4(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x, w)
    fun length() = sqrt(dot(this))
    fun normalize() = this/length()
    fun lerp(target: Vector4, factor: Double) = this + (target - this)*factor

    fun rotate(axis: Vector4, angRad: Double): Vector4 {
        val k = axis.normalize()
        val v1 = this*cos(angRad)
        val v2 = k.cross(this)*sin(angRad)
        val v3 = k*(k.dot(this)*(1.0 - cos(angRad)))
        return v1 + v2 + v3
    }

    companion object {
        fun worldRight() = Vector4(1.0, 0.0, 0.0)
        fun worldUp() = Vector4(0.0, 1.0, 0.0)
        fun worldForward() = Vector4(0.0, 0.0, 1.0)
    }
}
