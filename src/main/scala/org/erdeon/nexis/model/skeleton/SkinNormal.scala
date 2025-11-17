package org.erdeon.nexis.model.skeleton

import org.erdeon.nexis.math.{Matrix4f, Vector3f}

object SkinNormal{

  def apply(normal:Vector3f):SkinNormal = {
    new SkinNormal(Vector3f(normal), normal)
  }

}

class SkinNormal(val inNormal:Vector3f, val outNormal:Vector3f) {

  def apply(normalMatrix: Matrix4f): Unit = {
    normalMatrix(inNormal, outNormal)
  }

  def is(v:Vector3f):Boolean = {
    outNormal.eq(v)
  }

}
