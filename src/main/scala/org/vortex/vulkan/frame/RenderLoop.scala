package org.vortex.vulkan.frame

import org.vortex.vulkan.{CommandBuffer, Fence, Semaphore, VulkanSystem, vkCheck}
import org.lwjgl.vulkan.{VK10, VkCommandBuffer}

abstract class RenderLoop(val system: VulkanSystem) extends AutoCloseable, Runnable{

  protected def initImageAvailableSemaphore(): Semaphore = Semaphore(system.device)

  protected val imageAvailableSemaphore: Semaphore = initImageAvailableSemaphore()

  protected def initRenderFinishedSemaphore(count:Int):Array[Semaphore] = {
    val s = for (_ <- 0 until count) yield Semaphore(system.device)
    s.toArray
  }

  protected val renderFinishedSemaphore: Array[Semaphore] = initRenderFinishedSemaphore(system.swapChain.imagesCount)

  protected def initInFlightFence():Fence = Fence(system.device)

  protected val inFlightFence: Fence = initInFlightFence()

  override def run(): Unit = {
    val window = system.window
    val swapChain = system.swapChain
    val graphicsQueue = system.device.graphicsQueue

    while (window.pullEvents()) {

      compute()

      inFlightFence.await().reset()

      synchronize()

      val next = swapChain.acquireNextImage(imageAvailableSemaphore) // waiting
      for (q <- next.presentResult) presentResult(q)

      val buff = record(next)
      val ind = next.index

      graphicsQueue.submit(buff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT, renderFinishedSemaphore(ind), inFlightFence)
      //graphicsQueue.submit(cmdBuff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT, renderFinishedSemaphore, Some(inFlightFence))
      val res = swapChain.presentImage(next, renderFinishedSemaphore(ind))
      for (q <- res) presentResult(q)

    }

    inFlightFence.await()
    system.device.await()
  }

  protected def compute():Unit = {}

  protected def synchronize():Unit = {}

  protected def record(next:NextFrame):CommandBuffer

  protected def presentResult(presentResult: PresentResult):Unit = {
    println(presentResult)
  }

  override def close(): Unit = {
    inFlightFence.close()
    imageAvailableSemaphore.close()
    for(i <- renderFinishedSemaphore) i.close()
  }

}
