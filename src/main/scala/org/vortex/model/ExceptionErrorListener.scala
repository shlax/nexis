package org.vortex.model

import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.{ANTLRErrorListener, Parser, RecognitionException, Recognizer}
import java.util

class ExceptionErrorListener extends ANTLRErrorListener{

  override def syntaxError(recognizer: Recognizer[?, ?], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    throw RuntimeException("line " + line + ":" + charPositionInLine + " " + msg, e)
  }

  override def reportAmbiguity(recognizer: Parser, dfa: DFA, startIndex: Int, stopIndex: Int, exact: Boolean, ambigAlts: util.BitSet, configs: ATNConfigSet): Unit = {
    throw RuntimeException("index " + startIndex + ":" + stopIndex)
  }

  override def reportAttemptingFullContext(recognizer: Parser, dfa: DFA, startIndex: Int, stopIndex: Int, conflictingAlts: util.BitSet, configs: ATNConfigSet): Unit = {
    throw RuntimeException("index " + startIndex + ":" + stopIndex)
  }

  override def reportContextSensitivity(recognizer: Parser, dfa: DFA, startIndex: Int, stopIndex: Int, prediction: Int, configs: ATNConfigSet): Unit = {
    throw RuntimeException("index " + startIndex + ":" + stopIndex + ":" + prediction)
  }

}
