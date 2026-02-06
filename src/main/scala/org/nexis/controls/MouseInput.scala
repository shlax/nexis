package org.nexis.controls

import org.lwjgl.glfw.{GLFW, GLFWMouseButtonCallbackI}
import org.lwjgl.system.MemoryUtil
import org.nexis.math.Vector3f
import org.nexis.vulkan.GlfwWindow
import org.nexis.vulkan.interoperability.{MouseButtonCallback, MousePositionCallback, MouseScrollCallback}

class MouseInput(val window: GlfwWindow) extends AutoCloseable{
  val cursor: Long = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR)
  GLFW.glfwSetCursor(window.glfwWindowHandle, cursor)

  private var xOff:Double = 0
  private var yOff:Double = 0

  /** scroll */
  private var zOff:Double = 0

  private var xWin:Double = 0
  private var yWin:Double = 0

  def pull():Option[Vector3f] = {
    if(xOff == 0d && yOff == 0d && zOff == 0d){
      None
    }else{
      val offset = Vector3f(xOff.toFloat, yOff.toFloat, zOff.toFloat)
      xOff = 0d; yOff = 0d; zOff = 0d
      Some(offset)
    }
  }

  protected def closeCallback(c:AutoCloseable):Unit = {
    if(c != null) c.close()
  }

  private var rotate = false
  private var setXY = false

  closeCallback(GLFW.glfwSetCursorPosCallback(window.glfwWindowHandle, new MousePositionCallback( (win:Long, xPos: Double, yPos: Double) => {
    if (rotate) {
      if (setXY) {
        xOff += xPos - xWin
        yOff += yPos - yWin
      }else{
        setXY = true
      }
      xWin = xPos
      yWin = yPos
    }
  })))

  closeCallback(GLFW.glfwSetMouseButtonCallback(window.glfwWindowHandle, new MouseButtonCallback( (win: Long, button: Int, action: Int, mods: Int) => {
    if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
      if (action == GLFW.GLFW_PRESS) {
        GLFW.glfwSetInputMode(win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
        rotate = true
      }else if (action == GLFW.GLFW_RELEASE){
        rotate = false; setXY = false
        GLFW.glfwSetInputMode(win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
      }
    }
  })))

  closeCallback(GLFW.glfwSetScrollCallback(window.glfwWindowHandle, new MouseScrollCallback( (win: Long, xOffset:Double, yOffset: Double) => {
    zOff += yOffset
  })))

  override def close(): Unit = {
    closeCallback(GLFW.glfwSetCursorPosCallback(window.glfwWindowHandle, null))
    closeCallback(GLFW.glfwSetMouseButtonCallback(window.glfwWindowHandle, null))
    closeCallback(GLFW.glfwSetScrollCallback(window.glfwWindowHandle, null))

    GLFW.glfwSetCursor(window.glfwWindowHandle, MemoryUtil.NULL)
    GLFW.glfwDestroyCursor(cursor)
  }

}
