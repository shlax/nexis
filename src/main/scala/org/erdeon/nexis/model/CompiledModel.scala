package org.erdeon.nexis.model

class CompiledModel(val vulkanModel: IndexedModel, indexes:Array[Array[Int]]){

  def apply(i:Int):Array[Vertex] = {
    indexes(i).map( i => vulkanModel.vertexes(i) )
  }

}
