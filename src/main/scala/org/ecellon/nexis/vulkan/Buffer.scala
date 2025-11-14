package org.ecellon.nexis.vulkan

import org.ecellon.nexis.vulkan.memory.MemoryBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkBufferCreateInfo, VkMemoryRequirements}
import org.ecellon.nexis.utils.closeable.*

import java.util.function.Consumer

class Buffer(device: Device, size:Int, usage:Int, reqMask:Int) extends Memory(device){

  protected def initBufferMemory(size:Int, usage:Int, reqMask:Int): Long = MemoryStack.stackPush() | { stack =>
    val dev = device.physicalDevice

    val info = VkBufferCreateInfo.calloc(stack)
      .sType$Default()
      .sharingMode(if (dev.graphicsQueueIndex == dev.presentQueueIndex) VK10.VK_SHARING_MODE_EXCLUSIVE else VK10.VK_SHARING_MODE_CONCURRENT)
      .usage(usage)
      .size(size)

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateBuffer(device.vkDevice, info, null, lp))
    lp.get(0)
  }

  val vkBuffer:Long = initBufferMemory(size, usage, reqMask)

  override val vkMemory: Long = initMemory(reqMask)(new MemoryCallback{
    override def requirements(memReq: VkMemoryRequirements): Unit = {
      VK10.vkGetBufferMemoryRequirements(device.vkDevice, vkBuffer, memReq)
    }
    override def bind(mem: Long): Unit = {
      vkCheck(VK10.vkBindBufferMemory(device.vkDevice, vkBuffer, mem, 0))
    }
  })

  def mapMemory(c: Consumer[MemoryBuffer]): this.type = MemoryStack.stackPush() | { stack =>

    val pb = stack.callocPointer(1)
    vkCheck(VK10.vkMapMemory(device.vkDevice, vkMemory, 0, size, 0, pb))
    val mappedMemory = pb.get(0)

    try {
      c.accept(MemoryBuffer(mappedMemory, size))
    } finally {
      VK10.vkUnmapMemory(device.vkDevice, vkMemory)
    }

    this
  }

  override def close(): Unit = {
    VK10.vkDestroyBuffer(device.vkDevice, vkBuffer, null)
    super.close()
  }
}
