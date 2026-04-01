package renderer.constants

enum class RenderMode {
    WIREFRAME, SOLID, COLOR;
    companion object {
        var current = WIREFRAME
    }
}