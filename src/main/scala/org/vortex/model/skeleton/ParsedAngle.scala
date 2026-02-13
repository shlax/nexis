package org.vortex.model.skeleton

import org.vortex.math.Axis

class ParsedAngle(val from:Axis, val to:Axis, val value:Float){

  def apply(parent:RotatingJoint): InterpolatedAngle = {
    InterpolatedAngle(parent.angle(from), to, value)
  }

}
