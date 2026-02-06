package org.nexis.vulkan.interoperability;

@FunctionalInterface
public interface ButtonCallback {

    void invoke(long window, int button, int action, int mods);
    
}
