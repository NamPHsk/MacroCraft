package dev.boxadactle.macrocraft.config;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BScreenButton;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroCraftConfigScreen extends BOptionScreen {

    public MacroCraftConfigScreen(Screen parent) {
        super(parent, Component.translatable("screen.macrocraft.config.title", MacroCraft.VERSION_STRING));
        this.parent = parent;
        MacroCraft.CONFIG.cacheConfig();
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        layout.addChild(createCancelButton(b -> {
            MacroCraft.CONFIG.restoreCache();
            ClientUtils.setScreen(parent);
        }));

        setSaveButton(layout.addChild(createSaveButton(b -> {
            MacroCraft.CONFIG.save();
            ClientUtils.setScreen(parent);
        })));
    }

    @Override
    protected void addOptions() {
        BBooleanButton blockInputs = new BBooleanButton(
                "screen.macrocraft.config.blockInputsWhenPlaying",
                MacroCraft.CONFIG.get().blockInputsWhenPlaying,
                value -> MacroCraft.CONFIG.get().blockInputsWhenPlaying = value
        );
        blockInputs.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.blockInputsWhenPlaying.description")));
        addConfigLine(blockInputs);

        BBooleanButton ignoreMenu = new BBooleanButton(
                "screen.macrocraft.config.ignoreMenuNavigation",
                MacroCraft.CONFIG.get().ignoreMenuNavigation,
                value -> MacroCraft.CONFIG.get().ignoreMenuNavigation = value
        );
        ignoreMenu.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.ignoreMenuNavigation.description")));
        addConfigLine(ignoreMenu);

        BBooleanButton ignoreChat = new BBooleanButton(
                "screen.macrocraft.config.ignoreChatTyping",
                MacroCraft.CONFIG.get().ignoreChatTyping,
                value -> MacroCraft.CONFIG.get().ignoreChatTyping = value
        );
        ignoreChat.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.ignoreChatTyping.description")));
        addConfigLine(ignoreChat);

        BBooleanButton moveMouse = new BBooleanButton(
                "screen.macrocraft.config.moveMouseWhenPlaying",
                MacroCraft.CONFIG.get().moveMouseWhenPlaying,
                value -> MacroCraft.CONFIG.get().moveMouseWhenPlaying = value
        );
        moveMouse.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.moveMouseWhenPlaying.description")));
        addConfigLine(moveMouse);

        BBooleanButton renderHud = new BBooleanButton(
                "screen.macrocraft.config.shouldRenderHud",
                MacroCraft.CONFIG.get().shouldRenderHud,
                value -> MacroCraft.CONFIG.get().shouldRenderHud = value
        );
        renderHud.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.shouldRenderHud.description")));
        addConfigLine(renderHud);

        addConfigLine(new BSpacingEntry());
        addConfigLine(new BScreenButton(
                Component.translatable("screen.macrocraft.config.macroManager"),
                this,
                MacroListScreen::new
        ));
    }
}
