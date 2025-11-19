package org.nexis.math

import org.nexis.vulkan.memory.ToFloatBuffer
import java.nio.FloatBuffer

object Vector3f{

  def cross(v1: Vector3f, v2: Vector3f): Vector3f = {
    val tx = v1.y * v2.z - v1.z * v2.y
    val ty = v1.z * v2.x - v1.x * v2.z
    val tz = v1.x * v2.y - v1.y * v2.x

    Vector3f(tx, ty, tz)
  }

  def add(a: Vector3f, b: Vector3f): Vector3f = {
    val x = a.x + b.x
    val y = a.y + b.y
    val z = a.z + b.z

    Vector3f(x, y, z)
  }

  def subtract(a: Vector3f, b: Vector3f): Vector3f = {
    val x = a.x - b.x
    val y = a.y - b.y
    val z = a.z - b.z

    Vector3f(x, y, z)
  }

}

class Vector3f(var x : Float, var y : Float, var z : Float)  extends ToFloatBuffer{

  def this() = {
    this(0f, 0f, 0f)
  }

  def this(v: Vector3f) = {
    this(v.x, v.y, v.z)
  }

  def update(a:Axis, v:Float): this.type = {
    a(this) = v
    this
  }

  def apply(a:Axis): Float = {
    a(this)
  }

  def set(vx: Float, vy:Float, vz:Float): this.type  = {
    x = vx
    y = vy
    z = vz

    this
  }

  def set(v:Vector3f): this.type  = {
    x = v.x
    y = v.y
    z = v.z
    
    this
  }

  def + (b: Vector3f): this.type = {
    add(b)
  }

  def add(a:Vector3f, b:Vector3f):this.type = {
    x = a.x + b.x
    y = a.y + b.y
    z = a.z + b.z

    this
  }

  def add(b:Vector3f):this.type = {
    x += b.x
    y += b.y
    z += b.z

    this
  }

  def - (b:Vector3f) : this.type = {
    subtract(b)
  }

  def subtract(b:Vector3f): this.type = {
    x -= b.x
    y -= b.y
    z -= b.z

    this
  }

  def subtract(a:Vector3f, b:Vector3f) : this.type = {
    x = a.x - b.x
    y = a.y - b.y
    z = a.z - b.z

    this
  }

  def * (m: Float): this.type = {
    multiply(m)
  }

  def multiply(m:Float): this.type = {
    x = x * m
    y = y * m
    z = z * m

    this
  }

  def / (m: Float): this.type = {
    divide(m)
  }

  def divide(m:Float):this.type = {
    x = x / m
    y = y / m
    z = z / m

    this
  }

  def lengthSquared(): Float = {
    x * x + y * y + z * z
  }

  def length(): Float = {
    Math.sqrt(lengthSquared()).toFloat
  }

  def normalize(): this.type = {
    normalize(this)
  }

  def normalize(v:Vector3f): this.type = {
    val l = v.length()

    x = v.x / l
    y = v.y / l
    z = v.z / l

    this
  }

  def cross(v: Vector3f) : this.type = {
    cross(this, v)
  }

  def cross(v1: Vector3f, v2: Vector3f) : this.type = {
    val tx = v1.y * v2.z - v1.z * v2.y
    val ty = v1.z * v2.x - v1.x * v2.z
    val tz = v1.x * v2.y - v1.y * v2.x

    x = tx; y = ty; z = tz

    this
  }

  def dot(a: Vector3f): Float = {
    a.x * this.x + a.y * this.y + a.z * this.z
  }

  override def toFloatBuffer(b: FloatBuffer): FloatBuffer = {
    b.put(x).put(y).put(z)
  }

  override def toString: String = {
    "{" + x + ", " + y + ", " + z + "}"
  }

}
