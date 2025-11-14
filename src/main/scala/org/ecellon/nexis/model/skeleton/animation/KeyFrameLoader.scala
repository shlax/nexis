package org.ecellon.nexis.model.skeleton.animation

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.ecellon.nexis.model.ExceptionErrorListener
import org.ecellon.nexis.model.skeleton.animation.parser.{KeyFrameParser, KeyFrameLexer}

import java.io.InputStream
import java.nio.charset.StandardCharsets

class KeyFrameLoader extends ExceptionErrorListener{

  def loadKeyFrame(in: InputStream): ParsedKeyFrame = {
    val p = KeyFrameParser(CommonTokenStream(KeyFrameLexer(CharStreams.fromStream(in, StandardCharsets.UTF_8))))
    p.removeErrorListeners()
    p.addErrorListener(this)
    p.keyFrame().result
  }

}
