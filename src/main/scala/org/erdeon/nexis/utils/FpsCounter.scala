package org.erdeon.nexis.utils

import java.util.function.Consumer

class FpsCounter(mod:Int = 135, report:Int = 90) {

  private val times = new Array[Long](mod)
  private var ind = 0

  private var last:Long = System.nanoTime()
  private var act = -1

  def apply(c: Consumer[Double]): Unit = {
    val t = System.nanoTime()
    times(ind) = t - last
    last = t

    ind += 1
    if(ind == mod){
      ind = 0
      if(act == -1){
        act = 0
      }
    }

    if(act >= 0) {
      act += 1
      if (act == report) {
        act = 0
        val avg = times.sum.doubleValue / mod.doubleValue
        c.accept(1e9d / avg)
      }
    }

  }

}
