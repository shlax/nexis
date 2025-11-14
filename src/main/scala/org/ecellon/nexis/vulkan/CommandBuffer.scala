package org.ecellon.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkCommandBuffer, VkCommandBufferAllocateInfo, VkCommandBufferBeginInfo}
import org.ecellon.nexis.utils.closeable.*

import java.util.function.Consumer

class CommandBuffer(val commandPool: CommandPool, primary:Boolean = true) extends AutoCloseable{

  protected def initCommandBuffer(primary:Boolean):VkCommandBuffer = MemoryStack.stackPush() | { stack =>
    val info = VkCommandBufferAllocateInfo.calloc(stack)
      .sType$Default()
      .commandPool(commandPool.vkCommandPool)
      .level(if(primary) VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY else VK10.VK_COMMAND_BUFFER_LEVEL_SECONDARY)
      .commandBufferCount(1)

    val vkDevice = commandPool.device.vkDevice

    val buff = stack.callocPointer(1)
    vkCheck(VK10.vkAllocateCommandBuffers(vkDevice, info, buff))
    val cmdBuff = VkCommandBuffer(buff.get(0), vkDevice)

    cmdBuff
  }

  val vkCommandBuffer:VkCommandBuffer = initCommandBuffer(primary)

  protected def createBeginInfo(stack: MemoryStack, oneTimeSubmit:Boolean): VkCommandBufferBeginInfo = {
    val info = VkCommandBufferBeginInfo.calloc(stack)
      .sType$Default()

    if (oneTimeSubmit) {
      info.flags(VK10.VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT)
    }

    info
  }

  def record(stack:MemoryStack, oneTimeSubmit:Boolean = true)(f: Consumer[VkCommandBuffer]):this.type = {
    val i = createBeginInfo(stack, oneTimeSubmit)
    vkCheck(VK10.vkBeginCommandBuffer(vkCommandBuffer, i))

    try {
      f.accept(vkCommandBuffer)
    } finally {
      vkCheck(VK10.vkEndCommandBuffer(vkCommandBuffer))
    }

    this
  }

  override def close(): Unit = {
    VK10.vkFreeCommandBuffers(commandPool.device.vkDevice, commandPool.vkCommandPool, vkCommandBuffer)
  }

}
