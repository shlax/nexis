package org.vortex.model.skeleton.animation

import org.vortex.model.skeleton.{AbstractJoint, RotatingJoint}

import scala.collection.mutable

class ParsedKeyFrame(jointsAngles: Array[ParsedJointAngles]) {

  def apply(j:AbstractJoint): KeyFrame = {
    val angles = mutable.ArrayBuffer[Float]()

    j.traverse{
      case r : RotatingJoint =>
        val ang = jointsAngles.find(_.name == r.name)
        for(a <- r.angles){
          var v: Float = Float.NaN
          for(pa <- ang; x <- pa(a.axis)){
            v = x.angle()
          }
          angles += v
        }
    }

    KeyFrame(angles.toArray)
  }

}
