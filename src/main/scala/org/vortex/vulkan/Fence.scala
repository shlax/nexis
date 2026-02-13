package org.vortex.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkFenceCreateInfo}
import scala.concurrent.duration.Duration
import org.vortex.utils.closeable.*

class Fence(val device: Device, signaled:Boolean = true) extends AutoCloseable{

  protected def initFence(signaled:Boolean):Long = MemoryStack.stackPush() | { stack =>
    val info = VkFenceCreateInfo.calloc(stack)
      .sType$Default()
      .flags(if(signaled) VK10.VK_FENCE_CREATE_SIGNALED_BIT else 0)

    val buff = stack.callocLong(1)
    vkCheck(VK10.vkCreateFence(device.vkDevice, info, null, buff))
    buff.get(0)
  }

  val vkFence:Long = initFence(signaled)

  def await(waitAll:Boolean = true, timeout:Duration = device.physicalDevice.instance.system.timeout):this.type = {
    vkCheck( VK10.vkWaitForFences(device.vkDevice, vkFence, waitAll, timeout.toNanos) )
    this
  }

  def reset():Unit = {
    vkCheck( VK10.vkResetFences(device.vkDevice, vkFence) )
  }

  override def close(): Unit = {
    VK10.vkDestroyFence(device.vkDevice, vkFence, null)
  }
}
