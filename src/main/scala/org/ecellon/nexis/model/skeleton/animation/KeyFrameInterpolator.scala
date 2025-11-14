package org.ecellon.nexis.model.skeleton.animation

import org.ecellon.nexis.model.skeleton.{AbstractJoint, RotatingJoint}

import scala.collection.mutable

class KeyFrameInterpolator(j:AbstractJoint) {

  val interpolators:Array[AxisAngleInterpolator] = {
    val i = mutable.ArrayBuffer[AxisAngleInterpolator]()
    j.traverse {
      case r: RotatingJoint =>
        for (a <- r.angles) {
          i += AxisAngleInterpolator(a)
        }
    }
    i.toArray
  }

  def update(to:KeyFrame, next:KeyFrame): Unit = {
    for(i <- interpolators.indices){
      interpolators(i).update(to(i), next(i))
    }
  }

}
