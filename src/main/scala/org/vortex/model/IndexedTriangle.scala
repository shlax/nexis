package org.vortex.model

import org.vortex.vulkan.memory.{ToIntBuffer, TypeLength}

import java.nio.IntBuffer

class IndexedTriangle(val a:Int, val b:Int, val c:Int) extends ToIntBuffer{

  override def toIntBuffer(buff: IntBuffer): IntBuffer = {
    buff.put(a).put(b).put(c)
  }

  def size:Int = {
    TypeLength.intLength(3)
  }

}
