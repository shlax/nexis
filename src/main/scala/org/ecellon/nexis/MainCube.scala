package org.ecellon.nexis

import de.matthiasmann.twl.utils.PNGDecoder
import org.ecellon.nexis.controls.{MouseInput, OrbitCamera}
import org.ecellon.nexis.math.{Axis, Matrix4f, Vector2f, Vector3f}
import org.ecellon.nexis.model.ModelLoader
import org.ecellon.nexis.vulkan.shader.ShaderCompiler
import org.lwjgl.system.{Configuration, MemoryStack, MemoryUtil}
import org.lwjgl.util.shaderc.Shaderc
import org.lwjgl.vulkan.{VK10, VkCommandBuffer, VkPipelineLayoutCreateInfo, VkPipelineVertexInputStateCreateInfo, VkPushConstantRange, VkVertexInputAttributeDescription, VkVertexInputBindingDescription}
import org.ecellon.nexis.utils.closeable.*
import vulkan.memory.{MemoryBuffer, TypeLength}
import vulkan.{Buffer, CommandBuffer, DescriptorPool, DescriptorSet, DescriptorSetLayout, Fence, Pipeline, PipelineLayout, RenderCommand, Sampler, Semaphore, Texture, VulkanSystem}
import org.ecellon.nexis.math.perspective.*
import org.ecellon.nexis.utils.{Dimension, FpsCounter}

// --sun-misc-unsafe-memory-access=allow --enable-native-access=ALL-UNNAMED -XX:+UnlockExperimentalVMOptions -XX:+TrustFinalNonStaticFields
object MainCube extends Runnable{

  def main(args:Array[String]) : Unit = {
    //Configuration.STACK_SIZE.set(128)
    try {
      run()
    }catch {
      case e: Throwable =>
        e.printStackTrace()
    }
  }

  override def run(): Unit = {
    // val fps = new FpsCounter()

    val shaders = ShaderCompiler(true) | { compile =>
      IndexedSeq(
        compile("/shaders/shader.vert", Shaderc.shaderc_glsl_vertex_shader, VK10.VK_SHADER_STAGE_VERTEX_BIT),
        compile("/shaders/shader.frag", Shaderc.shaderc_glsl_fragment_shader, VK10.VK_SHADER_STAGE_FRAGMENT_BIT)
      )
    }

    val cube = (getClass.getResourceAsStream("/models/cube.msh")| { in => // sphere
      ModelLoader().loadModel(in)
    }).invert(Axis.Y).compile().vulkanModel

    VulkanSystem("NXN", Dimension(1280, 720)) | { sys => // , "NVIDIA GeForce RTX 2050"
      val graphicsQueue = sys.device.graphicsQueue

      using { use =>
        val imageAvailableSemaphore = use(Semaphore(sys.device))
        val renderFinishedSemaphore = use(Semaphore(sys.device))
        val inFlightFence = use(Fence(sys.device))

        // vec2(0.0, -0.5), vec2(-0.5, 0.5), vec2(0.5, 0.5)
        val points = use(Buffer(sys.device, cube.vertexesSize, VK10.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT,
            VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)).mapMemory{ memory =>
          val b = MemoryUtil.memFloatBuffer(memory.address, memory.size)
          cube.toFloatBuffer(b)
        }

        val indexes = use(Buffer(sys.device, cube.indexesSize, VK10.VK_BUFFER_USAGE_INDEX_BUFFER_BIT,
          VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)).mapMemory{ memory =>
          val b = MemoryUtil.memIntBuffer(memory.address, memory.size)
          cube.toIntBuffer(b)
        }

        val sampler = use(Sampler(sys.device))

        val descriptorPool = use(DescriptorPool(sys.device, Map(VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER -> 1)))
        val layout = use(DescriptorSetLayout(sys.device, 0, VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, VK10.VK_SHADER_STAGE_FRAGMENT_BIT))

        val descriptorSet = use(DescriptorSet(descriptorPool, IndexedSeq(layout)))

        // layout(push_constant) uniform Transformations { mat4 viewMatrix; } transformations;
        val pipelineLayout = use(new PipelineLayout(sys.device){
          override protected def pipelineLayout(stack: MemoryStack, info: VkPipelineLayoutCreateInfo): Unit = {
            val ranges = VkPushConstantRange.calloc(1, stack)
              .stageFlags(VK10.VK_SHADER_STAGE_VERTEX_BIT)
              .offset(0)
              .size(TypeLength.floatLength(2 * 4 * 4))
            info.pPushConstantRanges(ranges)

            info.pSetLayouts(stack.longs(layout.vkDescriptorLayout))
          }
        })

        val triangle = use(new Pipeline(pipelineLayout, sys.renderPass, shaders){

          // layout(location = 0) in vec2 inPosition
          override protected def vertexInput(stack: MemoryStack, info:VkPipelineVertexInputStateCreateInfo):Unit = {
            val bindings = VkVertexInputBindingDescription.calloc(1, stack)
            bindings.get(0)
              .binding(0)
              .stride(cube.vertexesStride)
              .inputRate(VK10.VK_VERTEX_INPUT_RATE_VERTEX)
            info.pVertexBindingDescriptions(bindings)

            val attributes = VkVertexInputAttributeDescription.calloc(3, stack)
            attributes.get(0) // vertex
              .binding(0)
              .location(0)
              .format(VK10.VK_FORMAT_R32G32B32_SFLOAT)
              .offset(0)
            attributes.get(1) // normal
              .binding(0)
              .location(1)
              .format(VK10.VK_FORMAT_R32G32B32_SFLOAT)
              .offset(TypeLength.floatLength(3))
            attributes.get(2) // uv
              .binding(0)
              .location(2)
              .format(VK10.VK_FORMAT_R32G32_SFLOAT)
              .offset(TypeLength.floatLength(3+3))
            info.pVertexAttributeDescriptions(attributes)
          }
        })

        RenderCommand(sys.renderPass) | { render =>

          val texture = use(Texture(sys.device, Dimension(512, 512))).updateDescriptorSet(descriptorSet, 0, sampler)

          Fence(sys.device, false) | { fence =>
            Buffer(sys.device, 512 * 512 * 4, VK10.VK_BUFFER_USAGE_TRANSFER_SRC_BIT,
              VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT) |{ stageBuffer =>

              stageBuffer.mapMemory{ memory =>
                val b = MemoryUtil.memByteBuffer(memory.address, memory.size)

                MainCube.getClass.getResourceAsStream("/textures/checker.png") | { is =>
                  val dec = PNGDecoder(is)
                  dec.decode(b, 512 * 4, PNGDecoder.Format.RGBA)
                }
              }

              CommandBuffer(render.commandPool) | { commandBuffer =>
                graphicsQueue.submit(texture.copyBufferToImage(stageBuffer, commandBuffer), fence).await()
              }

            }
          }

          val cameraPoint = Vector3f()
          val camera = use(OrbitCamera(sys.window, perspective(60, sys.windowSize, 1, 1000))).set(cameraPoint, 3, 0, 0)

          // >>
          while (sys.window.pullEvents()) {

            camera.update(cameraPoint)

            // cpu calc
            // fps(f => println("fps: "+f))

            inFlightFence.await().reset()

            // update gpu

            val next = sys.swapChain.acquireNextImage(imageAvailableSemaphore) // waiting
            for(q <- next.presentResult) println(q)

            val cmdBuff = render.record(next){ (stack, buff) =>
              triangle.bindPipeline(buff)

              val viewBuff = stack.callocFloat(2 * 4 * 4)
              camera.viewMatrix.toFloatBuffer(viewBuff)
              camera.rotationMatrix.toFloatBuffer(viewBuff)
              viewBuff.flip()

              VK10.vkCmdPushConstants(buff, pipelineLayout.vkPipelineLayout, VK10.VK_SHADER_STAGE_VERTEX_BIT, 0, viewBuff)

              VK10.vkCmdBindDescriptorSets(buff, VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, pipelineLayout.vkPipelineLayout, 0, stack.longs(descriptorSet.vkDescriptorSet), null)

              VK10.vkCmdBindVertexBuffers(buff, 0, stack.longs(points.vkBuffer), stack.longs(0L))
              VK10.vkCmdBindIndexBuffer(buff, indexes.vkBuffer, 0, VK10.VK_INDEX_TYPE_UINT32) //VK10.vkCmdDraw(buff, 3, 1, 0, 0)
              VK10.vkCmdDrawIndexed(buff, cube.indexesCount, 1, 0, 0, 0)
            }

            graphicsQueue.submit(cmdBuff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT, renderFinishedSemaphore, inFlightFence)
            //graphicsQueue.submit(cmdBuff, imageAvailableSemaphore, VK10.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT, renderFinishedSemaphore, Some(inFlightFence))
            val res = sys.swapChain.presentImage(next, renderFinishedSemaphore)
            for(q <- res) println(q)

          }

          // <<

        }

      }
    }
  }

}
