package dev.boxadactle.macrocraft.fabric;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class MacroCraftFabric implements ClientModInitializer {
    private static final Identifier HUD_ELEMENT = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "macro_hud");

    @Override
    public void onInitializeClient() {
        MacroCraft.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, HUD_ELEMENT, this::extractMacroHud);

        initKeybinds();
    }

    private void initKeybinds() {
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.hideGui);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.pauseMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.playMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.stopMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.openMacroList);
    }

    private void tick(Minecraft client) {
        MacroState.tick();

        if (MacroCraftKeybinds.openMacroList.consumeClick()) {
            ClientUtils.setScreen(new MacroListScreen(null));
        }
    }

    private void extractMacroHud(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        if (MacroState.LOADED_MACRO.shouldRenderHud()) {
            MacroPlayHud.render(graphics);
        }

        if (MacroState.shouldRenderHud()) {
            MacroRecordHud.render(graphics);
        }
    }
}
