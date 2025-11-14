package org.ecellon.nexis.vulkan

import org.ecellon.nexis.utils.Dimension
import org.lwjgl.glfw.{GLFW, GLFWErrorCallback, GLFWVulkan}

import scala.concurrent.duration.Duration
import scala.concurrent.duration.*

class VulkanSystem(val name:String, val windowSize:Dimension, deviceName:String = "",
                   val debug :LogLevel = LogLevel.warning, val timeout:Duration = 1.second) extends AutoCloseable{
  // GLFW.glfwInitHint(GLFW.GLFW_PLATFORM, GLFW.GLFW_PLATFORM_X11)

  GLFWErrorCallback.createPrint().set()

  if (!GLFW.glfwInit()){
    throw IllegalStateException("Unable to initialize GLFW")
  }

  if (!GLFWVulkan.glfwVulkanSupported()){
    throw IllegalStateException("Cannot find a compatible Vulkan installable client driver (ICD)")
  }

  protected def initInstance() : Instance = {
    Instance(this)
  }

  val instance:Instance = initInstance()

  protected def initWindow(): GlfwWindow = {
    GlfwWindow(this)
  }

  val window: GlfwWindow = initWindow()

  protected def initSurface(): Surface = {
    Surface(instance, window)
  }

  val surface:Surface = initSurface()

  protected def initDevice(deviceName:String):Device = {
    Device(instance, surface, deviceName)
  }

  val device:Device = initDevice(deviceName)

  protected def initSwapChain():SwapChain = {
    SwapChain(surface, device)
  }

  val swapChain:SwapChain = initSwapChain()

  protected def initRenderPass():RenderPass = {
    RenderPass(swapChain)
  }

  val renderPass:RenderPass = initRenderPass()

  override def close(): Unit = {
    device.await()

    renderPass.close()
    swapChain.close()
    device.close()
    surface.close()
    window.close()
    instance.close()

    GLFW.glfwTerminate()
    GLFW.glfwSetErrorCallback(null).free()
  }
}
