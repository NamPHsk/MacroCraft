package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.listeners.MouseInvoker;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;

public class MouseButtonAction extends MacroAction {
    int button;
    int action;
    int mods;

    public MouseButtonAction(int startTicks, int button, int action, int mods) {
        super(startTicks);
        this.button = button;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public void execute() {
        MouseHandler handler = ClientUtils.getClient().mouseHandler;
        long window = ClientUtils.getWindow();

        MacroState.IS_REPLAYING_INPUT = true;
        try {
            ((MouseInvoker) handler).invokeMouseButton(window, new MouseButtonInfo(button, mods), action);
        } finally {
            MacroState.IS_REPLAYING_INPUT = false;
        }
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("button", button);
        dataObject.addProperty("action", action);
        dataObject.addProperty("mods", mods);
        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        button = object.get("button").getAsInt();
        action = object.get("action").getAsInt();
        mods = object.get("mods").getAsInt();
    }
}
