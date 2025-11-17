package org.erdeon.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkDescriptorSetAllocateInfo}
import org.erdeon.nexis.utils.closeable.*

class DescriptorSet(val pool:DescriptorPool, layouts:IndexedSeq[DescriptorSetLayout]) extends AutoCloseable{

  protected def initDescriptorSet(layouts:IndexedSeq[DescriptorSetLayout]):Long = MemoryStack.stackPush() | { stack =>
    val setLayouts = stack.callocLong(layouts.size)
    for(i <- layouts.zipWithIndex) setLayouts.put(i._2, i._1.vkDescriptorLayout)

    val info = VkDescriptorSetAllocateInfo.calloc(stack)
      .sType$Default()
      .descriptorPool(pool.vkDescriptorPool)
      .pSetLayouts(setLayouts)

    val lb = stack.callocLong(1)
    vkCheck(VK10.vkAllocateDescriptorSets(pool.device.vkDevice, info, lb))
    lb.get(0)
  }

  val vkDescriptorSet:Long = initDescriptorSet(layouts)

  override def close(): Unit = {
    vkCheck( VK10.vkFreeDescriptorSets(pool.device.vkDevice, pool.vkDescriptorPool, vkDescriptorSet) )
  }
}
