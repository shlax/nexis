package org.erdeon.nexis

import de.matthiasmann.twl.utils.PNGDecoder
import org.erdeon.nexis.controls.OrbitCamera
import org.erdeon.nexis.math.{Axis, Matrix4f, Vector3f}
import org.erdeon.nexis.model.ModelLoader
import org.erdeon.nexis.model.skeleton.SkeletonLoader
import org.erdeon.nexis.model.skeleton.animation.{KeyFrameInterpolator, KeyFrameLoader}
import org.erdeon.nexis.utils.Dimension
import org.erdeon.nexis.vulkan.{Buffer, CommandBuffer, DescriptorPool, DescriptorSet, DescriptorSetLayout, Fence, Pipeline, PipelineLayout, RenderCommand, Sampler, Texture, VulkanSystem}
import org.erdeon.nexis.vulkan.shader.ShaderCompiler
import org.lwjgl.util.shaderc.Shaderc
import org.lwjgl.vulkan.{VK10, VkPipelineLayoutCreateInfo, VkPipelineVertexInputStateCreateInfo, VkPushConstantRange}
import org.erdeon.nexis.utils.closeable.*
import org.erdeon.nexis.vulkan.memory.TypeLength
import org.lwjgl.system.{MemoryStack, MemoryUtil}
import org.erdeon.nexis.math.perspective.*
import org.erdeon.nexis.vulkan.frame.{NextFrame, RenderLoop}

object MainBox extends Runnable{

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

    val shaders = ShaderCompiler(true) | { compile =>
      IndexedSeq(
        compile("/shaders/shader.vert", Shaderc.shaderc_glsl_vertex_shader, VK10.VK_SHADER_STAGE_VERTEX_BIT),
        compile("/shaders/shader.frag", Shaderc.shaderc_glsl_fragment_shader, VK10.VK_SHADER_STAGE_FRAGMENT_BIT)
      )
    }

    val boxMesh = (getClass.getResourceAsStream("/models/box/box.msh") | { in => // mesh
      ModelLoader().loadModel(in)
    }).invert(Axis.Y).compile()

    val boxSkeleton = getClass.getResourceAsStream("/models/box/box.skl") | { in => // skeleton
      SkeletonLoader().loadSkeleton(in)
    }

    val root = boxSkeleton(Map("pCube1" -> boxMesh))

    val animations = for(s <- Seq("00", "30")) yield getClass.getResourceAsStream("/models/box/animation/"+s+".ang") | { in =>
      KeyFrameLoader().loadKeyFrame(in).apply(root)
    }

    val interpolator = KeyFrameInterpolator(root)
    interpolator.update(animations(1), animations(1)).apply(1f)

    root.apply(Matrix4f(), Matrix4f())

    val box = boxMesh.vulkanModel

    VulkanSystem("Box", Dimension(1280, 720)) | { sys => // , "NVIDIA GeForce RTX 2050"
      val graphicsQueue = sys.device.graphicsQueue

      using { use =>

        val points = use(Buffer(sys.device, box.vertexesSize, VK10.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT,
          VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)).mapMemory{ memory =>
          val b = MemoryUtil.memFloatBuffer(memory.address, memory.size)
          box.toFloatBuffer(b)
        }

        val indexes = use(Buffer(sys.device, box.indexesSize, VK10.VK_BUFFER_USAGE_INDEX_BUFFER_BIT,
          VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)).mapMemory{ memory =>
          val b = MemoryUtil.memIntBuffer(memory.address, memory.size)
          box.toIntBuffer(b)
        }

        val sampler = use(Sampler(sys.device))

        val descriptorPool = use(DescriptorPool(sys.device, Map(VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER -> 1)))
        val layout = use(DescriptorSetLayout(sys.device, 0, VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER, VK10.VK_SHADER_STAGE_FRAGMENT_BIT))
        val descriptorSet = use(DescriptorSet(descriptorPool, IndexedSeq(layout)))

        val pipelineLayout = use(new PipelineLayout(sys.device) {
          override protected def pipelineLayout(stack: MemoryStack, info: VkPipelineLayoutCreateInfo): Unit = {
            val ranges = VkPushConstantRange.calloc(1, stack)
              .stageFlags(VK10.VK_SHADER_STAGE_VERTEX_BIT)
              .offset(0)
              .size(TypeLength.floatLength(2 * 4 * 4))

            info.pPushConstantRanges(ranges)
            info.pSetLayouts(stack.longs(layout.vkDescriptorLayout))
          }
        })

        val triangle = use(new Pipeline(pipelineLayout, sys.renderPass, shaders) {
          override protected def vertexInput(stack: MemoryStack, info: VkPipelineVertexInputStateCreateInfo): Unit = {
            box.pipeline(stack, info)
          }
        })

        RenderCommand(sys.renderPass) | { render =>

          val texture = use(Texture(sys.device, Dimension(512, 512))).updateDescriptorSet(descriptorSet, 0, sampler)

          Fence(sys.device, false) | { fence =>
            Buffer(sys.device, 512 * 512 * 4, VK10.VK_BUFFER_USAGE_TRANSFER_SRC_BIT,
              VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK10.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT) | { stageBuffer =>

              stageBuffer.mapMemory { memory =>
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
          val camera = use(OrbitCamera(sys.window, perspective(60, sys.windowSize, 1, 1000))).set(cameraPoint, 5, 0, 0)

          new RenderLoop(sys) {

            // cpu calc
            override protected def compute(): Unit = {
              camera.update(cameraPoint)
            }

            override protected def record(next: NextFrame): CommandBuffer = render.record(next) { (stack, buff) =>
              triangle.bindPipeline(buff)

              val viewBuff = stack.callocFloat(2 * 4 * 4)
              camera.viewMatrix.toFloatBuffer(viewBuff)
              camera.rotationMatrix.toFloatBuffer(viewBuff)
              viewBuff.flip()

              VK10.vkCmdPushConstants(buff, pipelineLayout.vkPipelineLayout, VK10.VK_SHADER_STAGE_VERTEX_BIT, 0, viewBuff)

              VK10.vkCmdBindDescriptorSets(buff, VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, pipelineLayout.vkPipelineLayout, 0, stack.longs(descriptorSet.vkDescriptorSet), null)

              VK10.vkCmdBindVertexBuffers(buff, 0, stack.longs(points.vkBuffer), stack.longs(0L))
              VK10.vkCmdBindIndexBuffer(buff, indexes.vkBuffer, 0, VK10.VK_INDEX_TYPE_UINT32) //VK10.vkCmdDraw(buff, 3, 1, 0, 0)
              VK10.vkCmdDrawIndexed(buff, box.indexesCount, 1, 0, 0, 0)
            }

          } | { loop => loop.run() }

        }

      }

    }

  }

}
