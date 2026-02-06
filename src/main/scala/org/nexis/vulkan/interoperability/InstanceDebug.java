package org.nexis.vulkan.interoperability;

import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackEXTI;
import org.nexis.vulkan.Instance;

public class InstanceDebug implements VkDebugUtilsMessengerCallbackEXTI {
    private final Instance instance;

    public InstanceDebug(Instance instance) {
        this.instance = instance;
    }

    @Override
    public int invoke(int messageSeverity, int messageTypes, long pCallbackData, long pUserData) {
        return instance.invoke(messageSeverity, messageTypes, pCallbackData, pUserData);
    }
}
