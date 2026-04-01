package renderer.classifications

import renderer.mesh.Triangle3D

interface Renderable {
    val trisToRender: MutableList<Triangle3D>
}