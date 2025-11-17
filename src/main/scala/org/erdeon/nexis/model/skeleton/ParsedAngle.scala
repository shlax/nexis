package org.erdeon.nexis.model.skeleton

import org.erdeon.nexis.math.Axis

class ParsedAngle(val from:Axis, val to:Axis, val value:Float){

  def apply(parent:RotatingJoint): InterpolatedAngle = {
    InterpolatedAngle(parent.angle(from), to, value)
  }

}
