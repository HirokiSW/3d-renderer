package renderer.constants

enum class RenderMode {
    WIREFRAME, SOLID, LIGHT, COLOR;
    companion object {
        var current = SOLID
        fun switchTo(mode: RenderMode) { current = mode }
    }
}