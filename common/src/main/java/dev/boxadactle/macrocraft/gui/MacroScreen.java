package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.KeyboardAction;
import dev.boxadactle.macrocraft.macro.action.MouseButtonAction;
import dev.boxadactle.macrocraft.macro.action.MousePositionAction;
import dev.boxadactle.macrocraft.macro.action.MouseScrollAction;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class MacroScreen extends BOptionScreen {

    private Macro macro;
    private final Path file;

    protected MacroScreen(Screen parent, Path macroFile) {
        super(parent, Component.literal(macroFile.toFile().getName()));
        this.parent = parent;
        this.file = macroFile;
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        layout.addChild(createBackButton(parent));
    }

    @Override
    protected void addOptions() {
        this.macro = MacroFile.loadMacro(file);
        if (macro == null) {
            addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.loadError")));
            return;
        }

        String filename = file.toFile().getName();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.filename", filename)));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.filepath", file.toFile().getAbsolutePath())));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.lastModified", MacroCraft.formatDate(file.toFile().lastModified()))));

        addConfigLine(new BCustomButton(Component.translatable("screen.macrocraft.macro.openfolder")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                if (MacroCraft.openMacroFolder()) {
                    button.setMessage(Component.translatable("screen.macrocraft.macro.openfolder.success"));
                } else {
                    button.setMessage(Component.translatable("screen.macrocraft.macro.openfolder.error"));
                }
                button.active = false;
            }
        });

        addConfigLine(new BSpacingEntry());
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.duration", MacroCraft.formatTicks(macro.duration))));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.actions", macro.actions.size())));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.keyboardActions", macro.actions.stream().filter(action -> action instanceof KeyboardAction).count())));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mouseButtonActions", macro.actions.stream().filter(action -> action instanceof MouseButtonAction).count())));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mousePositionActions", macro.actions.stream().filter(action -> action instanceof MousePositionAction).count())));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mouseScrollActions", macro.actions.stream().filter(action -> action instanceof MouseScrollAction).count())));
        addConfigLine(new BSpacingEntry());

        BCustomButton loadButton = new BCustomButton(Component.translatable("screen.macrocraft.macro.loadMacro")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                MacroCraft.LOGGER.info("Loading macro " + filename + "...");
                MacroState.loadMacro(macro, filename);
                MacroState.IS_RECORDING = false;
                MacroState.ticksElapsed = 0;
                ClientUtils.setScreen(parent);
            }
        };
        loadButton.active = !MacroState.IS_RECORDING && !MacroState.LOADED_MACRO.isPlaying;
        addConfigLine(loadButton);
    }
}
