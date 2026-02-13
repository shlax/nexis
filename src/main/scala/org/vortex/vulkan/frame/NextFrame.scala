package org.vortex.vulkan.frame

case class NextFrame(index:Int, presentResult:Option[PresentResult])
