package org.ecellon.nexis.vulkan.frame

import org.ecellon.nexis.vulkan.{CommandBuffer, Fence, Semaphore, VulkanSystem}
import org.lwjgl.vulkan.{VK10, VkCommandBuffer}

abstract class RenderLoop(val system: VulkanSystem) extends AutoCloseable, Runnable{

  protected def initImageAvailableSemaphore():Semaphore = Semaphore(system.device)

  val imageAvailableSemaphore:Semaphore = initImageAvailableSemaphore()

  protected def initRenderFinishedSemaphore():Semaphore = Semaphore(system.device)

  val renderFinishedSemaphore: Semaphore = initRenderFinishedSemaphore()

  protected def initInFlightFence():Fence = Fence(system.device)

  val inFlightFence: Fence = initInFlightFence()

  override def run(): Unit = {
    val window = system.window
    val swapChain = system.swapChain
    val graphicsQueue = system.device.graphicsQueue

    while (window.pullEvents()) {

      updateCpu()

      inFlightFence.await().reset()

      updateGpu()

      val next = swapChain.acquireNextImage(imageAvailableSemaphore) // waiting
      for (q <- next.presentResult) presentResult(q)

      val buff = record(next)

      graphicsQueue.submit(buff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT, renderFinishedSemaphore, inFlightFence)
      //graphicsQueue.submit(cmdBuff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT, renderFinishedSemaphore, Some(inFlightFence))
      val res = swapChain.presentImage(next, renderFinishedSemaphore)
      for (q <- res) presentResult(q)

    }

  }

  protected def updateCpu():Unit = {}

  protected def updateGpu():Unit = {}

  protected def record(next:NextFrame):CommandBuffer

  protected def presentResult(presentResult: PresentResult):Unit = {
    println(presentResult)
  }

  override def close(): Unit = {
    inFlightFence.close()
    renderFinishedSemaphore.close()
    imageAvailableSemaphore.close()
  }

}
