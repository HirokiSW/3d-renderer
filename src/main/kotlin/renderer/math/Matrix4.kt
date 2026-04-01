package renderer.math

import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan

data class Matrix4(val m: DoubleArray = DoubleArray(16)) {
    override fun equals(other: Any?): Boolean {
        if (other !is Matrix4) return false
        return m.contentEquals(other.m)
    }
    override fun hashCode() = m.contentHashCode()
    override fun toString(): String {
        val rows = (0..3).map { row ->
            val values = (0..3).map { col -> "%.4f".format(this[row, col]) }
            val joined = values.joinToString("  ")
            "| $joined |"
        }
        return rows.joinToString("\n")
    }
    operator fun get(row: Int, col: Int): Double = m[col*4 + row]
    operator fun set(row: Int, col: Int, value: Double) { m[col*4 + row] = value }

    operator fun times(v: Vector4) = Vector4(
        v.x*this[0, 0] + v.y*this[1, 0] + v.z*this[2, 0] + v.w*this[3, 0],
        v.x*this[0, 1] + v.y*this[1, 1] + v.z*this[2, 1] + v.w*this[3, 1],
        v.x*this[0, 2] + v.y*this[1, 2] + v.z*this[2, 2] + v.w*this[3, 2],
        v.x*this[0, 3] + v.y*this[1, 3] + v.z*this[2, 3] + v.w*this[3, 3]
    )
    operator fun times(m2: Matrix4): Matrix4 {
        val result = Matrix4()
        for (row in 0..3) {
            for (col in 0..3) {
                result[row, col] = (0..3).sumOf { i -> this[row, i]*m2[i, col] }
            }
        }
        return result
    }

    companion object {
        fun identity() = Matrix4(doubleArrayOf(
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        ))

        fun projection(aspectRatio: Double, angleOfView: Double, zNear: Double, zFar: Double): Matrix4 {
            val fov = 1.0/tan(angleOfView/2.0)
            val depthNormalize = zFar/(zFar - zNear)
            val depthOffset = (-zFar*zNear)/(zFar - zNear)
            return Matrix4(doubleArrayOf(
                fov/aspectRatio, 0.0, 0.0, 0.0,
                0.0, fov, 0.0, 0.0,
                0.0, 0.0, depthNormalize, 1.0,
                0.0, 0.0, depthOffset, 0.0
            ))
        }
        fun translation(pos: Vector4) = Matrix4(doubleArrayOf(
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            pos.x, pos.y, pos.z, 1.0
        ))
        fun scale(s: Double) = Matrix4(doubleArrayOf(
            s, 0.0, 0.0, 0.0,
            0.0, s, 0.0, 0.0,
            0.0, 0.0, s, 0.0,
            0.0, 0.0, 0.0, 1.0
        ))

        fun rotationX(angRad: Double): Matrix4 {
            val c = cos(angRad)
            val s = sin(angRad)
            return Matrix4(doubleArrayOf(
                1.0, 0.0, 0.0, 0.0,
                0.0, c, s, 0.0,
                0.0, -s, c, 0.0,
                0.0, 0.0, 0.0, 1.0
            ))
        }
        fun rotationY(angRad: Double): Matrix4 {
            val c = cos(angRad)
            val s = sin(angRad)
            return Matrix4(doubleArrayOf(
                c, 0.0, -s, 0.0,
                0.0, 1.0, 0.0, 0.0,
                s, 0.0, c, 0.0,
                0.0, 0.0, 0.0, 1.0
            ))
        }
        fun rotationZ(angRad: Double): Matrix4 {
            val c = cos(angRad);
            val s = sin(angRad)
            return Matrix4(doubleArrayOf(
                c, s, 0.0, 0.0,
                -s, c, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
            ))
        }

        fun rotationSpin(axis: Vector4, angRad: Double): Matrix4 {
            val x = axis.x; val y = axis.y; val z = axis.z
            val c = cos(angRad);
            val s = sin(angRad);
            val t = 1.0 - c
            return Matrix4(doubleArrayOf(
                t*x*x + c, t*x*y + s*z, t*x*z - s*y, 0.0,
                t*x*y - s*z, t*y*y + c, t*y*z + s*x, 0.0,
                t*x*z + s*y, t*y*z - s*x, t*z*z + c, 0.0,
                0.0, 0.0, 0.0, 1.0
            ))
        }
        fun rotationTilt(newAxis: Vector4, worldAxis: Vector4): Matrix4 {
            val axis = worldAxis.cross(newAxis)
            if (axis.length() < 1e-6) return identity()
            val angRad = acos(worldAxis.dot(newAxis))
            return rotationSpin(axis.normalize(), angRad)
        }

        fun camera(pos: Vector4, right: Vector4, up: Vector4, forward: Vector4) = Matrix4(doubleArrayOf(
            right.x, right.y, right.z, 0.0,
            up.x, up.y, up.z, 0.0,
            forward.x, forward.y, forward.z, 0.0,
            pos.x, pos.y, pos.z, 1.0
        ))
        fun view(pos: Vector4, right: Vector4, up: Vector4, forward: Vector4) = Matrix4(doubleArrayOf(
            right.x, up.x, forward.x, 0.0,
            right.y, up.y, forward.y, 0.0,
            right.z, up.z, forward.z, 0.0,
            -pos.dot(right), -pos.dot(up), -pos.dot(forward), 1.0
        ))
    }
}
