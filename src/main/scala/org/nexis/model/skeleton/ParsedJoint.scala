package org.nexis.model.skeleton

import org.nexis.math.{Axis, Vector3f}
import org.nexis.model.CompiledModel

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ParsedJoint(val name:String, val point: Vector3f, val axis:Array[Axis], val angles:Array[ParsedAngle],
                    val binding: Array[ParsedBinding], val subJoints: Array[ParsedJoint]){

  if(point == null && angles == null) throw IllegalArgumentException("point == null && angles == null")
  if(point != null && angles != null) throw IllegalArgumentException("point != null && angles != null")
  if(point != null && axis == null) throw IllegalArgumentException("point != null && axis == null")
  if(angles != null){
    if(angles.length == 0) throw IllegalArgumentException("angles == 0")
    if(angles.length > 3) throw IllegalArgumentException("angles > 3 :" + angles.length)
    if(angles.groupBy(_.to).maxBy(_._2.length)._2.length > 1) throw IllegalArgumentException("duplicate to angle value")
  }else if (axis != null) {
    if (axis.length == 0) throw IllegalArgumentException("axis == 0")
    if (axis.length > 3) throw IllegalArgumentException("axis > 3 :" + axis.length)
    if (axis.groupBy(_.name()).maxBy(_._2.length)._2.length > 1) throw IllegalArgumentException("duplicate axis value")
  }

  def apply(models:Map[String, CompiledModel], offset:Vector3f = Vector3f(), parent:Option[RotatingJoint] = None):AbstractJoint = {
    val off = if(point == null) offset else Vector3f(offset).add(point)

    val vertexes = ArrayBuffer[SkinVertex]()
    if(binding != null) {
      for (b <- binding) {
        val m = models(b.mesh)
        for (i <- b.indexes) {
          vertexes += SkinVertex(off, m(i))
        }
      }
    }

    val sub = if(subJoints == null) new Array[AbstractJoint](0) else new Array[AbstractJoint](subJoints.length)

    val ret = if(point != null) RotatingJoint(name.intern(), point, axis.map(a => AxisAngle(a)), vertexes.toArray, sub)
      else{
        val ang = angles.map(i => i(parent.get))
        InterpolatedJoint(name.intern(), ang, vertexes.toArray, sub)
      }

    val par = ret match {
        case j: RotatingJoint => Some(j)
        case _ => parent
      }

    if(subJoints != null) {
      for (i <- subJoints.zipWithIndex) sub(i._2) = i._1(models, off, par)
    }

    ret
  }

}
