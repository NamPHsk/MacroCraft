package dev.boxadactle.macrocraft;

import com.mojang.blaze3d.platform.InputConstants;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class MacroCraftKeybinds {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MacroCraft.MOD_ID, "macrocraft")
    );

    public static final KeyMapping hideGui = new KeyMapping("key.macrocraft.hide_gui", GLFW.GLFW_KEY_X, CATEGORY);
    public static final KeyMapping playMacro = new KeyMapping("key.macrocraft.play_macro", GLFW.GLFW_KEY_K, CATEGORY);
    public static final KeyMapping pauseMacro = new KeyMapping("key.macrocraft.pause_macro", GLFW.GLFW_KEY_P, CATEGORY);
    public static final KeyMapping stopMacro = new KeyMapping("key.macrocraft.stop_macro", GLFW.GLFW_KEY_L, CATEGORY);
    public static final KeyMapping openMacroList = new KeyMapping("key.macrocraft.open_macro_list", GLFW.GLFW_KEY_C, CATEGORY);

    private static boolean matches(KeyMapping mapping, int code) {
        return mapping.matches(InputConstants.Type.KEYSYM.getOrCreate(code));
    }

    public static void checkKeybind(int code, Runnable resume, Runnable pause, Runnable stop) {
        if (matches(hideGui, code)) {
            MacroCraft.CONFIG.get().shouldRenderHud = !MacroCraft.CONFIG.get().shouldRenderHud;
            MacroCraft.CONFIG.save();
        } else if (matches(playMacro, code)) {
            resume.run();
        } else if (matches(pauseMacro, code)) {
            pause.run();
        } else if (matches(stopMacro, code)) {
            stop.run();
        }
    }

    public static boolean shouldIgnoreInput(int code) {
        return matches(hideGui, code)
                || matches(playMacro, code)
                || matches(pauseMacro, code)
                || matches(stopMacro, code);
    }
}
