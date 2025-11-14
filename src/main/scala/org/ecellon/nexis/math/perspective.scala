package org.ecellon.nexis.math

import org.ecellon.nexis.utils.Dimension

object perspective{

  def perspective(fovy: Float, dim:Dimension, zNear:Float, zFar:Float) : Matrix4f = {
    val aspect = dim.width.toFloat / dim.height.toFloat
    perspective(fovy, aspect, zNear, zFar)
  }

  def perspective(fovy: Float, aspect:Float, zNear:Float, zFar:Float) : Matrix4f = {
    val tan = Math.tan(fovy.toDouble * Math.PI / 360d).toFloat // fovy / 2

    val m00 = 1f / (tan * aspect)
    val m11 = 1f / tan
    val m22 = zFar / (zFar - zNear)
    val m23 = - (zFar * zNear) / (zFar - zNear)

    Matrix4f(m00, 0f,  0f,  0f,
             0f , m11, 0f,  0f,
             0f , 0f,  m22, m23,
             0f , 0f,  1f,  0f)
  }

}
