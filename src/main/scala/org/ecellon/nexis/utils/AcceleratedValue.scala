package org.ecellon.nexis.utils

class AcceleratedValue {

  private var lastS = 0f
  private var lastV = 0f

  private var v0 = 0f
  private var v1 = 0f

  private var a1 = 0f
  private var a2 = 0f

  private var s0 = 0f
  private var s1 = 0f

  def set(value: Float, nextValue:Float):Unit = {
    s0 += lastS

    val s2 = value - s0

    val s3 = nextValue - value
    val v2 = if (s3 != 0f && Math.signum(s2) == Math.signum(s3)) nextValue - s0 else 0f

    v0 = lastV

    a1 = 4f * s2 - 3f * v0 - v2
    v1 = v0 + a1 / 2f

    a2 = 2f * v2 - 2f * v1

    s1 = v0 / 2f + a1 / 8f
  }

  def apply(t:Float):Float = {
    if (t <= 0.5f) {
      val at = a1 * t
      lastV = v0 + at
      lastS = v0 * t + (at * t) / 2f
    } else {
      val dt = t - 0.5f
      val adt = a2 * dt
      lastV = v1 + adt
      lastS = v1 * dt + (adt * dt) / 2f + s1
    }

    s0 + lastS
  }

}
