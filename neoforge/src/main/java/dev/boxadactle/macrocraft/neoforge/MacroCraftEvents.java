package dev.boxadactle.macrocraft.neoforge;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = MacroCraft.MOD_ID, value = Dist.CLIENT)
public class MacroCraftEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post ignored) {
        MacroState.tick();

        if (MacroCraftKeybinds.openMacroList.consumeClick()) {
            ClientUtils.setScreen(new MacroListScreen(null));
        }
    }

    @SubscribeEvent
    public static void renderMacroHud(RenderGuiEvent.Post event) {
        if (MacroState.LOADED_MACRO.shouldRenderHud()) {
            MacroPlayHud.render(event.getGuiGraphicsExtractor());
        }

        if (MacroState.shouldRenderHud()) {
            MacroRecordHud.render(event.getGuiGraphicsExtractor());
        }
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(MacroCraftKeybinds.playMacro);
        event.register(MacroCraftKeybinds.pauseMacro);
        event.register(MacroCraftKeybinds.stopMacro);
        event.register(MacroCraftKeybinds.hideGui);
        event.register(MacroCraftKeybinds.openMacroList);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                (minecraft, screen) -> new MacroCraftConfigScreen(screen)
        );
    }
}
