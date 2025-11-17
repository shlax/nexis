package org.erdeon.nexis.vulkan.memory

import java.nio.IntBuffer

trait ToIntBuffer {

  def toIntBuffer(b:IntBuffer):IntBuffer

}
