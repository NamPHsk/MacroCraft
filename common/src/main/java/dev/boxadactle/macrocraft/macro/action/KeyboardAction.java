package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.listeners.KeyboardInvoker;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;

public class KeyboardAction extends MacroAction {
    int key;
    int scancode;
    int action;
    int mods;

    public KeyboardAction(int startTicks, int key, int scancode, int action, int mods) {
        super(startTicks);
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public void execute() {
        KeyboardHandler handler = ClientUtils.getClient().keyboardHandler;
        long window = ClientUtils.getWindow();

        MacroState.IS_REPLAYING_INPUT = true;
        try {
            ((KeyboardInvoker) handler).invokeKeyPress(window, action, new KeyEvent(key, scancode, mods));
        } finally {
            MacroState.IS_REPLAYING_INPUT = false;
        }
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("key", key);
        dataObject.addProperty("scancode", scancode);
        dataObject.addProperty("action", action);
        dataObject.addProperty("mods", mods);
        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        key = object.get("key").getAsInt();
        scancode = object.get("scancode").getAsInt();
        action = object.get("action").getAsInt();
        mods = object.get("mods").getAsInt();
    }
}
