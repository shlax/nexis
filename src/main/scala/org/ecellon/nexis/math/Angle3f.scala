package org.ecellon.nexis.math

object Angle3f{
  val PIx2: Float = (Math.PI * 2d).toFloat
}

class Angle3f(start:Float = 0, min:Float = -Angle3f.PIx2, max:Float = Angle3f.PIx2) {
  private var angle: Float = start

  def apply(): Float = {
    angle
  }
  
  def add(v: Float): Float = {
    angle += v

    if (angle < min){
      angle += Angle3f.PIx2
    }else if (angle > max){
      angle -= Angle3f.PIx2
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
