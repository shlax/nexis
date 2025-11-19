package org.erdeon.nexis.model.skeleton.animation

import org.erdeon.nexis.math.{Angle3f, Axis}

class ParsedJointAngle(val axis:Axis, val value:Float) {

  def angle():Float = {
    value * Angle3f.PI / 180f
  }

}