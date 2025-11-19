package org.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkDescriptorSetLayoutBinding, VkDescriptorSetLayoutCreateInfo}
import org.nexis.utils.closeable.*

class DescriptorSetLayout(val device: Device, binding:Int, descriptorType:Int, stage:Int, descriptorCount:Int = 1) extends AutoCloseable{

  protected def initDescriptorLayout(binding:Int, descriptorType:Int, stage:Int, descriptorCount:Int):Long = MemoryStack.stackPush() | { stack =>
    val bindings = VkDescriptorSetLayoutBinding.calloc(1, stack)

    bindings.get(0)
      .binding(binding)
      .descriptorType(descriptorType)
      .descriptorCount(descriptorCount)
      .stageFlags(stage)

    val info = VkDescriptorSetLayoutCreateInfo.calloc(stack)
      .sType$Default()
      .pBindings(bindings)

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateDescriptorSetLayout(device.vkDevice, info, null, lp))
    lp.get(0)
  }

  val vkDescriptorLayout:Long = initDescriptorLayout(binding, descriptorType, stage, descriptorCount)

  override def close(): Unit = {
    VK10.vkDestroyDescriptorSetLayout(device.vkDevice, vkDescriptorLayout, null)
  }
}
