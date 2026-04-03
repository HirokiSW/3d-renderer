package renderer.classifications

interface Buildable {
    fun build(
        renderables: MutableList<Renderable>,
        updatables: MutableList<Updatable>,
        drawable: MutableList<Drawable>
    )
}