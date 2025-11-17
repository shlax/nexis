package org.erdeon.nexis.model.skeleton

import org.erdeon.nexis.math.Matrix4f

abstract class AbstractJoint(val name:String, val vertexes:Array[SkinVertex], val subJoints:Array[AbstractJoint]){

  def traverse(fn: PartialFunction[AbstractJoint, Unit]):Unit = {
    if(fn.isDefinedAt(this)){
      fn.apply(this)
    }
    for(s <- subJoints){
      s.traverse(fn)
    }
  }

  def apply(modelMatrix:Matrix4f, normalMatrix:Matrix4f):Unit

  def update(modelMatrix:Matrix4f, normalMatrix:Matrix4f):Unit = {
    for(s <- subJoints) s(modelMatrix, normalMatrix)
    for(v <- vertexes) v(modelMatrix, normalMatrix)
  }

}
