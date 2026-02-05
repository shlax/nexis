package org.nexis.vulkan.interopt;

@FunctionalInterface
public interface ScrollCallback {

    void invoke(long window, double xOffset, double yOffset);

}
