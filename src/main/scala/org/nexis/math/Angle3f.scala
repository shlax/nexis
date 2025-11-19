package org.nexis.math

object Angle3f{
  val PI: Float = Math.PI.toFloat
}

class Angle3f(start:Float = 0, min:Float = -Angle3f.PI * 2f, max:Float = Angle3f.PI * 2f) {
  private var angle: Float = start

  def apply(): Float = {
    angle
  }
  
  def add(v: Float): Float = {
    angle += v

    if (angle < min){
      angle += Angle3f.PI * 2f
    }else if (angle > max){
      angle -= Angle3f.PI * 2f
    }

    angle
  }

  def set(v: Float): this.type = {
    angle = v
    this
  }
  
  override def toString: String = {
    "<"+angle+">"
  }
}
