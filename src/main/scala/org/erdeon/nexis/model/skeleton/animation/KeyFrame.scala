package org.erdeon.nexis.model.skeleton.animation

class KeyFrame(val angles: Array[Float]) {
  
  def apply(i:Int):Float = angles(i)
  
}

