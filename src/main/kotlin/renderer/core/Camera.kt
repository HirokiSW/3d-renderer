package renderer.core

import renderer.classifications.Updatable
import renderer.constants.Direction
import renderer.constants.Properties
import renderer.math.Vector3

class Camera(var pos: Vector3 = Vector3()): Updatable {
    var right = Vector3.worldRight()
    var up = Vector3.worldUp()
    var forward = Vector3.worldForward()
    var speed = Properties.DEFAULT_CAMERA_SPEED
    var rotSpeed = Properties.DEFAULT_CAMERA_ROT_SPEED
    val isMoving = mutableMapOf<Direction, Boolean>()
    val isRotating = mutableMapOf<Direction, Boolean>()

    override fun updateObject(timeElapsedInMillis: Int) {
        handleMovement(timeElapsedInMillis)
        handleRotation(timeElapsedInMillis)
    }
    private fun handleMovement(timeElapsedInMillis: Int) {
        val dt = timeElapsedInMillis/1000.0
        if (isMoving[Direction.RIGHT] == true)      move(right, speed*dt)
        if (isMoving[Direction.LEFT] == true)       move(right, -speed*dt)
        if (isMoving[Direction.UP] == true)         move(up, speed*dt)
        if (isMoving[Direction.DOWN] == true)       move(up, -speed*dt)
        if (isMoving[Direction.FORWARD] == true)    move(forward, speed*dt)
        if (isMoving[Direction.BACKWARD] == true)   move(forward, -speed*dt)
    }
    private fun handleRotation(timeElapsedInMillis: Int) {
        val dt = timeElapsedInMillis/1000.0
        if (isRotating[Direction.RIGHT] == true)    yaw(rotSpeed*dt)
        if (isRotating[Direction.LEFT] == true)     yaw(-rotSpeed*dt)
        if (isRotating[Direction.UP] == true)       pitch(rotSpeed*dt)
        if (isRotating[Direction.DOWN] == true)     pitch(-rotSpeed*dt)
        if (isRotating[Direction.FORWARD] == true)  roll(rotSpeed*dt)
        if (isRotating[Direction.BACKWARD] == true) roll(-rotSpeed*dt)
    }

    private fun move(axis: Vector3, displacement: Double) {
        pos += axis*displacement
    }
    private fun pitch(angularDisplacement: Double) {
        forward = forward.rotate(right, angularDisplacement)
        up = up.rotate(right, angularDisplacement)
        normalizeAxes()
    }
    private fun yaw(angularDisplacement: Double) {
        forward = forward.rotate(up, angularDisplacement)
        right = right.rotate(up, angularDisplacement)
        normalizeAxes()
    }
    private fun roll(angularDisplacement: Double) {
        right = right.rotate(forward, angularDisplacement)
        up = up.rotate(forward, angularDisplacement)
        normalizeAxes()
    }
    private fun normalizeAxes() {
        forward = forward.normalize()
        right = up.cross(forward).normalize()
        up = forward.cross(right).normalize()
    }
}