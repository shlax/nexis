package org.ecellon.nexis.vulkan

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.MemoryUtil

class GlfwWindow(val system:VulkanSystem) extends  AutoCloseable{

  GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API)
  GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)

  protected def initWindow():Long = {
    val s = system.windowSize
    GLFW.glfwCreateWindow(s.width, s.height, system.name, MemoryUtil.NULL, MemoryUtil.NULL)
  }

  val glfwWindowHandle:Long = initWindow()
  if (glfwWindowHandle == MemoryUtil.NULL){
    throw IllegalStateException("Failed to create the GLFW window")
  }

  def pullEvents() :Boolean = {
    val res = !GLFW.glfwWindowShouldClose(glfwWindowHandle)
    if(res) GLFW.glfwPollEvents()
    res
  }

  override def close(): Unit = {
    Callbacks.glfwFreeCallbacks(glfwWindowHandle)
    GLFW.glfwDestroyWindow(glfwWindowHandle)
  }

}
