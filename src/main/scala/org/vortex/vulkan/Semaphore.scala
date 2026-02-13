package org.vortex.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkSemaphoreCreateInfo}
import org.vortex.utils.closeable.*

class Semaphore(val device: Device) extends AutoCloseable{

  protected def initSemaphore():Long = MemoryStack.stackPush() | { stack =>
    val info = VkSemaphoreCreateInfo.calloc(stack)
      .sType$Default()

    val buff = stack.callocLong(1)
    vkCheck(VK10.vkCreateSemaphore(device.vkDevice, info, null, buff))
    buff.get(0)
  }

  val vkSemaphore:Long = initSemaphore()

  override def close(): Unit = {
    VK10.vkDestroySemaphore(device.vkDevice, vkSemaphore, null)
  }
}
