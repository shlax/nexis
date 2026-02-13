package org.vortex.model.skeleton

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.vortex.model.skeleton.parser.{SkeletonLexer, SkeletonParser}
import org.vortex.model.ExceptionErrorListener

import java.io.InputStream
import java.nio.charset.StandardCharsets

class SkeletonLoader extends ExceptionErrorListener{

  def loadSkeleton(in: InputStream): ParsedJoint = {
    val p = SkeletonParser(CommonTokenStream(SkeletonLexer(CharStreams.fromStream(in, StandardCharsets.UTF_8))))
    p.removeErrorListeners()
    p.addErrorListener(this)
    p.skeleton().result
  }

}
