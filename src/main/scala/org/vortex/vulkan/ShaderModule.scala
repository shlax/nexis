package org.vortex.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkShaderModuleCreateInfo}
import org.vortex.utils.closeable.*
import org.vortex.vulkan.shader.CompiledShader

class ShaderModule(val device: Device, compiledShader: CompiledShader) extends AutoCloseable{
  val stage: Int = compiledShader.stage
  val name: String = compiledShader.name

  protected def initShaderModule(code:Array[Byte]):Long = MemoryStack.stackPush()|{ stack =>
    val pCode = stack.calloc(code.length).put(0, code)

    val info = VkShaderModuleCreateInfo.calloc(stack)
      .sType$Default()
      .pCode(pCode)

    val lp = stack.callocLong(1)
    vkCheck(VK10.vkCreateShaderModule(device.vkDevice, info, null, lp))
    lp.get()
  }

  val shaderModule:Long = initShaderModule(compiledShader.code)

  override def close(): Unit = {
    VK10.vkDestroyShaderModule(device.vkDevice, shaderModule, null)
  }
}
