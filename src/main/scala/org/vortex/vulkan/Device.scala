package org.vortex.vulkan

import org.lwjgl.system.{MemoryStack, MemoryUtil}
import org.lwjgl.vulkan.{KHRSwapchain, VK10, VkDevice, VkDeviceCreateInfo, VkDeviceQueueCreateInfo, VkPhysicalDeviceFeatures}
import org.vortex.utils.closeable.*

class Device(val instance: Instance, surface: Surface, deviceName:String = "") extends AutoCloseable{

  protected def initPhysicalDevice(deviceName:String):PhysicalDevice = {
    PhysicalDevice(instance, surface, deviceName)
  }

  val physicalDevice:PhysicalDevice = initPhysicalDevice(deviceName)

  val queuesFamilies:IndexedSeq[Int] = physicalDevice.queuesFamilies()

  protected def initDevice(): VkDevice = MemoryStack.stackPush() | { stack =>
    val queue = VkDeviceQueueCreateInfo.calloc(queuesFamilies.size, stack)

    val priorities = stack.callocFloat(queuesFamilies.size)
    for(_ <- queuesFamilies.indices) priorities.put(1f)
    priorities.flip()

    for(i <- queuesFamilies.zipWithIndex) {
      queue.get(i._2)
        .sType$Default()
        .queueFamilyIndex(i._1)
        .pQueuePriorities(priorities)
        .pNext(MemoryUtil.NULL)
        .flags(0)
    }

    val deviceFeatures = VkPhysicalDeviceFeatures.calloc(stack)
    VK10.vkGetPhysicalDeviceFeatures(physicalDevice.vkPhysicalDevice, deviceFeatures)
    val enabledFeatures = features(deviceFeatures, stack)

    val extNm = stack.callocPointer(1)
    extNm.put(MemoryUtil.memASCII(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME))
    extNm.flip()

    /* var enabledLayerNames:Option[PointerBuffer] = None
    if(physicalDevice.instance.system.debug){
      val p = stack.callocPointer(1)
      p.put(stack.ASCII("VK_LAYER_KHRONOS_validation"))
      p.flip()
      enabledLayerNames = Some(p)
    } */

    val devInf = VkDeviceCreateInfo.calloc(stack)
      .sType$Default()
      .pQueueCreateInfos(queue)
//      .ppEnabledLayerNames(enabledLayerNames.orNull)
      .pEnabledFeatures(enabledFeatures)
      .ppEnabledExtensionNames(extNm)
      .pNext(MemoryUtil.NULL)
      .flags(0)

    val buff = stack.callocPointer(1)
    vkCheck(VK10.vkCreateDevice(physicalDevice.vkPhysicalDevice, devInf, null, buff))

    VkDevice(buff.get(0), physicalDevice.vkPhysicalDevice, devInf)
  }

  val vkDevice:VkDevice = initDevice()

  protected def features(deviceFeatures:VkPhysicalDeviceFeatures, stack:MemoryStack) : VkPhysicalDeviceFeatures = {
    deviceFeatures
  }

  protected def initGraphicsQueue():Queue = {
    Queue(this, physicalDevice.graphicsQueueIndex, 0)
  }

  val graphicsQueue:Queue = initGraphicsQueue()

  protected def initPresentQueue(): Queue = {
    if(physicalDevice.graphicsQueueIndex == physicalDevice.presentQueueIndex){
      graphicsQueue
    }else {
      Queue(this, physicalDevice.presentQueueIndex, 0)
    }
  }

  val presentQueue:Queue = initPresentQueue()

  def await():Unit = {
    if(presentQueue != graphicsQueue){
      graphicsQueue.await()
    }
    presentQueue.await()

    vkCheck(VK10.vkDeviceWaitIdle(vkDevice))
  }
  
  override def close(): Unit = {
    VK10.vkDestroyDevice(vkDevice, null)
  }

}
