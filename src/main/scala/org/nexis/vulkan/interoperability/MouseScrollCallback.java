package org.nexis.vulkan.interoperability;

import org.lwjgl.glfw.GLFWScrollCallbackI;

public class MouseScrollCallback implements GLFWScrollCallbackI {
    private final ScrollCallback scrollCallback;

    public MouseScrollCallback(ScrollCallback scrollCallback) {
        this.scrollCallback = scrollCallback;
    }

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        scrollCallback.invoke(window, xoffset, yoffset);
    }

}
