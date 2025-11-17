package org.erdeon.nexis

import org.erdeon.nexis.math.Axis
import org.erdeon.nexis.model.ModelLoader
import org.erdeon.nexis.model.skeleton.SkeletonLoader
import org.erdeon.nexis.model.skeleton.animation.KeyFrameLoader
import org.erdeon.nexis.utils.Dimension
import org.erdeon.nexis.vulkan.VulkanSystem
import org.erdeon.nexis.vulkan.shader.ShaderCompiler
import org.lwjgl.util.shaderc.Shaderc
import org.lwjgl.vulkan.VK10
import org.erdeon.nexis.utils.closeable.*

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

    val model = boxSkeleton(Map("pCube1" -> boxMesh))

    val animations = for(s <- Seq("00", "30")) yield getClass.getResourceAsStream("/models/box/animation/"+s+".ang") | { in =>
      KeyFrameLoader().loadKeyFrame(in).apply(model)
    }

    VulkanSystem("NXN", Dimension(1280, 720)) | { sys => // , "NVIDIA GeForce RTX 2050"
      val graphicsQueue = sys.device.graphicsQueue

      
    }

  }

}
