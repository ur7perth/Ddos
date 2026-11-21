package com.ddosclient;

import com.ddosclient.attack.AttackManager;
import com.ddosclient.command.ClientCommands;
import com.ddosclient.gui.AttackScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DdosClient implements ClientModInitializer {

    public static final String MOD_ID = "ddosclient";
    public static AttackManager attackManager;
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        attackManager = new AttackManager();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ddosclient.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "category.ddosclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new AttackScreen());
                }
            }
            attackManager.tick(client);
        });

        ClientCommands.register();
    }

    public static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }
}
