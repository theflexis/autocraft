package com.autocraft.mod;

import com.autocraft.mod.gui.AutoCraftScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AutoCraftClient implements ClientModInitializer {
    public static AutoCraftConfig CONFIG;

    private static KeyBinding openConfigKey;
    private static KeyBinding toggleActiveKey;

    @Override
    public void onInitializeClient() {
        CONFIG = AutoCraftConfig.load();

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autocraft.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "category.autocraft"
        ));

        toggleActiveKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autocraft.toggle",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.autocraft"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AutoCraftScreen(null));
                }
            }

            while (toggleActiveKey.wasPressed()) {
                CONFIG.active = !CONFIG.active;
                CONFIG.save();
                if (CONFIG.chatFeedback && client.player != null) {
                    client.player.sendMessage(
                            Text.literal("[AutoCraft] " + (CONFIG.active ? "Açık" : "Kapalı")),
                            true
                    );
                }
            }

            AutoCraftLogic.tick(client);
        });
    }
}
