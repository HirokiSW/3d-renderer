package renderer.math

import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt

data class Vector3(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var w: Double = 1.0
) {
    operator fun plus(s: Double) = Vector3(x+s, y+s, z+s, w)
    operator fun minus(s: Double) = Vector3(x-s, y-s, z-s, w)
    operator fun times(s: Double) = Vector3(x*s, y*s, z*s, w)
    operator fun div(s: Double) = if (s == 0.0) Vector3() else Vector3(x/s, y/s, z/s, w)

    operator fun plus(v: Vector3) = Vector3(x+v.x, y+v.y, z+v.z, w)
    operator fun minus(v: Vector3) = Vector3(x-v.x, y-v.y, z-v.z, w)
    operator fun unaryMinus() = Vector3(-x, -y, -z, w)

    fun dot(v: Vector3) = x*v.x + y*v.y + z*v.z
    fun cross(v: Vector3) = Vector3(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x, w)
    fun length() = sqrt(dot(this))
    fun normalize() = this/length()
    fun lerp(target: Vector3, factor: Double) = this + (target - this)*factor

    fun rotate(axis: Vector3, angRad: Double): Vector3 {
        val k = axis.normalize()
        val v1 = this*cos(angRad)
        val v2 = k.cross(this)*sin(angRad)
        val v3 = k*(k.dot(this)*(1.0 - cos(angRad)))
        return v1 + v2 + v3
    }

    companion object {
        fun worldRight() = Vector3(1.0, 0.0, 0.0)
        fun worldUp() = Vector3(0.0, 1.0, 0.0)
        fun worldForward() = Vector3(0.0, 0.0, 1.0)
    }
}
