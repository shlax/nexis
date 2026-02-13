package org.vortex.vulkan.interoperability;

@FunctionalInterface
public interface ScrollCallback {

    void invoke(long window, double xOffset, double yOffset);

}
