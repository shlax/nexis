package org.ecellon.nexis.model

import org.ecellon.nexis.math.{Axis, Vector2f, Vector3f}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ParsedModel(val points:Array[Vector3f], val faces:Array[ParsedTriangle]){

  def invert(a:Axis):this.type = {
    for(p <- points) p(a) = -1f * p(a)
    for(f <- faces) f.invert(a)
    this
  }

  def compile(epsilon:Float = 0.0000001f):CompiledModel = {
    class IndexVertex(val index:Int, val vertex:Vertex)

    val vertexes = new Array[ArrayBuffer[IndexVertex]](points.length)
    val indexes = new Array[IndexedTriangle](faces.length)
    var ind = 0

    def eqVector2f(a: Vector2f, b: Vector2f): Boolean = {
      Math.abs(a.x - b.x) < epsilon && Math.abs(a.y - b.y) < epsilon
    }

    def eqVector3f(a:Vector3f, b:Vector3f):Boolean = {
      Math.abs(a.x - b.x) < epsilon && Math.abs(a.y - b.y) < epsilon && Math.abs(a.z - b.z) < epsilon
    }

    def eqVertex(a:Vertex, b:ParsedVertex):Boolean = {
      eqVector3f(a.point, points(b.index)) && eqVector3f(a.normal, b.normal)
        && a.uvs.zipWithIndex.forall( (uv, i) => eqVector2f(uv, b.uvs(i)) )
    }

    var next = 0
    def put(pv:ParsedVertex):Int = {
      var v = vertexes(pv.index)
      if(v == null){
        v = ArrayBuffer[IndexVertex]()
        vertexes(pv.index) = v
      }

      v.find( x => eqVertex(x.vertex, pv) ).map(_.index).getOrElse {
        val act = next
        val normal = v.map(_.vertex.normal).find( i => eqVector3f(i, pv.normal) ).getOrElse(pv.normal)
        val uvs = pv.uvs.zipWithIndex.map( i => v.map(_.vertex.uvs(i._2)).find( j => eqVector2f(j, i._1) ).getOrElse(i._1) )
        v += IndexVertex(act, Vertex(points(pv.index), normal, uvs))
        next += 1
        act
      }
    }

    for(f <- faces){
      indexes(ind) = IndexedTriangle(put(f.a), put(f.b), put(f.c))
      ind += 1
    }

    val vertexArray = new Array[Vertex](next)
    for(i <- vertexes; j <- i) vertexArray(j.index) = j.vertex

    ind = 0
    val indMap = new Array[Array[Int]](vertexes.length)
    for(i <- vertexes){
      indMap(ind) = i.map(_.index).toArray
      ind += 1
    }

    CompiledModel(IndexedModel(vertexArray, indexes), indMap)
  }

}
