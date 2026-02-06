package org.nexis.vulkan.interoperability;

@FunctionalInterface
public interface PositionCallback {

    void invoke(long window, double x, double y);

}
