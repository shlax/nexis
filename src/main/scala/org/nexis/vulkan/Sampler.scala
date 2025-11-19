package org.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkSamplerCreateInfo}
import org.nexis.utils.closeable.*

class Sampler(val device: Device, filter:Int = VK10.VK_FILTER_LINEAR, addressMode:Int = VK10.VK_SAMPLER_ADDRESS_MODE_REPEAT) extends AutoCloseable{

  protected def initSampler(filter:Int, addressMode:Int):Long = MemoryStack.stackPush() | { stack =>
    val info = VkSamplerCreateInfo.calloc(stack)
      .sType$Default()
      .magFilter(filter)
      .minFilter(filter)
      .addressModeU(addressMode)
      .addressModeV(addressMode)
      .addressModeW(addressMode)
      .unnormalizedCoordinates(false)

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateSampler(device.vkDevice, info, null, lp))
    lp.get(0)
  }

  val vkSampler:Long = initSampler(filter, addressMode:Int)

  override def close(): Unit = {
    VK10.vkDestroySampler(device.vkDevice, vkSampler, null)
  }
}
