package org.nexis.math

import org.nexis.vulkan.memory.ToFloatBuffer
import java.nio.FloatBuffer

object Vector2f{

  def add(a: Vector2f, b: Vector2f): Vector2f = {
    val x = a.x + b.x
    val y = a.y + b.y

    Vector2f(x, y)
  }

  def subtract(a: Vector2f, b: Vector2f): Vector2f = {
    val x = a.x - b.x
    val y = a.y - b.y

    Vector2f(x, y)
  }

}

class Vector2f(var x : Float, var y : Float) extends ToFloatBuffer{

  def this() = this(0f, 0f)

  def + (b: Vector2f): this.type = {
    add(b)
  }

  def add(a: Vector2f, b: Vector2f): this.type = {
    x = a.x + b.x
    y = a.y + b.y

    this
  }

  def add(b: Vector2f): this.type = {
    x += b.x
    y += b.y

    this
  }

  def - (b: Vector2f): this.type = {
    subtract(b)
  }

  def subtract(b: Vector2f): this.type = {
    x -= b.x
    y -= b.y

    this
  }

  def subtract(a: Vector2f, b: Vector2f): this.type = {
    x = a.x - b.x
    y = a.y - b.y

    this
  }

  def * (m: Float): this.type = {
    multiply(m)
  }

  def multiply(m: Float): this.type = {
    x = x * m
    y = y * m

    this
  }

  def / (m: Float): this.type = {
    divide(m)
  }

  def divide(m: Float): this.type = {
    x = x / m
    y = y / m

    this
  }

  def lengthSquared(): Float = {
    x * x + y * y
  }

  def length(): Float = {
    Math.sqrt(lengthSquared()).toFloat
  }

  def normalize(): this.type = {
    normalize(this)
  }

  def normalize(v: Vector2f): this.type = {
    val l = v.length()

    x = v.x / l
    y = v.y / l

    this
  }

  def dot(a: Vector2f): Float = {
    a.x * this.x + a.y * this.y
  }

  /** return z */
  def cross(v: Vector2f): Float = {
    this.x * v.y - v.x * this.y
  }

  override def toFloatBuffer(b:FloatBuffer):FloatBuffer = {
    b.put(x).put(y)
  }
  
  override def toString: String = {
    "{" + x + ", " + y + "}"
  }

}
