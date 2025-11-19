package org.nexis.vulkan.memory

enum TypeLength(val size:Int) {
  case intLength extends TypeLength(4)
  case floatLength extends TypeLength(4)

  def apply(n:Int):Int = {
    size * n
  }

}
