package org.erdeon.nexis.model.skeleton.animation

class Interpolator{
  var s = 0f
  var t = 0f

  var s0 = 0f
  var v0 = 0f
  var a0 = 0f

  var s1 = 0f
  var v1 = 0f
  var a1 = 0f

  def update(value: Float, nextValue:Float):Unit = {
    v0 = if(t <= 0.5f) v0 + a0 * t else v1 - a1 * (1f - t)
    s0 = s

    val s01 = value - s0
    val s12 = nextValue - value

    s1 = value
    v1 = if(s12 != 0f && s01 != 0f && Math.signum(s01) == Math.signum(s12) ) s12 else 0f

    // solve a0 a1  t = 0.5
    // v1 = v0 + a0 * (t/2) + a1 * (t/2)
    // s1 = s0 + v0 * (t/2) + a0 * (t/2)^2 / 2 + v1 * (t/2) + a1 * (t/2)^2 / 2
    // v0 + a0 * (t/2) = v1 + a1 * (t/2)

    // a1 = (a0 t + 2 v0 - 2 v1)/t and t!=0
    // a0 = -(2 (v0 - v1))/t and t!=0
    // a0 = -(4 s0 - 4 s1 + 3 t v0 + t v1)/t^2 and t!=0

    a0 = 4f * s1 - 4f * s0 - 3f * v0 - v1
    a1 = a0 + 2f * v0 - 2f * v1
  }

  /** time from 0 to 1 */
  def apply(tt: Float): Float = {
    t = tt
    if(tt <= 0.5f){
      s = s0 + v0 * tt + a0 * tt * tt / 2f
    }else{
      val tm = 1f - tt
      s = s1 - v1 * tm - a1 * tm * tm / 2f
    }
    s
  }

  def value():Float = s

}
