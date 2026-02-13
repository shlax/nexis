package org.vortex.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{KHRSurface, KHRSwapchain, VK10, VkExtensionProperties, VkPhysicalDevice, VkPhysicalDeviceMemoryProperties, VkPhysicalDeviceProperties, VkQueueFamilyProperties}
import org.vortex.utils.closeable.*

class PhysicalDevice(val instance: Instance, val surface: Surface, deviceName:String = ""){

  /** vkPhysicalDevice:VkPhysicalDevice,
   graphicsQueueIndex:Int, graphicsQueueIndexes:IndexedSeq[Int],
   presentQueueIndex:Int, presentQueueIndexes:IndexedSeq[Int] */
  protected def initPhysicalDeviceQueues(deviceName:String):(VkPhysicalDevice, Int, IndexedSeq[Int], Int, IndexedSeq[Int]) = MemoryStack.stackPush()|{ stackTop =>
    val vkInstance = instance.vkInstance

    val nBuff = stackTop.callocInt(1)
    vkCheck(VK10.vkEnumeratePhysicalDevices(vkInstance, nBuff, null))
    val n = nBuff.get(0)
    if(n <= 0){
      throw IllegalStateException("vkEnumeratePhysicalDevices reported zero accessible devices.")
    }

    val devices = stackTop.callocPointer(n)
    vkCheck(VK10.vkEnumeratePhysicalDevices(vkInstance, nBuff, devices))

    var vkGpu:Option[(VkPhysicalDevice, Int, IndexedSeq[Int], Int, IndexedSeq[Int])] = None

    for(i <- 0 until n) MemoryStack.stackPush()|{ stack =>
      val gpu = VkPhysicalDevice(devices.get(i), vkInstance)
      val intBuff = stack.callocInt(1)

      val nm = if(deviceName.isEmpty){ ""
      }else{
        MemoryStack.stackPush()| { stackNm =>
          val devProp = VkPhysicalDeviceProperties.calloc(stackNm)
          VK10.vkGetPhysicalDeviceProperties(gpu, devProp)
          devProp.deviceNameString()
        }
      }

      VK10.vkGetPhysicalDeviceQueueFamilyProperties(gpu, intBuff, null)
      val props = VkQueueFamilyProperties.calloc(intBuff.get(0), stack)
      VK10.vkGetPhysicalDeviceQueueFamilyProperties(gpu, intBuff, props)

      var graphicsQueueInd:List[Int] = Nil
      var presentQueueInd:List[Int] = Nil

      for(i <- 0 until props.capacity()){
        val p = props.get(i)
        if( (p.queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0 ){
          graphicsQueueInd = i :: graphicsQueueInd
        }

        val supportsPresent = stack.callocInt(1)
        vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(gpu, i, surface.vkSurface, supportsPresent))
        if (supportsPresent.get(0) == VK10.VK_TRUE) {
          presentQueueInd = i :: presentQueueInd
        }
      }

      var hasKHRSwapChainExtension = false
      if(graphicsQueueInd.nonEmpty && presentQueueInd.nonEmpty){
        vkCheck(VK10.vkEnumerateDeviceExtensionProperties(gpu, null.asInstanceOf[CharSequence], intBuff, null))
        val ext = VkExtensionProperties.calloc(intBuff.get(0), stack)
        vkCheck(VK10.vkEnumerateDeviceExtensionProperties(gpu, null.asInstanceOf[CharSequence], intBuff, ext))

        for(i <- 0 until ext.capacity() if !hasKHRSwapChainExtension){
          val e = ext.get(i)
          if(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME == e.extensionNameString()){
            hasKHRSwapChainExtension = true
          }
        }
      }

      if(hasKHRSwapChainExtension){
        val int = graphicsQueueInd.intersect(presentQueueInd)
        val gInd = if(int.isEmpty) graphicsQueueInd.head else int.head
        val pInd = if(int.isEmpty) presentQueueInd.head else int.head

        if(deviceName.isEmpty){
          if (vkGpu.isEmpty || (int.nonEmpty && vkGpu.get._2 != vkGpu.get._4)) {
            vkGpu = Some((gpu, gInd, graphicsQueueInd.toIndexedSeq, pInd, presentQueueInd.toIndexedSeq))
          }
        }else{
          if(deviceName == nm){
            vkGpu = Some((gpu, gInd, graphicsQueueInd.toIndexedSeq, pInd, presentQueueInd.toIndexedSeq))
          }
        }

      }

    }

    vkGpu.get
  }

  val (vkPhysicalDevice:VkPhysicalDevice,
    graphicsQueueIndex:Int, graphicsQueueIndexes:IndexedSeq[Int],
    presentQueueIndex:Int, presentQueueIndexes:IndexedSeq[Int]) = initPhysicalDeviceQueues(deviceName)

  case class MemoryType(propertyFlags:Int)

  protected def initMemoryTypes(): IndexedSeq[MemoryType] = MemoryStack.stackPush() |{ stack =>
    val memProps = VkPhysicalDeviceMemoryProperties.calloc(stack)
    VK10.vkGetPhysicalDeviceMemoryProperties(vkPhysicalDevice, memProps)

    val memTypes = memProps.memoryTypes()
    for(i <- 0 until memProps.memoryTypeCount()) yield MemoryType(memTypes.get(i).propertyFlags())
  }

  val memoryTypes:IndexedSeq[MemoryType] = initMemoryTypes()

  def queuesFamilies() :IndexedSeq[Int] = {
    Set(graphicsQueueIndex, presentQueueIndex).toIndexedSeq
  }

}
