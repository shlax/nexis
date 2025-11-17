package org.erdeon.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkCommandPoolCreateInfo}
import org.erdeon.nexis.utils.closeable.*

class CommandPool(val device: Device, val queueFamilyIndex:Int, createReset: Boolean = true) extends AutoCloseable{

  protected def initCommandPool(createReset: Boolean): Long = MemoryStack.stackPush() | { stack =>
    val info = VkCommandPoolCreateInfo.calloc(stack)
      .sType$Default()
      .flags(if(createReset) VK10.VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT else 0)
      .queueFamilyIndex(queueFamilyIndex)

    val buff = stack.callocLong(1)
    vkCheck(VK10.vkCreateCommandPool(device.vkDevice, info, null, buff))
    buff.get(0)
  }

  val vkCommandPool: Long = initCommandPool(createReset)

  override def close(): Unit = {
    VK10.vkDestroyCommandPool(device.vkDevice, vkCommandPool, null)
  }
}
