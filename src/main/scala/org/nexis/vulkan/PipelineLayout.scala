package org.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkPipelineLayoutCreateInfo}
import org.nexis.utils.closeable.*

class PipelineLayout(val device: Device) extends AutoCloseable{

  /** customize VkPipelineLayoutCreateInfo */
  protected def pipelineLayout(stack: MemoryStack, info: VkPipelineLayoutCreateInfo): Unit = {}

  protected def initPipelineLayout(): Long = MemoryStack.stackPush() | { stack =>
    val layout = VkPipelineLayoutCreateInfo.calloc(stack)
      .sType$Default()

    pipelineLayout(stack, layout)

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreatePipelineLayout(device.vkDevice, layout, null, lp))
    lp.get(0)
  }

  val vkPipelineLayout: Long = initPipelineLayout()

  override def close(): Unit = {
    VK10.vkDestroyPipelineLayout(device.vkDevice, vkPipelineLayout, null)
  }

}
