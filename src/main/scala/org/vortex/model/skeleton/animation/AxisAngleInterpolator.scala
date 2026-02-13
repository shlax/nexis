package org.vortex.model.skeleton.animation

import org.vortex.model.skeleton.AxisAngle

class AxisAngleInterpolator(val angle:AxisAngle) {
  val interpolator = Interpolator()

  /** time from 0 to 1 */
  def apply(t: Float): Unit = {
    val a = interpolator.apply(t)
    angle.update(a)
  }

  def update(value: Float, nextValue:Float):Unit = {
    val to = if (java.lang.Float.isNaN(value)) interpolator.value() else value
    val next = if (java.lang.Float.isNaN(nextValue)) to else nextValue

    interpolator.update(to, next)
  }

}
