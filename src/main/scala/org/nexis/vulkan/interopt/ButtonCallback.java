package org.nexis.vulkan.interopt;

@FunctionalInterface
public interface ButtonCallback {

    void invoke(long window, int button, int action, int mods);
    
}
