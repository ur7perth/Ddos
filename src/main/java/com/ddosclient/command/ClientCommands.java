package com.ddosclient.command;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.text.Text;

public class ClientCommands {

    public static void register() {
        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.getNetworkHandler() == null) return true;

            if (message.equalsIgnoreCase(".pl")) {
                // Request plugin list via tab-complete trick
                mc.getNetworkHandler().sendPacket(
                    new RequestCommandCompletionsC2SPacket(0, "bukkit:ver ")
                );
                mc.getNetworkHandler().sendPacket(
                    new RequestCommandCompletionsC2SPacket(1, "pl ")
                );
                mc.getNetworkHandler().sendPacket(
                    new RequestCommandCompletionsC2SPacket(2, "plugins ")
                );
                mc.player.sendMessage(
                    Text.literal("[DDoS] Plugin probe sent — check completions/chat for response."),
                    false
                );
                return false;
            }

            if (message.equalsIgnoreCase(".ver")) {
                mc.getNetworkHandler().sendPacket(
                    new RequestCommandCompletionsC2SPacket(10, "version ")
                );
                mc.getNetworkHandler().sendPacket(
                    new RequestCommandCompletionsC2SPacket(11, "ver ")
                );
                String serverBrand = mc.player.getServerBrand() != null
                    ? mc.player.getServerBrand() : "unknown";
                mc.player.sendMessage(
                    Text.literal("[DDoS] Server Brand: " + serverBrand +
                        " | MC: 1.20.1 | Tab-complete probe sent."),
                    false
                );
                return false;
            }

            return true;
        });
    }
}
