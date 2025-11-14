package org.ecellon.nexis.model

import org.ecellon.nexis.math.{Vector2f, Vector3f}
import org.ecellon.nexis.vulkan.memory.{ToFloatBuffer, TypeLength}

import java.nio.FloatBuffer

class Vertex(val point:Vector3f, val normal:Vector3f, val uvs:Array[Vector2f]) extends ToFloatBuffer {

  override def toFloatBuffer(b: FloatBuffer): FloatBuffer = {
    point.toFloatBuffer(b)
    normal.toFloatBuffer(b)
    for(uv <- uvs) uv.toFloatBuffer(b)
    b
  }

  def size:Int = {
    TypeLength.floatLength(3 + 3) + TypeLength.floatLength(2 * uvs.length)
  }

}
