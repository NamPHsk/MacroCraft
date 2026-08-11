package dev.boxadactle.macrocraft.gui;

import com.google.common.collect.ImmutableList;
import dev.boxadactle.boxlib.gui.config.BConfigList;
import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionHelper;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BScreenButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.config.widget.label.BLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.util.List;

public class MacroListScreen extends BOptionScreen {
    public MacroListScreen(Screen parent) {
        super(parent, Component.translatable("screen.macrocraft.macroList.title"));
        this.parent = parent;
    }

    @Override
    protected int getRowWidth() {
        return 350;
    }

    @Override
    protected int getRowHeight() {
        return super.getRowHeight() + 4;
    }

    @Override
    protected int getScrollbarX() {
        return Math.min(width - 10, width / 2 + 190);
    }

    @Override
    protected void initFooter(LinearLayout layout) {
        Button doneButton = createDoneButton(parent);
        doneButton.setMessage(GuiUtils.DONE);
        layout.addChild(doneButton);

        Button recordButton = Button.builder(
                Component.translatable("screen.macrocraft.macroList.record"),
                b -> ClientUtils.setScreen(new MacroRecordScreen(this))
        ).build();
        layout.addChild(recordButton);

        Button playButton = Button.builder(
                Component.translatable("screen.macrocraft.macroList.play"),
                b -> ClientUtils.setScreen(new MacroPlayScreen(this))
        ).build();
        layout.addChild(playButton);
    }

    @Override
    protected void addOptions() {
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.loaded", MacroState.MACRO_NAME)));

        List<File> macroFiles = MacroFile.getMacroFiles();
        if (!macroFiles.isEmpty()) {
            for (File file : macroFiles) {
                configList.addEntry(new FileEntry(file.getName()));
            }
        } else {
            addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macroList.noMacros")));
        }
    }

    public class FileEntry extends BConfigList.ConfigEntry {
        private final BLabel label;
        private final BCustomButton loadButton;
        private final BScreenButton editButton;
        private final BCustomButton deleteButton;

        public FileEntry(String filename) {
            this.label = new BLabel(Component.literal(filename));
            this.loadButton = new BCustomButton(Component.translatable("screen.macrocraft.macroList.load")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    Macro macro = MacroFile.loadMacro(filename);
                    if (macro != null) {
                        MacroState.loadMacro(macro, MacroFile.resolveFilename(filename));
                        ClientUtils.setScreen(MacroListScreen.this.parent);
                    }
                }
            };

            this.editButton = new BScreenButton(
                    Component.translatable("screen.macrocraft.macroList.edit"),
                    MacroListScreen.this,
                    p -> new MacroScreen(p, MacroFile.resolveMacroPath(filename))
            );

            this.deleteButton = new BCustomButton(Component.translatable("screen.macrocraft.macroList.delete")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    ClientUtils.confirm(
                            Component.translatable("screen.macrocraft.macroList.delete.title"),
                            Component.translatable("screen.macrocraft.macroList.delete.description", filename),
                            () -> {
                                MacroFile.deleteMacroFile(filename);
                                ClientUtils.setScreen(new MacroListScreen(MacroListScreen.this.parent));
                            },
                            () -> ClientUtils.setScreen(new MacroListScreen(MacroListScreen.this.parent))
                    );
                }
            };
        }

        @Override
        public List<? extends AbstractWidget> getWidgets() {
            return ImmutableList.of(label, loadButton, editButton, deleteButton);
        }

        @Override
        public boolean isInvalid() {
            return label.isInvalid() || loadButton.isInvalid() || editButton.isInvalid() || deleteButton.isInvalid();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int padding = BOptionHelper.padding();
            int x = getX();
            int y = getY();
            int entryWidth = getWidth();

            int labelWidth = (int) (entryWidth * 0.62) - padding;
            int buttonsWidth = entryWidth - labelWidth;
            int buttonWidth = Math.max(1, buttonsWidth / 3 - padding);

            label.setX(x);
            label.setY(y);
            label.setWidth(labelWidth);

            loadButton.setX(x + labelWidth + padding);
            loadButton.setY(y);
            loadButton.setWidth(buttonWidth);

            editButton.setX(x + labelWidth + padding + buttonWidth + padding);
            editButton.setY(y);
            editButton.setWidth(buttonWidth);

            deleteButton.setX(x + labelWidth + padding + (buttonWidth + padding) * 2);
            deleteButton.setY(y);
            deleteButton.setWidth(buttonWidth);

            label.extractRenderState(graphics, mouseX, mouseY, tickDelta);
            loadButton.extractRenderState(graphics, mouseX, mouseY, tickDelta);
            editButton.extractRenderState(graphics, mouseX, mouseY, tickDelta);
            deleteButton.extractRenderState(graphics, mouseX, mouseY, tickDelta);
        }
    }
}
