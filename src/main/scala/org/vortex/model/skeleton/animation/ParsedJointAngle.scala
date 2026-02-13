package org.vortex.model.skeleton.animation

import org.vortex.math.{Angle3f, Axis}

class ParsedJointAngle(val axis:Axis, val value:Float) {

  def angle():Float = {
    value * Angle3f.PI / 180f
  }

}