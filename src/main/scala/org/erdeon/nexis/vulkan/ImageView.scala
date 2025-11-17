package org.erdeon.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkImageSubresourceRange, VkImageViewCreateInfo}
import org.erdeon.nexis.utils.closeable.*

import java.util.function.Consumer

class ImageView(val device: Device, image:Long, format:Int, aspectMask:Int = VK10.VK_IMAGE_ASPECT_COLOR_BIT) extends AutoCloseable{

  protected def initImageView(image:Long, format:Int, aspectMask:Int) : Long = MemoryStack.stackPush() | { stack =>
    val info = VkImageViewCreateInfo.calloc(stack)
      .sType$Default()
      .image(image)
      .viewType(VK10.VK_IMAGE_VIEW_TYPE_2D)
      .format(format)
      .subresourceRange( (r: VkImageSubresourceRange) => {
        r.aspectMask(aspectMask)
          .baseMipLevel(0)
          .levelCount(1)
          .baseArrayLayer(0)
          .layerCount(1)
      }:Unit )

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateImageView(device.vkDevice, info, null, lp))
    lp.get(0)
  }

  val vkImageView: Long = initImageView(image, format, aspectMask)

  override def close(): Unit = {
    VK10.vkDestroyImageView(device.vkDevice, vkImageView, null)
  }

}
