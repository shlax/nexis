package org.nexis.vulkan.shader

import org.lwjgl.util.shaderc.Shaderc
import org.nexis.utils.closeable.*

import scala.io.Source

class ShaderCompiler(debug: Boolean = false) extends AutoCloseable{

  val compiler:Long = Shaderc.shaderc_compiler_initialize()
  val options: Long = Shaderc.shaderc_compile_options_initialize()
  
  if(debug) Shaderc.shaderc_compile_options_set_generate_debug_info(options)

  def apply(uri:String, shaderType:Int, stage:Int, entryPoint:String = "main"): CompiledShader = {
    val src = this.getClass.getResourceAsStream(uri)|{ in =>
      Source.fromInputStream(in, "UTF-8").getLines().mkString("\n")
    }
    val res = Shaderc.shaderc_compile_into_spv(compiler, src, shaderType, uri, entryPoint, options)
    if (Shaderc.shaderc_result_get_compilation_status(res) != Shaderc.shaderc_compilation_status_success) {
      throw RuntimeException("Shader compilation failed: " + Shaderc.shaderc_result_get_error_message(res))
    }

    val buff = Shaderc.shaderc_result_get_bytes(res)
    val compiled = new Array[Byte](buff.remaining())
    buff.get(compiled)

    CompiledShader(compiled, stage, entryPoint)
  }

  override def close(): Unit = {
    Shaderc.shaderc_compile_options_release(options)
    Shaderc.shaderc_compiler_release(compiler)
  }

}
