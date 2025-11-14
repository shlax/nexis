package org.ecellon.nexis.model.skeleton

import org.ecellon.nexis.math.Axis

class ParsedAngle(val from:Axis, val to:Axis, val value:Float){

  def apply(parent:RotatingJoint): InterpolatedAngle = {
    InterpolatedAngle(parent.angle(from), to, value)
  }

}
