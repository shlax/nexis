package org.ecellon.nexis.model.skeleton

import org.ecellon.nexis.math.{Axis, Matrix4f}

class AxisAngle(val axis:Axis){

  var angle:Float = 0f

  def rotation(): Matrix4f = {
    axis.rotate(angle)
  }

  def rotation(m:Matrix4f): Matrix4f = {
    axis.rotate(m, angle)
  }

}
