package org.nexis.vulkan.memory

import java.nio.FloatBuffer

trait ToFloatBuffer {

  def toFloatBuffer(b:FloatBuffer):FloatBuffer

}
