package dev.boxadactle.macrocraft.listeners;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InputConstants.class)
public class InputConstantsMixin {

    @Inject(
            method = "grabOrReleaseMouse",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void ignoreMouseGrabbing(Window window, int cursorMode, double x, double y, CallbackInfo ci) {
        if (MacroCraft.shouldIgnoreInput() && !MacroState.IS_REPLAYING_INPUT) {
            ci.cancel();
        }
    }

}
