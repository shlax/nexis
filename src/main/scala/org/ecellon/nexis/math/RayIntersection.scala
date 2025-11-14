package org.ecellon.nexis.math

class RayIntersection {

  private val e1 = Vector3f()
  private val e2 = Vector3f()

  private val vp = Vector3f()
  private val vq = Vector3f()
  private val vt = Vector3f()

  /** https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm */
  def intersect(r: Ray3f, t: Triangle3f): Float = {
    e1.subtract(t.b, t.a)
    e2.subtract(t.c, t.a)

    vp.cross(r.dir, e2)

    val det = e1.dot(vp)
    if (det == 0) Float.NaN
    else {
      val iDet = 1f / det

      vt.subtract(r.point, t.a)

      val u = vt.dot(vp) * iDet
      if (u < 0 || u > 1) Float.NaN
      else {
        vq.cross(vt, e1)

        val v = r.dir.dot(vq) * iDet
        if (v < 0 || u + v > 1) Float.NaN
        else e2.dot(vq) * iDet
      }
    }
  }

}
