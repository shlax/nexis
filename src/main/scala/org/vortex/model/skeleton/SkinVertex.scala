package org.vortex.model.skeleton

import org.vortex.math.{Matrix4f, Vector3f}
import org.vortex.model.Vertex

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object SkinVertex {

  def apply(offset:Vector3f, points:Array[Vertex]):SkinVertex = {
    val point = points.head.point
    val normals = ArrayBuffer[SkinNormal]()

    for(v <- points){
      if(!point.eq(v.point)) {
        throw IllegalArgumentException("" + point + " != " + v.point)
      }

      normals += normals.find(_.is(v.normal)).getOrElse(SkinNormal(v.normal))
    }

    new SkinVertex(Vector3f(point).subtract(offset), point, normals.toArray)
  }

}

class SkinVertex(val inPoint:Vector3f, val outPoint:Vector3f, val normals:Array[SkinNormal]) {

  def apply(modelMatrix:Matrix4f, normalMatrix:Matrix4f):Unit = {
    modelMatrix(inPoint, outPoint)
    for(n <- normals) n(normalMatrix)
  }

}
