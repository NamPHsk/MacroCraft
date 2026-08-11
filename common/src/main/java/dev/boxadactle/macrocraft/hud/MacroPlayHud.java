package dev.boxadactle.macrocraft.hud;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.CenteredLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class MacroPlayHud {

    public static final Identifier PROGRESS_BAR = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "progress/progress_bar");
    public static final Identifier PROGRESS_BACKGROUND = Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "progress/progress_empty");

    public static void render(GuiGraphicsExtractor graphics) {
        RowLayout information = new RowLayout(0, 0, 10);

        information.addComponent(new CenteredParagraphComponent(
                0,
                Component.translatable("hud.macrocraft.elapsed", MacroCraft.formatTicks(MacroState.LOADED_MACRO.ticksElapsed))
        ));

        information.addComponent(new CenteredParagraphComponent(
                10,
                Component.translatable("hud.macrocraft.playing", MacroState.MACRO_NAME),
                Component.translatable(
                        "hud.macrocraft.hide",
                        GuiUtils.brackets(MacroCraftKeybinds.hideGui.getTranslatedKeyMessage())
                )
        ));

        information.addComponent(new CenteredParagraphComponent(
                0,
                Component.translatable("hud.macrocraft.length", MacroCraft.formatTicks(MacroState.LOADED_MACRO.duration))
        ));

        CenteredLayout centeredInformation = new CenteredLayout(
                10, 15,
                graphics.guiWidth() - 10,
                information.calculateRect().getHeight() + 5,
                information
        );
        centeredInformation.render(graphics);

        RenderingLayout controls = MacroControls.createButtons(
                graphics,
                MacroState.LOADED_MACRO.isPlaying,
                MacroState.LOADED_MACRO.isPaused
        );
        controls.render(graphics);

        int buttonX = 65;
        int buttonY = controls.calculateRect().getY() - 19;
        int buttonWidth = Math.max(4, graphics.guiWidth() - 130);
        int buttonHeight = 12;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BACKGROUND, buttonX, buttonY, buttonWidth, buttonHeight);

        int barX = buttonX + 1;
        int barY = buttonY + 1;
        int barMaxWidth = buttonWidth - 2;
        int barHeight = buttonHeight - 2;

        int duration = Math.max(1, MacroState.LOADED_MACRO.duration);
        float completion = Math.min(1.0f, Math.max(0.0f, (float) MacroState.LOADED_MACRO.ticksElapsed / duration));
        int barWidth = Math.round(completion * barMaxWidth);

        String elapsed = MacroCraft.formatTicks(MacroState.LOADED_MACRO.ticksElapsed);
        String remaining = MacroCraft.formatTicks(Math.max(0, MacroState.LOADED_MACRO.duration - MacroState.LOADED_MACRO.ticksElapsed));

        RenderUtils.drawText(graphics, elapsed, buttonX - GuiUtils.getTextRenderer().width(elapsed) - 5, buttonY + 2);
        RenderUtils.drawText(graphics, "-" + remaining, buttonX + buttonWidth + 5, buttonY + 2);

        if (barWidth > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR, barX, barY, barWidth, barHeight);
        }
    }

    public static void checkKeys(int code) {
        MacroCraftKeybinds.checkKeybind(
                code,
                () -> {
                    if (MacroState.LOADED_MACRO.isPaused) MacroState.LOADED_MACRO.resumeMacro();
                },
                () -> {
                    if (MacroState.LOADED_MACRO.isPlaying) MacroState.LOADED_MACRO.pauseMacro();
                },
                () -> {
                    if (MacroState.LOADED_MACRO.isPlaying || MacroState.LOADED_MACRO.isPaused) MacroState.LOADED_MACRO.endMacro();
                }
        );
    }
}
