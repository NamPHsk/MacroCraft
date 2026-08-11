package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.field.BStringField;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.config.widget.label.BLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroSaveScreen extends BOptionScreen {
    String fileName = "";
    BCustomButton saveButton;

    public MacroSaveScreen(Screen parent) {
        super(parent, Component.translatable("screen.macrocraft.save.title"));
        this.parent = parent;
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        layout.addChild(createCancelButton(parent));
    }

    @Override
    protected void addOptions() {
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.loaded", MacroState.MACRO_NAME)));
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.size", MacroState.LOADED_MACRO.actions.size())));
        addConfigLine(new BSpacingEntry());

        if (MacroState.HAS_UNSAVED_CHANGES) {
            saveButton = new BCustomButton(Component.translatable("screen.macrocraft.save.save")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    String file = MacroFile.resolveFilename(fileName);
                    MacroCraft.LOGGER.info("Saving macro to file: " + file);

                    Runnable save = () -> {
                        MacroFile.saveMacro(fileName, MacroState.LOADED_MACRO);
                        MacroState.loadMacro(MacroState.LOADED_MACRO, fileName);
                        ClientUtils.setScreen(parent);
                    };

                    if (MacroFile.doesMacroExist(fileName)) {
                        ClientUtils.confirm(
                                Component.translatable("screen.macrocraft.overwrite.title", file),
                                Component.translatable("screen.macrocraft.overwrite.description", file),
                                () -> {
                                    MacroFile.deleteMacroFile(fileName);
                                    save.run();
                                },
                                () -> ClientUtils.setScreen(MacroSaveScreen.this)
                        );
                        return;
                    }

                    save.run();
                }
            };
            saveButton.active = false;

            addConfigLine(
                    new BStringField("", value -> {
                        fileName = value.trim();
                        saveButton.active = !fileName.isEmpty();
                    }),
                    new BLabel(Component.literal(MacroFile.MACRO_EXTENSION))
            );
            addConfigLine(saveButton);
        } else {
            addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.save.alreadySaved")));
        }
    }
}
