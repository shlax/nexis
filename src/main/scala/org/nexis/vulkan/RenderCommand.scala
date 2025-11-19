package org.nexis.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkClearValue, VkCommandBuffer, VkRect2D, VkRenderPassBeginInfo}

import java.util.function.{BiConsumer, Consumer}
import org.nexis.utils.closeable.*
import org.nexis.vulkan.frame.NextFrame

class RenderCommand(val renderPass: RenderPass, count:Int = 1) extends AutoCloseable{

  protected def initCommandPool(): CommandPool = {
    val dev = renderPass.swapChain.device
    CommandPool(dev, dev.physicalDevice.graphicsQueueIndex, true)
  }

  val commandPool:CommandPool = initCommandPool()

  protected def initCommandBuffers(count:Int): IndexedSeq[CommandBuffer] = {
    for (i <- 0 until count) yield  CommandBuffer(commandPool)
  }

  val commandBuffers: IndexedSeq[CommandBuffer] = initCommandBuffers(count)

  def record(frame:NextFrame, oneTimeSubmit:Boolean = true, index:Int = 0)(fn: BiConsumer[MemoryStack, VkCommandBuffer]): CommandBuffer  = MemoryStack.stackPush() | { stack =>
    val dim = renderPass.swapChain.dimension

    val info = VkRenderPassBeginInfo.calloc(stack)
      .sType$Default()
      .renderPass(renderPass.vkRenderPass)
      .pClearValues(VkClearValue.calloc(2, stack).apply(0, { v =>
          v.color().float32(0, 0f).float32(1, 0f).float32(2, 0f).float32(3, 0f)
        }).apply(1, { v =>
          v.depthStencil().depth(1f)
        }))
      .clearValueCount(2)
      .renderArea({ a =>
          a.offset().x(0).y(0)
          a.extent().width(dim.width).height(dim.height)
        }:Consumer[VkRect2D])
      .framebuffer(renderPass.frameBuffers(frame.index).vkFrameBuffer)

    val cmdBuff = commandBuffers(index)

    cmdBuff.record(stack, oneTimeSubmit){ (vkCommandBuffer:VkCommandBuffer) =>
      VK10.vkCmdBeginRenderPass(vkCommandBuffer, info, VK10.VK_SUBPASS_CONTENTS_INLINE)
      fn.accept(stack, vkCommandBuffer)
      VK10.vkCmdEndRenderPass(vkCommandBuffer)
    }

  }

  override def close(): Unit = {
    for(c <- commandBuffers) c.close()
    commandPool.close()
  }

}
