package org.vortex.vulkan

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.{VK10, VkCommandBuffer, VkGraphicsPipelineCreateInfo, VkPipelineColorBlendAttachmentState, VkPipelineColorBlendStateCreateInfo, VkPipelineDepthStencilStateCreateInfo, VkPipelineDynamicStateCreateInfo, VkPipelineInputAssemblyStateCreateInfo, VkPipelineMultisampleStateCreateInfo, VkPipelineRasterizationStateCreateInfo, VkPipelineShaderStageCreateInfo, VkPipelineVertexInputStateCreateInfo, VkPipelineViewportStateCreateInfo, VkRect2D, VkViewport}
import org.vortex.utils.closeable.*
import org.vortex.vulkan.shader.CompiledShader

abstract class Pipeline(val pipelineLayout: PipelineLayout, val renderPass: RenderPass,
               compiledShaders:IndexedSeq[CompiledShader],
               topology:Int = VK10.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST,
               polygonMode:Int = VK10.VK_POLYGON_MODE_FILL,
               cullMode:Int = VK10.VK_CULL_MODE_BACK_BIT,
               frontFace:Int = VK10.VK_FRONT_FACE_CLOCKWISE,
               colorAttachmentsCount:Int = 1 ) extends AutoCloseable{

  protected def createShaderModules(modules:IndexedSeq[CompiledShader]):IndexedSeq[ShaderModule] = {
    val dev = renderPass.swapChain.device
    for(c <- modules) yield ShaderModule(dev, c)
  }

  /** customize VkPipelineVertexInputStateCreateInfo */
  protected def vertexInput(stack:MemoryStack, info:VkPipelineVertexInputStateCreateInfo):Unit

  protected def initPipeline(compiledShaders:IndexedSeq[CompiledShader],
       topology:Int, polygonMode:Int, cullMode:Int, frontFace:Int, colorAttachmentsCount:Int): Long = MemoryStack.stackPush() | { stack =>
    val shaderModules = createShaderModules(compiledShaders)

    val stages = VkPipelineShaderStageCreateInfo.calloc(shaderModules.length, stack)
    for(i <- shaderModules.indices){
      val s = shaderModules(i)
      val nm = stack.UTF8(s.name)

      stages.get(i)
        .sType$Default()
        .module(s.shaderModule)
        .stage(s.stage)
        .pName(nm)
    }

    val vertexInputInfo = VkPipelineVertexInputStateCreateInfo.calloc(stack)
      .sType$Default()

    vertexInput(stack, vertexInputInfo)

    val inputAssembly = VkPipelineInputAssemblyStateCreateInfo.calloc(stack)
      .sType$Default()
      .topology(topology)
      .primitiveRestartEnable(false)

    val dim = renderPass.swapChain.dimension

    val viewports = VkViewport.calloc(1, stack)
    viewports.get(0).x(0).y(0)
      .width(dim.width.toFloat).height(dim.height.toFloat)
      .minDepth(0f).maxDepth(1f)

    val scissors = VkRect2D.calloc(1, stack)
    val scissor = scissors.get(0)
    scissor.offset().x(0).y(0)
    scissor.extent().width(dim.width).height(dim.height)

    val viewport = VkPipelineViewportStateCreateInfo.calloc(stack)
      .sType$Default()
      .viewportCount(1)
      .pViewports(viewports)
      .scissorCount(1)
      .pScissors(scissors)

    val rasterization = VkPipelineRasterizationStateCreateInfo.calloc(stack)
      .sType$Default()
      .depthBiasEnable(false)
      .polygonMode(polygonMode)
      .cullMode(cullMode)
      .frontFace(frontFace)
      .lineWidth(1f)

    val multisample = VkPipelineMultisampleStateCreateInfo.calloc(stack)
      .sType$Default()
      .sampleShadingEnable(false)
      .rasterizationSamples(VK10.VK_SAMPLE_COUNT_1_BIT)

    val colorBlendAttachments = VkPipelineColorBlendAttachmentState.calloc(colorAttachmentsCount, stack)
    for(i <- 0 until colorAttachmentsCount){
      colorBlendAttachments.get(i)
        .blendEnable(false)
        .colorWriteMask(VK10.VK_COLOR_COMPONENT_R_BIT | VK10.VK_COLOR_COMPONENT_G_BIT | VK10.VK_COLOR_COMPONENT_B_BIT | VK10.VK_COLOR_COMPONENT_A_BIT)
    }

    val colorBlend = VkPipelineColorBlendStateCreateInfo.calloc(stack)
      .sType$Default()
      .pAttachments(colorBlendAttachments)

    val dynamic = VkPipelineDynamicStateCreateInfo.calloc(stack)
      .sType$Default()
      //.pDynamicStates(stack.ints(VK10.VK_DYNAMIC_STATE_VIEWPORT, VK10.VK_DYNAMIC_STATE_SCISSOR))

    val depthInfo = VkPipelineDepthStencilStateCreateInfo.calloc(stack)
      .sType$Default()
      .depthTestEnable(true)
      .depthWriteEnable(true)
      .depthCompareOp(VK10.VK_COMPARE_OP_LESS)
      .depthBoundsTestEnable(false)
      .stencilTestEnable(false)

    val info = VkGraphicsPipelineCreateInfo.calloc(1, stack)
      .sType$Default()
      .pStages(stages)
      .pVertexInputState(vertexInputInfo)
      .pInputAssemblyState(inputAssembly)
      .pViewportState(viewport)
      .pRasterizationState(rasterization)
      .pMultisampleState(multisample)
      .pColorBlendState(colorBlend)
      .pDynamicState(dynamic)
      .layout(pipelineLayout.vkPipelineLayout)
      .renderPass(renderPass.vkRenderPass)
      .pDepthStencilState(depthInfo)

    val pl = stack.callocLong(1)
    vkCheck(VK10.vkCreateGraphicsPipelines(renderPass.swapChain.device.vkDevice, VK10.VK_NULL_HANDLE, info, null, pl))
    val pipeline = pl.get(0)

    /** allowed to destroy the shader modules again as soon as pipeline creation is finished */
    for (m <- shaderModules) m.close()

    pipeline
  }

  val vkPipeline:Long = initPipeline(compiledShaders, topology, polygonMode, cullMode, frontFace, colorAttachmentsCount)

  def bindPipeline(commandBuffer:VkCommandBuffer, compute:Boolean = false):Unit = {
    VK10.vkCmdBindPipeline(commandBuffer, if(compute) VK10.VK_PIPELINE_BIND_POINT_COMPUTE else VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, vkPipeline)
  }

  override def close(): Unit = {
    VK10.vkDestroyPipeline(renderPass.swapChain.device.vkDevice, vkPipeline, null)
  }
}
