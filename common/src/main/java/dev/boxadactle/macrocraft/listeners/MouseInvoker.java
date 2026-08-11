package dev.boxadactle.macrocraft.listeners;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseInvoker {

    @Invoker("onButton")
    void invokeMouseButton(long window, MouseButtonInfo buttonInfo, int action);

    @Invoker("onMove")
    void invokeMove(long window, double x, double y);

    @Invoker("onScroll")
    void invokeScroll(long window, double xOffset, double yOffset);
}
