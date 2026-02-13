package org.vortex.vulkan

import org.vortex.vulkan.frame.{NextFrame, PresentResult}
import org.lwjgl.system.{MemoryStack, MemoryUtil}
import org.lwjgl.vulkan.{KHRSurface, KHRSwapchain, VK10, VkExtent2D, VkExtent3D, VkFormatProperties, VkImageCreateInfo, VkMemoryRequirements, VkPresentInfoKHR, VkSurfaceCapabilitiesKHR, VkSurfaceFormatKHR, VkSwapchainCreateInfoKHR}
import org.vortex.utils.closeable.*
import org.vortex.utils.Dimension

import java.util.function.Consumer
import scala.concurrent.duration.Duration

class SwapChain(val surface: Surface, device: Device, imageCount:Int = 0) extends Memory(device){

  /** (vkSwapChain : Long,
   vkImages: IndexedSeq[Long], format:Int,
   dimension: Dimension) */
  protected def initSwapChain(imageCount:Int): (Long, IndexedSeq[Long], Int, Dimension) = MemoryStack.stackPush() | { stack =>
    val vkPhysicalDevice = device.physicalDevice.vkPhysicalDevice

    val surfCapabilities = VkSurfaceCapabilitiesKHR.calloc(stack)
    vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(vkPhysicalDevice, surface.vkSurface, surfCapabilities))

    val maxImages = surfCapabilities.maxImageCount()
    val minImages = surfCapabilities.minImageCount()
    val imgCnt = if(imageCount == 0) {
      if( maxImages == 0 ){
        minImages + 1
      }else{
        // sticking to this minimum means that we may sometimes have to wait on the driver to complete internal operations before we can acquire another image to render to.
        (minImages + 1).min(maxImages)
      }
    }else{
      if( maxImages == 0 ){
        imageCount.max(minImages)
      }else{
        imageCount.min(maxImages).max(minImages)
      }
    }

    val formatsBuff = stack.callocInt(1)
    vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(vkPhysicalDevice, surface.vkSurface, formatsBuff, null))
    val numFormats = formatsBuff.get(0)
    if(numFormats <= 0){
      throw RuntimeException("vkGetPhysicalDeviceSurfaceFormatsKHR pSurfaceFormatCount = "+numFormats)
    }

    val surfaceFormats = VkSurfaceFormatKHR.calloc(numFormats, stack)
    vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(vkPhysicalDevice, surface.vkSurface, formatsBuff, surfaceFormats))

    var imageFormat = surfaceFormats.get(0).format()
    var colorSpace = surfaceFormats.get(0).colorSpace()
    for(i <- 0 until numFormats){
      val sf = surfaceFormats.get(i)
      if(sf.format() == VK10.VK_FORMAT_B8G8R8A8_SRGB && sf.colorSpace() == KHRSurface.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR){
        imageFormat = sf.format()
        colorSpace = sf.colorSpace()
      }
    }

    val presentModesBuff = stack.callocInt(1)
    vkCheck(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(vkPhysicalDevice, surface.vkSurface, presentModesBuff, null))
    val mumPresentModes = presentModesBuff.get(0)

    val presentModes = stack.callocInt(mumPresentModes)
    vkCheck(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(vkPhysicalDevice, surface.vkSurface, presentModesBuff, presentModes))

    val modes = for(i <- 0 until mumPresentModes) yield presentModes.get(i)
    val presentMode = if(modes.contains(KHRSurface.VK_PRESENT_MODE_FIFO_RELAXED_KHR)) KHRSurface.VK_PRESENT_MODE_FIFO_RELAXED_KHR else KHRSurface.VK_PRESENT_MODE_FIFO_KHR

    val s = surface.window.system.windowSize
    var width = s.width
    var height = s.height

    val swapChainExtent = VkExtent2D.calloc(stack)
    if(surfCapabilities.currentExtent().width() == 0xFFFFFFFF && surfCapabilities.currentExtent().height() == 0xFFFFFFFF){
      // Surface size undefined. Set to the window size if within bounds

      val minExt = surfCapabilities.minImageExtent()
      val maxExt = surfCapabilities.maxImageExtent()

      def minMax(v: Int, min: Int, max: Int) = v.min(max).max(min)

      width = minMax(s.width, minExt.width(), maxExt.width())
      height = minMax(s.height, minExt.height(), maxExt.height())

      swapChainExtent.width(width)
      swapChainExtent.height(height)
    }else{
      swapChainExtent.set(surfCapabilities.currentExtent())
    }

    val swapChainInfo = VkSwapchainCreateInfoKHR.calloc(stack)
      .sType$Default()
      .surface(surface.vkSurface)
      .minImageCount(imgCnt)
      .imageFormat(imageFormat)
      .imageColorSpace(colorSpace)
      .presentMode(presentMode)
      .imageExtent(swapChainExtent)
      .imageArrayLayers(1)
      .imageUsage(VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
      .compositeAlpha(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR)
      .preTransform(surfCapabilities.currentTransform())
      .clipped(true)
      .oldSwapchain(MemoryUtil.NULL)

    val indices = device.queuesFamilies.toSet
    if(indices.size > 1){
      val intBuffer = stack.callocInt(indices.size)
      for(i <- indices) intBuffer.put(i)
      intBuffer.flip()

      swapChainInfo.imageSharingMode(VK10.VK_SHARING_MODE_CONCURRENT)
        .queueFamilyIndexCount(indices.size)
        .pQueueFamilyIndices(intBuffer)
    }else{
      swapChainInfo.imageSharingMode(VK10.VK_SHARING_MODE_EXCLUSIVE)
        .queueFamilyIndexCount(0)
        .pQueueFamilyIndices(null)
    }

    val lp = stack.callocLong(1)
    vkCheck(KHRSwapchain.vkCreateSwapchainKHR(device.vkDevice, swapChainInfo, null, lp))
    val swapChain = lp.get(0)

    val ivBuff = stack.callocInt(1)
    vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.vkDevice, swapChain, ivBuff, null))
    val numImages = ivBuff.get(0)

    val swapChainImages = stack.callocLong(numImages)
    vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.vkDevice, swapChain, ivBuff, swapChainImages))
    val images = for(i <- 0 until numImages ) yield swapChainImages.get(i)

    (swapChain, images, imageFormat, Dimension(width, height) )
  }

  val (vkSwapChain : Long,
    vkImages: IndexedSeq[Long], format:Int,
    dimension: Dimension) = initSwapChain(imageCount)

  def imagesCount: Int = vkImages.length

  protected def initImageViews(): IndexedSeq[ImageView] = {
    for(i <- vkImages.zipWithIndex) yield ImageView(device, i._1, format)
  }

  val imageViews: IndexedSeq[ImageView] = initImageViews()

  protected def initDepthImage():(Long, Int) = MemoryStack.stackPush() | { stack =>
    val depthFormat = VK10.VK_FORMAT_D32_SFLOAT

    val props = VkFormatProperties.calloc(stack)
    VK10.vkGetPhysicalDeviceFormatProperties(device.physicalDevice.vkPhysicalDevice, depthFormat, props)
    if((props.optimalTilingFeatures() & VK10.VK_FORMAT_FEATURE_DEPTH_STENCIL_ATTACHMENT_BIT) != VK10.VK_FORMAT_FEATURE_DEPTH_STENCIL_ATTACHMENT_BIT){
      throw RuntimeException("vkPhysicalDevice don't support VK_FORMAT_D32_SFLOAT")
    }

    val info = VkImageCreateInfo.calloc(stack)
      .sType$Default()
      .imageType(VK10.VK_IMAGE_TYPE_2D)
      .usage(VK10.VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT)
      .format(depthFormat)
      .tiling(VK10.VK_IMAGE_TILING_OPTIMAL)
      .sharingMode(VK10.VK_SHARING_MODE_EXCLUSIVE)
      .samples(VK10.VK_SAMPLE_COUNT_1_BIT)
      .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
      .mipLevels(1)
      .arrayLayers(1)
      .extent({ e =>
          e.width(dimension.width).height(dimension.height).depth(1)
        }: Consumer[VkExtent3D])

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateImage(device.vkDevice, info, null, lp))
    (lp.get(0), depthFormat)
  }

  val (vkDepthImage:Long, depthImageFormat:Int) = initDepthImage()

  override val vkMemory:Long = initMemory(VK10.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT)(new MemoryCallback{
    override def requirements(memReq: VkMemoryRequirements): Unit = {
      VK10.vkGetImageMemoryRequirements(device.vkDevice, vkDepthImage, memReq)
    }
    override def bind(mem: Long): Unit = {
      vkCheck(VK10.vkBindImageMemory(device.vkDevice, vkDepthImage, mem, 0))
    }
  })

  protected def initDepthImageView():ImageView = {
    ImageView(device, vkDepthImage, depthImageFormat, VK10.VK_IMAGE_ASPECT_DEPTH_BIT)
  }

  val vkDepthImageView:ImageView = initDepthImageView()

  def presentResult(err:Int) : Option[PresentResult] = {
    var res:Option[PresentResult] = None

    if (err == KHRSwapchain.VK_ERROR_OUT_OF_DATE_KHR) {
      res = Some(PresentResult.outOfDate)
    } else if (err == KHRSwapchain.VK_SUBOPTIMAL_KHR) {
      res = Some(PresentResult.suboptimal)
    } else if (err != VK10.VK_SUCCESS) {
      throw IllegalStateException(String.format("Vulkan error [0x%X]", err));
    }

    res
  }

  def acquireNextImage(signalSemaphore: Semaphore, timeout:Duration = device.physicalDevice.instance.system.timeout):NextFrame = MemoryStack.stackPush() | { stack =>
    val sem = signalSemaphore.vkSemaphore

    val ip = stack.callocInt(1)
    val err = KHRSwapchain.vkAcquireNextImageKHR(device.vkDevice, vkSwapChain, timeout.toNanos, sem, MemoryUtil.NULL, ip)

    val res = presentResult(err)
    val ind = ip.get(0)

    NextFrame(ind, res)
  }

  def presentImage(index: NextFrame, waitSemaphore: Semaphore, queue: Queue = device.presentQueue): Option[PresentResult] = MemoryStack.stackPush() | { stack =>
    if (index.index >= vkImages.length) {
      throw IndexOutOfBoundsException(index.index)
    }

    val info = VkPresentInfoKHR.calloc(stack)
      .sType$Default()
      .pWaitSemaphores(stack.longs(waitSemaphore.vkSemaphore))
      .swapchainCount(1)
      .pSwapchains(stack.longs(vkSwapChain))
      .pImageIndices(stack.ints(index.index))

    val err = KHRSwapchain.vkQueuePresentKHR(queue.vkQueue, info)
    presentResult(err)
  }

  override def close(): Unit = {
    vkDepthImageView.close()
    VK10.vkDestroyImage(device.vkDevice, vkDepthImage, null)

    super.close()

    KHRSwapchain.vkDestroySwapchainKHR(device.vkDevice, vkSwapChain, null)
    for(i <- imageViews) i.close()
  }
}
