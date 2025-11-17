package org.erdeon.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkDescriptorPoolCreateInfo, VkDescriptorPoolSize}
import org.erdeon.nexis.utils.closeable.*

/** poolTypes : Map(VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER -> 1) */
class DescriptorPool(val device: Device, poolSizes:Map[Int,Int], maxSets:Int = 1) extends AutoCloseable{

  protected def initDescriptorPool(poolSizes:Map[Int,Int], maxSets:Int):Long = MemoryStack.stackPush() | { stack =>
    val pools = VkDescriptorPoolSize.calloc(poolSizes.size, stack)
    for(i <- poolSizes.zipWithIndex) {
      val e = i._1
      pools.get(i._2)
        .`type`(e._1)
        .descriptorCount(e._2)
    }

    val info = VkDescriptorPoolCreateInfo.calloc(stack)
      .sType$Default()
      .flags(VK10.VK_DESCRIPTOR_POOL_CREATE_FREE_DESCRIPTOR_SET_BIT)
      .pPoolSizes(pools)
      .maxSets(maxSets)

    val lb = stack.callocLong(1)
    vkCheck(VK10.vkCreateDescriptorPool(device.vkDevice, info, null, lb))
    lb.get(0)
  }

  val vkDescriptorPool:Long = initDescriptorPool(poolSizes, maxSets)

  override def close(): Unit = {
    VK10.vkDestroyDescriptorPool(device.vkDevice, vkDescriptorPool, null)
  }

}
