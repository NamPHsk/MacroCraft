package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.KeyboardAction;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardListener {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void macrocraft$handleKey(long window, int action, KeyEvent event, CallbackInfo ci) {
        int key = event.key();

        if (!MacroState.IS_REPLAYING_INPUT) {
            if (MacroState.LOADED_MACRO.shouldRenderHud()) {
                MacroPlayHud.checkKeys(key);
            }
            if (MacroState.shouldRenderHud()) {
                MacroRecordHud.checkKeys(key);
            }
        }

        if (MacroState.IS_RECORDING && !MacroState.IS_REPLAYING_INPUT) {
            if ((ClientUtils.getCurrentScreen() instanceof ChatScreen && MacroCraft.CONFIG.get().ignoreChatTyping)
                    || ClientUtils.getOptions().keyChat.matches(event)) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to chat typing.");
            } else if (MacroCraftKeybinds.shouldIgnoreInput(key)) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to keybind.");
            } else if (key == 256 && ClientUtils.getClient().isPaused()) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to escape key.");
            } else {
                MacroState.addAction(new KeyboardAction(
                        MacroState.ticksElapsed,
                        event.key(),
                        event.scancode(),
                        action,
                        event.modifiers()
                ));
            }
        }

        if (MacroCraft.shouldIgnoreInput() && !MacroState.IS_REPLAYING_INPUT) {
            ci.cancel();
        }
    }
}
