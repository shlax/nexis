package org.ecellon.nexis.model

import org.ecellon.nexis.vulkan.memory.ToFloatBuffer
import java.nio.FloatBuffer

abstract class VertexModel(val vertexes: Array[Vertex]) extends ToFloatBuffer {

  override def toFloatBuffer(b: FloatBuffer): FloatBuffer = {
    for(v <- vertexes) v.toFloatBuffer(b)
    b
  }

  def vertexesStride: Int = {
    vertexes.head.size
  }

  def vertexesSize : Int = {
    vertexes.length * vertexesStride
  }

  /** see vkCmdDrawIndexed */
  def indexesCount: Int

}
