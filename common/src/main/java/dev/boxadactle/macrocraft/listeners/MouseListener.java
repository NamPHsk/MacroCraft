package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.MouseButtonAction;
import dev.boxadactle.macrocraft.macro.action.MousePositionAction;
import dev.boxadactle.macrocraft.macro.action.MouseScrollAction;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseListener {

    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    private void macrocraft$onMove(long window, double x, double y, CallbackInfo ci) {
        if (MacroState.IS_RECORDING && !MacroState.IS_REPLAYING_INPUT) {
            MacroState.addAction(new MousePositionAction(MacroState.ticksElapsed, x, y));
        }
        if (MacroCraft.shouldIgnoreInput() && !MacroState.IS_REPLAYING_INPUT) {
            ci.cancel();
        }
    }

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void macrocraft$onButton(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
        if (MacroState.IS_RECORDING && !MacroState.IS_REPLAYING_INPUT) {
            MacroState.addAction(new MouseButtonAction(
                    MacroState.ticksElapsed,
                    buttonInfo.button(),
                    action,
                    buttonInfo.modifiers()
            ));
        }
        if (MacroCraft.shouldIgnoreInput() && !MacroState.IS_REPLAYING_INPUT) {
            ci.cancel();
        }
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void macrocraft$onScroll(long window, double xOffset, double yOffset, CallbackInfo ci) {
        if (MacroState.IS_RECORDING && !MacroState.IS_REPLAYING_INPUT) {
            MacroState.addAction(new MouseScrollAction(MacroState.ticksElapsed, xOffset, yOffset));
        }
        if (MacroCraft.shouldIgnoreInput() && !MacroState.IS_REPLAYING_INPUT) {
            ci.cancel();
        }
    }
}
