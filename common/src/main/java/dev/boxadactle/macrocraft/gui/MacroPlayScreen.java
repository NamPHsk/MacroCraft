package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BScreenButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroPlayScreen extends BOptionScreen {

    public MacroPlayScreen(Screen parent) {
        super(parent, Component.translatable("screen.macrocraft.play.title"));
        this.parent = parent;
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        layout.addChild(createBackButton(parent));
    }

    @Override
    protected void addOptions() {
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.play.loaded", MacroState.MACRO_NAME)));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.play.size", MacroState.LOADED_MACRO.actions.size())));
        addConfigLine(new BScreenButton(Component.translatable("screen.macrocraft.play.loadDifferent"), this, MacroListScreen::new));
        addConfigLine(new BSpacingEntry());

        StopButton stopButton = new StopButton();
        PlayButton playButton = new PlayButton(stopButton);
        addConfigLine(playButton, stopButton);
    }

    public static class PlayButton extends BCustomButton {
        StopButton stopButton;

        public PlayButton(StopButton stopButton) {
            super(Component.translatable(MacroState.LOADED_MACRO.isPlaying ? "screen.macrocraft.play.pause" : "screen.macrocraft.play.play"));
            this.stopButton = stopButton;
            stopButton.active = MacroState.LOADED_MACRO.isPlaying;
        }

        @Override
        protected void buttonClicked(BOptionButton<?> button) {
            if (!MacroState.LOADED_MACRO.isPlaying) {
                if (!MacroState.LOADED_MACRO.playMacro()) {
                    return;
                }
                button.setMessage(Component.translatable("screen.macrocraft.play.pause"));
                stopButton.active = true;
                ClientUtils.setScreen(null);
            } else {
                MacroState.LOADED_MACRO.pauseMacro();
                button.setMessage(Component.translatable("screen.macrocraft.play.play"));
                stopButton.active = false;
            }
        }
    }

    public static class StopButton extends BCustomButton {
        public StopButton() {
            super(Component.translatable("screen.macrocraft.play.stop"));
        }

        @Override
        protected void buttonClicked(BOptionButton<?> button) {
            MacroState.LOADED_MACRO.endMacro();
            button.active = false;
        }
    }
}
