package org.ecellon.nexis.vulkan

import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRSurface
import org.ecellon.nexis.utils.closeable.*

class Surface(val instance:Instance, val window:GlfwWindow) extends AutoCloseable{

  protected def initSurface():Long = MemoryStack.stackPush()|{ stack =>
    val b = stack.callocLong(1)
    vkCheck(GLFWVulkan.glfwCreateWindowSurface(instance.vkInstance, window.glfwWindowHandle, null, b))
    b.get(0)
  }

  val vkSurface:Long = initSurface()

  override def close(): Unit = {
    KHRSurface.vkDestroySurfaceKHR(instance.vkInstance, vkSurface, null)
  }
}
