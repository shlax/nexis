package org.ecellon.nexis.model.skeleton

import org.ecellon.nexis.math.{Axis, Matrix4f}

class InterpolatedAngle(val from: AxisAngle, val to:Axis, val value:Float){

  def rotation(): Matrix4f = {
    val angle = value * from.angle
    to.rotate(angle)
  }

  def rotation(m:Matrix4f):Matrix4f = {
    val angle = value * from.angle
    to.rotate(m, angle)
  }

}
