package org.nexis.vulkan.interopt;

@FunctionalInterface
public interface PositionCallback {

    void invoke(long window, double x, double y);

}
