package org.ecellon.nexis.model.skeleton.animation

import org.junit.jupiter.api.{Assertions, Test}

class InterpolatorTest {

  @Test
  def main(): Unit = {
    val d = Seq(1f, -1f, -1f, 0f, 1f, 1f, -1f, -1f)
    val i = new Interpolator

    def time(): Unit = {
      var t = 0.1f
      while (t < 1) {
        val z = i.apply(t)
        Assertions.assertTrue(z >= -1f && z <= 1f)
        //println(z)
        t += 0.1f
      }
      val z = i.apply(1f)
      Assertions.assertTrue(z >= -1f && z <= 1f)
      //println(z)
    }

    i.update(d(0), d(1))
    val z = i.apply(0f)
    Assertions.assertTrue(z >= -1f && z <= 1f)
    //println(z)
    for(j <- 1 until d.length - 1){
      time()
      i.update(d(j), d(j+1))
    }
    time()
  }

}
