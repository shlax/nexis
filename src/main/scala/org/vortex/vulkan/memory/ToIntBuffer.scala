package org.vortex.vulkan.memory

import java.nio.IntBuffer

trait ToIntBuffer {

  def toIntBuffer(b:IntBuffer):IntBuffer

}
