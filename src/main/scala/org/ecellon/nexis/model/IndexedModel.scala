package org.ecellon.nexis.model

import org.ecellon.nexis.vulkan.memory.ToIntBuffer
import java.nio.IntBuffer

class IndexedModel(vertexes: Array[Vertex], val indexes:Array[IndexedTriangle]) extends VertexModel(vertexes), ToIntBuffer {

  override def toIntBuffer(b: IntBuffer): IntBuffer = {
    for (i <- indexes) i.toIntBuffer(b)
    b
  }

  def indexesStride: Int = {
    indexes.head.size
  }

  def indexesSize: Int = {
    indexes.length * indexesStride
  }

  override def indexesCount: Int ={
    indexes.length * 3
  }

}
