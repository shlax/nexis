package org.nexis.model.skeleton

import org.nexis.math.Matrix4f

class InterpolatedJoint(name:String, val interpolations:Array[InterpolatedAngle],
                          vertexes:Array[SkinVertex], subJoints:Array[AbstractJoint]) extends AbstractJoint(name, vertexes, subJoints){
  
  def apply(modelMatrix:Matrix4f, normalMatrix:Matrix4f):Unit = {
    if(interpolations.length == 1){
      val rot = interpolations.head.rotation()

      val tmp = Matrix4f.multiply(modelMatrix, rot)
      rot.multiplyWith(normalMatrix)

      update(tmp, rot)
    }else{
      val tmp = interpolations.head.rotation()
      val rot = interpolations.tail.head.rotation().multiplyWith(tmp)
      for (i <- interpolations.tail.tail) rot.multiply(i.rotation(tmp))

      tmp.multiply(modelMatrix, rot)
      rot.multiplyWith(normalMatrix)

      update(tmp, rot)
    }
  }

}
