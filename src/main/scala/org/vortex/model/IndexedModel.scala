package org.vortex.model

import org.vortex.vulkan.memory.{ToIntBuffer, TypeLength}
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkPipelineVertexInputStateCreateInfo, VkVertexInputAttributeDescription, VkVertexInputBindingDescription}

import java.nio.IntBuffer

class IndexedModel(verts: Array[Vertex], val indexes:Array[IndexedTriangle]) extends VertexModel(verts), ToIntBuffer {

  override def toIntBuffer(b: IntBuffer): IntBuffer = {
    for (i <- indexes) i.toIntBuffer(b)
    b
  }

  def indexesStride: Int = {
    indexes.head.size
  }

  def indexesSize: Int = {
    indexes.length * indexesStride
  }

  override def indexesCount: Int ={
    indexes.length * 3
  }

  def uvsCount: Int = vertexes.head.uvsCount

  def pipeline(stack: MemoryStack, info: VkPipelineVertexInputStateCreateInfo): Unit = {
    val bindings = VkVertexInputBindingDescription.calloc(1, stack)
    bindings.get(0)
      .binding(0)
      .stride(vertexesStride)
      .inputRate(VK10.VK_VERTEX_INPUT_RATE_VERTEX)
    info.pVertexBindingDescriptions(bindings)

    val attributes = VkVertexInputAttributeDescription.calloc(2 + uvsCount, stack)

    attributes.get(0) // vertex : layout(location = 0) in vec2 inPosition
      .binding(0)
      .location(0)
      .format(VK10.VK_FORMAT_R32G32B32_SFLOAT)
      .offset(0)

    attributes.get(1) // normal : layout(location = 1) in vec3 inNormal;
      .binding(0)
      .location(1)
      .format(VK10.VK_FORMAT_R32G32B32_SFLOAT)
      .offset(TypeLength.floatLength(3))

    for(i <- 0 until uvsCount) {
      attributes.get(2 + i) // uv : layout(location = 2) in vec2 inUv;
        .binding(0)
        .location(2 + i)
        .format(VK10.VK_FORMAT_R32G32_SFLOAT)
        .offset(TypeLength.floatLength(3 + 3 + (i * 2)))
      info.pVertexAttributeDescriptions(attributes)
    }
  }

}
