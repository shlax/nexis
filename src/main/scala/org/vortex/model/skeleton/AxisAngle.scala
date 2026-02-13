package org.vortex.model.skeleton

import org.vortex.math.{Axis, Matrix4f}

class AxisAngle(val axis:Axis){

  var angle:Float = 0f

  def update(a:Float): Unit = {
    angle = a
  }

  def rotation(): Matrix4f = {
    axis.rotate(angle)
  }

  def rotation(m:Matrix4f): Matrix4f = {
    axis.rotate(m, angle)
  }

}
