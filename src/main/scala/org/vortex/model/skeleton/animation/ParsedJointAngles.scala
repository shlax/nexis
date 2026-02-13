package org.vortex.model.skeleton.animation

import org.vortex.math.Axis

class ParsedJointAngles(val name:String, val angles:Array[ParsedJointAngle]) {

  def apply(a:Axis):Option[ParsedJointAngle] = {
    var res:Option[ParsedJointAngle] = None
    for(i <- angles if i.axis == a){
      res = Some(i)
    }
    res
  }

}
