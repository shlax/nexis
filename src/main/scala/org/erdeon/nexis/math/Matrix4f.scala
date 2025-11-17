package org.erdeon.nexis.math

import org.erdeon.nexis.vulkan.memory.ToFloatBuffer
import java.nio.FloatBuffer

object Matrix4f{

  def rotateX(angle: Float): Matrix4f = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    Matrix4f(1f, 0f, 0f, 0f,
             0f,  c, -s, 0f,
             0f,  s,  c, 0f,
             0f, 0f, 0f, 1f)
  }

  def rotateY(angle: Float): Matrix4f = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    Matrix4f( c, 0f,  s, 0f,
             0f, 1f, 0f, 0f,
             -s, 0f,  c, 0f,
             0f, 0f, 0f, 1f)
  }

  def rotateZ(angle: Float): Matrix4f = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    Matrix4f( c, -s, 0f, 0f,
              s,  c, 0f, 0f,
             0f, 0f, 1f, 0f,
             0f, 0f, 0f, 1f)
  }

  def multiply(m : Matrix4f, n : Matrix4f):Matrix4f = {
    val t00 = m.m00 * n.m00 + m.m01 * n.m10 + m.m02 * n.m20 + m.m03 * n.m30
    val t01 = m.m00 * n.m01 + m.m01 * n.m11 + m.m02 * n.m21 + m.m03 * n.m31
    val t02 = m.m00 * n.m02 + m.m01 * n.m12 + m.m02 * n.m22 + m.m03 * n.m32
    val t03 = m.m00 * n.m03 + m.m01 * n.m13 + m.m02 * n.m23 + m.m03 * n.m33
    val t10 = m.m10 * n.m00 + m.m11 * n.m10 + m.m12 * n.m20 + m.m13 * n.m30
    val t11 = m.m10 * n.m01 + m.m11 * n.m11 + m.m12 * n.m21 + m.m13 * n.m31
    val t12 = m.m10 * n.m02 + m.m11 * n.m12 + m.m12 * n.m22 + m.m13 * n.m32
    val t13 = m.m10 * n.m03 + m.m11 * n.m13 + m.m12 * n.m23 + m.m13 * n.m33
    val t20 = m.m20 * n.m00 + m.m21 * n.m10 + m.m22 * n.m20 + m.m23 * n.m30
    val t21 = m.m20 * n.m01 + m.m21 * n.m11 + m.m22 * n.m21 + m.m23 * n.m31
    val t22 = m.m20 * n.m02 + m.m21 * n.m12 + m.m22 * n.m22 + m.m23 * n.m32
    val t23 = m.m20 * n.m03 + m.m21 * n.m13 + m.m22 * n.m23 + m.m23 * n.m33
    val t30 = m.m30 * n.m00 + m.m31 * n.m10 + m.m32 * n.m20 + m.m33 * n.m30
    val t31 = m.m30 * n.m01 + m.m31 * n.m11 + m.m32 * n.m21 + m.m33 * n.m31
    val t32 = m.m30 * n.m02 + m.m31 * n.m12 + m.m32 * n.m22 + m.m33 * n.m32
    val t33 = m.m30 * n.m03 + m.m31 * n.m13 + m.m32 * n.m23 + m.m33 * n.m33

    Matrix4f(t00, t01, t02, t03,
             t10, t11, t12, t13,
             t20, t21, t22, t23,
             t30, t31, t32, t33)
  }

}

class Matrix4f(var m00: Float, var m01: Float, var m02: Float, var m03: Float,                         // 0  1  2  3
               var m10: Float, var m11: Float, var m12: Float, var m13: Float,                         // 4  5  6  7
               var m20: Float, var m21: Float, var m22: Float, var m23: Float,                         // 8  9  10 11
               var m30: Float, var m31: Float, var m32: Float, var m33: Float) extends ToFloatBuffer { // 12 13 14 15

  def this() = {
    this(1f, 0f, 0f, 0f,
         0f, 1f, 0f, 0f,
         0f, 0f, 1f, 0f,
         0f, 0f, 0f, 1f)
  }

  def this(v: Vector3f) = {
    this(1f, 0f, 0f, v.x,
         0f, 1f, 0f, v.y,
         0f, 0f, 1f, v.z,
         0f, 0f, 0f, 1f)
  }

  def this(x:Float, y:Float, z:Float) = {
    this(1f, 0f, 0f, x,
         0f, 1f, 0f, y,
         0f, 0f, 1f, z,
         0f, 0f, 0f, 1f)
  }

  def this(v: Matrix4f) = {
    this(v.m00, v.m01, v.m02, v.m03,
         v.m10, v.m11, v.m12, v.m13,
         v.m20, v.m21, v.m22, v.m23,
         v.m30, v.m31, v.m32, v.m33)
  }

  def rotateX(angle: Float): this.type = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    m00 = 1f; m01 = 0f; m02 = 0f; m03 = 0f
    m10 = 0f; m11 =  c; m12 = -s; m13 = 0f
    m20 = 0f; m21 =  s; m22 =  c; m23 = 0f
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def rotateY(angle: Float): this.type = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    m00 =  c; m01 = 0f; m02 =  s; m03 = 0f
    m10 = 0f; m11 = 1f; m12 = 0f; m13 = 0f
    m20 = -s; m21 = 0f; m22 =  c; m23 = 0f
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def rotateZ(angle: Float): this.type = {
    val s = Math.sin(angle.toDouble).toFloat
    val c = Math.cos(angle.toDouble).toFloat

    m00 =  c; m01 = -s; m02 = 0f; m03 = 0f
    m10 =  s; m11 =  c; m12 = 0f; m13 = 0f
    m20 = 0f; m21 = 0f; m22 = 1f; m23 = 0f
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def identity() : this.type = {
    m00 = 1f; m01 = 0f; m02 = 0f; m03 = 0f
    m10 = 0f; m11 = 1f; m12 = 0f; m13 = 0f
    m20 = 0f; m21 = 0f; m22 = 1f; m23 = 0f
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def set(v: Matrix4f): this.type = {
    m00 = v.m00; m01 = v.m01; m02 = v.m02; m03 = v.m03
    m10 = v.m10; m11 = v.m11; m12 = v.m12; m13 = v.m13
    m20 = v.m20; m21 = v.m21; m22 = v.m22; m23 = v.m23
    m30 = v.m30; m31 = v.m31; m32 = v.m32; m33 = v.m33

    this
  }

  def set(x: Float, y:Float, z:Float): this.type = {
    m00 = 1f; m01 = 0f; m02 = 0f; m03 = x
    m10 = 0f; m11 = 1f; m12 = 0f; m13 = y
    m20 = 0f; m21 = 0f; m22 = 1f; m23 = z
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def set(v: Vector3f): this.type = {
    m00 = 1f; m01 = 0f; m02 = 0f; m03 = v.x
    m10 = 0f; m11 = 1f; m12 = 0f; m13 = v.y
    m20 = 0f; m21 = 0f; m22 = 1f; m23 = v.z
    m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f

    this
  }

  def apply(v: Vector3f): Vector3f = {
    apply(v, v)
  }

  def apply(in: Vector3f, out : Vector3f) : Vector3f = {
    val x = m00*in.x + m01*in.y + m02*in.z + m03
    val y = m10*in.x + m11*in.y + m12*in.z + m13
    val z = m20*in.x + m21*in.y + m22*in.z + m23

    out.x = x; out.y = y; out.z = z

    out
  }

  def * (m : Matrix4f): Matrix4f = {
    Matrix4f.multiply(this, m)
  }

  def multiplyWith(m: Matrix4f): this.type = {
    multiply(m, this)
  }

  /** Sets the value of this matrix to the result of multiplying itself with matrix `m` */
  def multiply(m : Matrix4f) : this.type = {
    multiply(this, m)
  }

  def multiply(m : Matrix4f, n : Matrix4f):this.type = {
    val t00 = m.m00 * n.m00 + m.m01 * n.m10 + m.m02 * n.m20 + m.m03 * n.m30
    val t01 = m.m00 * n.m01 + m.m01 * n.m11 + m.m02 * n.m21 + m.m03 * n.m31
    val t02 = m.m00 * n.m02 + m.m01 * n.m12 + m.m02 * n.m22 + m.m03 * n.m32
    val t03 = m.m00 * n.m03 + m.m01 * n.m13 + m.m02 * n.m23 + m.m03 * n.m33
    val t10 = m.m10 * n.m00 + m.m11 * n.m10 + m.m12 * n.m20 + m.m13 * n.m30
    val t11 = m.m10 * n.m01 + m.m11 * n.m11 + m.m12 * n.m21 + m.m13 * n.m31
    val t12 = m.m10 * n.m02 + m.m11 * n.m12 + m.m12 * n.m22 + m.m13 * n.m32
    val t13 = m.m10 * n.m03 + m.m11 * n.m13 + m.m12 * n.m23 + m.m13 * n.m33
    val t20 = m.m20 * n.m00 + m.m21 * n.m10 + m.m22 * n.m20 + m.m23 * n.m30
    val t21 = m.m20 * n.m01 + m.m21 * n.m11 + m.m22 * n.m21 + m.m23 * n.m31
    val t22 = m.m20 * n.m02 + m.m21 * n.m12 + m.m22 * n.m22 + m.m23 * n.m32
    val t23 = m.m20 * n.m03 + m.m21 * n.m13 + m.m22 * n.m23 + m.m23 * n.m33
    val t30 = m.m30 * n.m00 + m.m31 * n.m10 + m.m32 * n.m20 + m.m33 * n.m30
    val t31 = m.m30 * n.m01 + m.m31 * n.m11 + m.m32 * n.m21 + m.m33 * n.m31
    val t32 = m.m30 * n.m02 + m.m31 * n.m12 + m.m32 * n.m22 + m.m33 * n.m32
    val t33 = m.m30 * n.m03 + m.m31 * n.m13 + m.m32 * n.m23 + m.m33 * n.m33

    m00 = t00; m01 = t01; m02 = t02; m03 = t03
    m10 = t10; m11 = t11; m12 = t12; m13 = t13
    m20 = t20; m21 = t21; m22 = t22; m23 = t23
    m30 = t30; m31 = t31; m32 = t32; m33 = t33

    this
  }

  def inverse(): this.type = {
    inverse(this)
  }

  def inverse(m: Matrix4f): this.type = {
    val t00 =  m.m11 * m.m22 * m.m33 - m.m11 * m.m23 * m.m32 - m.m21 * m.m12 * m.m33 + m.m21 * m.m13 * m.m32 + m.m31 * m.m12 * m.m23 - m.m31 * m.m13 * m.m22
    val t10 = -m.m10 * m.m22 * m.m33 + m.m10 * m.m23 * m.m32 + m.m20 * m.m12 * m.m33 - m.m20 * m.m13 * m.m32 - m.m30 * m.m12 * m.m23 + m.m30 * m.m13 * m.m22
    val t20 =  m.m10 * m.m21 * m.m33 - m.m10 * m.m23 * m.m31 - m.m20 * m.m11 * m.m33 + m.m20 * m.m13 * m.m31 + m.m30 * m.m11 * m.m23 - m.m30 * m.m13 * m.m21
    val t30 = -m.m10 * m.m21 * m.m32 + m.m10 * m.m22 * m.m31 + m.m20 * m.m11 * m.m32 - m.m20 * m.m12 * m.m31 - m.m30 * m.m11 * m.m22 + m.m30 * m.m12 * m.m21
    val t01 = -m.m01 * m.m22 * m.m33 + m.m01 * m.m23 * m.m32 + m.m21 * m.m02 * m.m33 - m.m21 * m.m03 * m.m32 - m.m31 * m.m02 * m.m23 + m.m31 * m.m03 * m.m22
    val t11 =  m.m00 * m.m22 * m.m33 - m.m00 * m.m23 * m.m32 - m.m20 * m.m02 * m.m33 + m.m20 * m.m03 * m.m32 + m.m30 * m.m02 * m.m23 - m.m30 * m.m03 * m.m22
    val t21 = -m.m00 * m.m21 * m.m33 + m.m00 * m.m23 * m.m31 + m.m20 * m.m01 * m.m33 - m.m20 * m.m03 * m.m31 - m.m30 * m.m01 * m.m23 + m.m30 * m.m03 * m.m21
    val t31 =  m.m00 * m.m21 * m.m32 - m.m00 * m.m22 * m.m31 - m.m20 * m.m01 * m.m32 + m.m20 * m.m02 * m.m31 + m.m30 * m.m01 * m.m22 - m.m30 * m.m02 * m.m21
    val t02 =  m.m01 * m.m12 * m.m33 - m.m01 * m.m13 * m.m32 - m.m11 * m.m02 * m.m33 + m.m11 * m.m03 * m.m32 + m.m31 * m.m02 * m.m13 - m.m31 * m.m03 * m.m12
    val t12 = -m.m00 * m.m12 * m.m33 + m.m00 * m.m13 * m.m32 + m.m10 * m.m02 * m.m33 - m.m10 * m.m03 * m.m32 - m.m30 * m.m02 * m.m13 + m.m30 * m.m03 * m.m12
    val t22 =  m.m00 * m.m11 * m.m33 - m.m00 * m.m13 * m.m31 - m.m10 * m.m01 * m.m33 + m.m10 * m.m03 * m.m31 + m.m30 * m.m01 * m.m13 - m.m30 * m.m03 * m.m11
    val t32 = -m.m00 * m.m11 * m.m32 + m.m00 * m.m12 * m.m31 + m.m10 * m.m01 * m.m32 - m.m10 * m.m02 * m.m31 - m.m30 * m.m01 * m.m12 + m.m30 * m.m02 * m.m11
    val t03 = -m.m01 * m.m12 * m.m23 + m.m01 * m.m13 * m.m22 + m.m11 * m.m02 * m.m23 - m.m11 * m.m03 * m.m22 - m.m21 * m.m02 * m.m13 + m.m21 * m.m03 * m.m12
    val t13 =  m.m00 * m.m12 * m.m23 - m.m00 * m.m13 * m.m22 - m.m10 * m.m02 * m.m23 + m.m10 * m.m03 * m.m22 + m.m20 * m.m02 * m.m13 - m.m20 * m.m03 * m.m12
    val t23 = -m.m00 * m.m11 * m.m23 + m.m00 * m.m13 * m.m21 + m.m10 * m.m01 * m.m23 - m.m10 * m.m03 * m.m21 - m.m20 * m.m01 * m.m13 + m.m20 * m.m03 * m.m11
    val t33 =  m.m00 * m.m11 * m.m22 - m.m00 * m.m12 * m.m21 - m.m10 * m.m01 * m.m22 + m.m10 * m.m02 * m.m21 + m.m20 * m.m01 * m.m12 - m.m20 * m.m02 * m.m11

    val det = 1f / (m00 * t00 + m01 * t10 + m02 * t20 + m03 * t30)

    m00 = t00 * det; m01 = t01 * det; m02 = t02 * det; m03 = t03 * det
    m10 = t10 * det; m11 = t11 * det; m12 = t12 * det; m13 = t13 * det
    m20 = t20 * det; m21 = t21 * det; m22 = t22 * det; m23 = t23 * det
    m30 = t30 * det; m31 = t31 * det; m32 = t32 * det; m33 = t33 * det

    this
  }

  def transpose(): this.type = {
    val tmp01 = m10
    this.m10 = this.m01
    this.m01 = tmp01

    val tmp20 = this.m20
    this.m20 = this.m02
    this.m02 = tmp20

    val tmp30 = this.m30
    this.m30 = this.m03
    this.m03 = tmp30

    val tmp21 = this.m21
    this.m21 = this.m12
    this.m12 = tmp21

    val tmp31 = this.m31
    this.m31 = this.m13
    this.m13 = tmp31

    val tmp32 = this.m32
    this.m32 = this.m23
    this.m23 = tmp32

    this
  }

  override def toFloatBuffer(b: FloatBuffer): FloatBuffer = {
    b.put(m00).put(m10).put(m20).put(m30)
    b.put(m01).put(m11).put(m21).put(m31)
    b.put(m02).put(m12).put(m22).put(m32)
    b.put(m03).put(m13).put(m23).put(m33)
  }
  
  override def toString: String = {
    "{ {" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + "}, " +
      "{" + m10 + ", " + m11 + ", " + m12 + ", " + m13 + "}, " +
      "{" + m20 + ", " + m21 + ", " + m22 + ", " + m23 + "}, " +
      "{" + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "} }"
  }

}
