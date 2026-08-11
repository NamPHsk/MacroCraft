package dev.boxadactle.macrocraft.hud;

import dev.boxadactle.boxlib.layouts.LayoutComponent;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.layout.CenteredLayout;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class MacroControls {

    public static final Identifier PAUSE_DISABLED = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "textures/gui/pause_disabled.png");
    public static final Identifier PAUSE_ENABLED = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "textures/gui/pause_enabled.png");
    public static final Identifier PLAY_DISABLED = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "textures/gui/play_disabled.png");
    public static final Identifier PLAY_ENABLED = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "textures/gui/play_enabled.png");
    public static final Identifier STOP = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "textures/gui/stop.png");

    static int padding = 5;

    public static RenderingLayout createButtons(GuiGraphicsExtractor graphics, boolean disablePlayButton, boolean disablePauseButton) {
        RowLayout layout = new RowLayout(0, 0, padding);

        layout.addComponent(new LayoutContainerComponent(renderPlayButton(disablePlayButton)));
        layout.addComponent(new LayoutContainerComponent(renderPauseButton(disablePauseButton)));
        layout.addComponent(new LayoutContainerComponent(renderStopButton()));

        int w = graphics.guiWidth();
        int h = graphics.guiHeight();

        return new CenteredLayout(0, h - 100, w, layout.calculateRect().getHeight() + 20, layout);
    }

    static RenderingLayout renderPlayButton(boolean isDisabled) {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(isDisabled ? PLAY_DISABLED : PLAY_ENABLED));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(MacroCraftKeybinds.playMacro.getTranslatedKeyMessage())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(0, 0, 36, text.calculateRect().getHeight(), text)
        ));
        return layout;
    }

    static RenderingLayout renderPauseButton(boolean isDisabled) {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(isDisabled ? PAUSE_DISABLED : PAUSE_ENABLED));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(MacroCraftKeybinds.pauseMacro.getTranslatedKeyMessage())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(0, 0, 36, text.calculateRect().getHeight(), text)
        ));
        return layout;
    }

    static RenderingLayout renderStopButton() {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(STOP));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(MacroCraftKeybinds.stopMacro.getTranslatedKeyMessage())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(0, 0, 36, text.calculateRect().getHeight(), text)
        ));
        return layout;
    }

    private static class ImageRendererComponent extends LayoutComponent<Identifier> {
        public ImageRendererComponent(Identifier component) {
            super(component);
        }

        @Override
        public int getWidth() {
            return 36;
        }

        @Override
        public int getHeight() {
            return 36;
        }

        @Override
        public void render(GuiGraphicsExtractor graphics, int x, int y) {
            int size = 36;
            graphics.blit(RenderPipelines.GUI_TEXTURED, component, x, y, 0.0f, 0.0f, size, size, size, size);
        }
    }
}
