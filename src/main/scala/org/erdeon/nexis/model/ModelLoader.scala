package org.erdeon.nexis.model

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.erdeon.nexis.model.parser.{ModelLexer, ModelParser}

import java.io.InputStream
import java.nio.charset.StandardCharsets

class ModelLoader extends ExceptionErrorListener{

  def loadModel(in: InputStream): ParsedModel = {
    val p = ModelParser(CommonTokenStream(ModelLexer(CharStreams.fromStream(in, StandardCharsets.UTF_8))))
    p.removeErrorListeners()
    p.addErrorListener(this)
    p.model().result
  }

}
