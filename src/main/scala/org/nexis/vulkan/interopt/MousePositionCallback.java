package org.nexis.vulkan.interopt;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MousePositionCallback implements GLFWCursorPosCallbackI {
    private final PositionCallback posCallback;

    public MousePositionCallback(PositionCallback posCallback) {
        this.posCallback = posCallback;
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        posCallback.invoke(window, xpos, ypos);
    }

}
