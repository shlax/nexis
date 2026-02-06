package org.nexis.vulkan.interoperability;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseButtonCallback implements GLFWMouseButtonCallbackI {
    private final ButtonCallback buttonCallback;
    
    public MouseButtonCallback(ButtonCallback buttonCallback) {
        this.buttonCallback = buttonCallback;
    }
    
    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttonCallback.invoke(window, button, action, mods);
    }
}
