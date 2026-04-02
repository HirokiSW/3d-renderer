package renderer.classifications

import renderer.core.Camera
import renderer.mesh.Triangle3D

interface Renderable {
    fun trisToRender(pov: Camera): MutableList<Triangle3D>
}