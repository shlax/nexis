package org.nexis.model.skeleton.animation

import org.nexis.model.skeleton.{AbstractJoint, RotatingJoint}

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

  /** time from 0 to 1 */
  def apply(t:Float):this.type = {
    for(i <- interpolators) i.apply(t)
    this
  }

  def update(to:KeyFrame, next:KeyFrame): this.type = {
    for(i <- interpolators.indices) interpolators(i).update(to(i), next(i))
    this
  }

}
